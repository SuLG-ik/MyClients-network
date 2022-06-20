package beauty.shafran.network.auth.data


class RegisterAccountRequest(
    val username: String,
    val password: String,
    val name: String,
)

class RegisterAccountResponse(
    val tokens: TokensData,
)

