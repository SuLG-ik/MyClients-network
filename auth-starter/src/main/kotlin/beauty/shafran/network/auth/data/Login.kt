package beauty.shafran.network.auth.data

import com.fasterxml.jackson.annotation.JsonProperty


class LoginAccountRequest(
    val username: String,
    val password: String,
)


class LoginAccountResponse(
    val tokens: TokensData,
)
