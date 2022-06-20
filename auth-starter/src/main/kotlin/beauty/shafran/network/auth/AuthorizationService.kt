package beauty.shafran.network.auth

import org.springframework.stereotype.Service


@Service
class AuthorizationService {

    @DelicateAuthorizationApi
    fun fullAccessAuthorities(): List<Authority> {
        return Authority.fullAccessAuthorities()
    }

    fun refreshTokenAuthorities(): List<Authority> {
        return listOf(Authority.SCOPE_REFRESH)
    }

}