package beauty.shafran.network.auth

import org.springframework.stereotype.Service


@Service
class AuthorizationService {


    private val fullAccessedAuthorities by lazy {
        Authority.values().toList() - (refreshTokenAuthorities().toSet() + anonymousTokenAuthorities().toSet())
    }

    @DelicateAuthorizationApi
    fun fullAccessAuthorities(): List<Authority> {
        return fullAccessedAuthorities
    }

    fun refreshTokenAuthorities(): List<Authority> {
        return listOf(Authority.SCOPE_REFRESH)
    }

    fun anonymousTokenAuthorities(): List<Authority> {
        return listOf(Authority.SCOPE_REGISTER_ACCOUNT, Authority.SCOPE_LOGIN_ACCOUNT)
    }

}