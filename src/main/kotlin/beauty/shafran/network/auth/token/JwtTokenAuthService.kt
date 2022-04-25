package beauty.shafran.network.auth.token

import beauty.shafran.network.TokenTimeout
import beauty.shafran.network.account.repository.AccountsRepository
import beauty.shafran.network.auth.data.*
import beauty.shafran.network.auth.entity.AccountSessionEntity
import beauty.shafran.network.auth.jwt.JwtConfig
import beauty.shafran.network.auth.repository.AccountSessionsRepository
import beauty.shafran.network.companies.repository.CompaniesRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.context.annotation.Primary
import org.springframework.security.oauth2.jose.jws.JwsAlgorithms
import org.springframework.security.oauth2.jwt.*
import org.springframework.stereotype.Service
import java.time.Instant

@Service
@Primary
class JwtTokenAuthService(
    private val accountsRepository: AccountsRepository,
    private val companiesRepository: CompaniesRepository,
    private val accountSessionsRepository: AccountSessionsRepository,
    private val jwtEncoder: JwtEncoder,
    private val jwtDecoder: ReactiveJwtDecoder,
    private val jwtConfig: JwtConfig,
) : TokenAuthService {

    private val header = JwsHeader.with { JwsAlgorithms.HS256 }.build()

    override suspend fun generateTokenForSession(session: AccountSessionEntity): JwtTokenData {
        val expiresIn = System.currentTimeMillis() + 30 * 60 * 1000
        val accessToken = generateJwtAccessToken(session, expiresIn)
        val refreshToken = generateJwtRefreshToken(session)
        return JwtTokenData(
            accessToken = JwtAccessTokenData(accessToken.tokenValue),
            refreshToken = JwtRefreshTokenData(refreshToken.tokenValue),
            expiresAt = expiresIn,
            sessionId = session.id.toString(),
            accountId = session.accountId,
        )
    }

    private fun generateJwtAccessToken(session: AccountSessionEntity, expiresIn: Long): Jwt {
        val claims = JwtClaimsSet.builder()
            .issuer(jwtConfig.issuer)
            .expiresAt(Instant.ofEpochMilli(expiresIn))
            .claim("sessionsId", session.id.toString())
            .issuedAt(Instant.ofEpochMilli(System.currentTimeMillis()))
            .build()
        return jwtEncoder.encode(JwtEncoderParameters.from(header, claims))
    }

    private fun generateJwtRefreshToken(session: AccountSessionEntity): Jwt {
        val claims = JwtClaimsSet.builder()
            .issuer(jwtConfig.issuer)
            .expiresAt(Instant.ofEpochMilli(System.currentTimeMillis() + 30L * 24 * 60 * 60 * 1000))
            .claim("sessionsId", session.id.toString())
            .issuedAt(Instant.ofEpochMilli(System.currentTimeMillis()))
            .build()
        return jwtEncoder.encode(JwtEncoderParameters.from(header, claims))
    }

    override suspend fun findSessionWithRefreshToken(refreshTokenData: RefreshTokenData): AccountSessionEntity {
        return when (refreshTokenData) {
            is JwtRefreshTokenData -> refreshTokenData.findSession()
        }
    }

    private suspend fun JwtRefreshTokenData.findSession(): AccountSessionEntity {
        val jwt = jwtDecoder.decode(refreshToken).awaitSingle()
        if (Instant.now() > jwt.expiresAt) {
            throw TokenTimeout()
        }
        val sessionId = jwt.getClaimAsString("sessionId")
        return accountSessionsRepository.findSessionWithId(sessionId)
    }

    override suspend fun authorizeAccount(sessionId: String): AuthorizedAccount {
        return coroutineScope {
            val session = accountSessionsRepository.findSessionWithId(sessionId)
            val account = async { accountsRepository.findAccountById(session.accountId) }
            val members = async { companiesRepository.findAccountMembersOfAccount(session.accountId) }
            return@coroutineScope AuthorizedAccount(
                accountId = account.await().id.toString(),
                data = AuthorizedAccountData(account.await().data.username),
                scopes = members.await().flatMap { it.formattedAccessScope }
            )
        }
    }
}