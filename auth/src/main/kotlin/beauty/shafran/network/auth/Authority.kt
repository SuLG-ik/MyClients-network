package beauty.shafran.network.auth

import org.springframework.security.core.GrantedAuthority

@MustBeDocumented
@Retention(value = AnnotationRetention.BINARY)
@RequiresOptIn(
    level = RequiresOptIn.Level.WARNING,
    message = "This is a delicate API and its use requires care."
)
public annotation class DelicateAuthorizationApi

enum class Authority(private val authority: String) : GrantedAuthority {
    /**
     * Register new account
     */
    SCOPE_CREATE_USER("SCOPE_create_user"),

    /**
     * Read username
     */
    SCOPE_READ_PRIMARY_ACCOUNT_INFO("SCOPE_read_primary_account_info"),

    /**
     * Read email, phone, name,
     */
    SCOPE_READ_EXTENDED_ACCOUNT_INFO("SCOPE_read_extended_account_info"),

    /**
     * Has only refresh token to generate access token
     */
    SCOPE_REFRESH("SCOPE_refresh");

    override fun getAuthority(): String {
        return authority
    }

    override fun toString(): String {
        return authority
    }

    companion object {
        private val fullAccessedAuthorities by lazy { Authority.values().toList() - SCOPE_REFRESH }

        @DelicateAuthorizationApi
        fun fullAccessAuthorities(): List<Authority> {
            return fullAccessedAuthorities
        }
    }
}