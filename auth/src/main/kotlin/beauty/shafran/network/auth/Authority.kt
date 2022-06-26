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
    SCOPE_REFRESH("SCOPE_refresh"),


    /**
     * Create account and generate tokens for him
     */
    SCOPE_REGISTER_ACCOUNT("SCOPE_register_account"),

    /**
     * Generate tokens for registered token by credentials
     */
    SCOPE_LOGIN_ACCOUNT("SCOPE_login_account");

    override fun getAuthority(): String {
        return authority
    }

    override fun toString(): String {
        return authority
    }

}