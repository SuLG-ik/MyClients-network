package beauty.shafran.network.auth.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class RefreshTokenRequest {

    @Serializable
    @SerialName("refresh_jwt")
    class JwtRefreshTokenRequest(
        val accessToken: String,
    ) : RefreshTokenRequest()

}


@Serializable
class RefreshTokenResponse(
    val token: TokensData,
)

