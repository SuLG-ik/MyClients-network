package beauty.shafran.network.auth.data

import com.fasterxml.jackson.annotation.JsonProperty


class RefreshTokenRequest(
    @JsonProperty("accessToken")
    val accessToken: String,
)

class RefreshTokenResponse(
    val token: TokensData,
)

