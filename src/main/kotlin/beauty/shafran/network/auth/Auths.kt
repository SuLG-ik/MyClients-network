package beauty.shafran.network.auth

import beauty.shafran.network.auth.data.*
import beauty.shafran.network.auth.executor.AuthenticationExecutor
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/auth")
class Auths(
    private val executor: AuthenticationExecutor,
) {

    @RequestMapping(
        value = ["/register"],
        method = [RequestMethod.POST],
        produces = [MediaType.APPLICATION_JSON_VALUE],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
    )
    suspend fun register(@RequestBody request: RegisterAccountRequest): RegisterAccountResponse {
        return executor.registerAccount(request)
    }

    @RequestMapping(
        value = ["/login"],
        method = [RequestMethod.GET],
        produces = [MediaType.APPLICATION_JSON_VALUE],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
    )
    suspend fun login(@RequestBody request: LoginAccountRequest): LoginAccountResponse {
        return executor.loginAccount(request)
    }

    @RequestMapping(
        value = ["/refresh"],
        method = [RequestMethod.GET],
        produces = [MediaType.APPLICATION_JSON_VALUE],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
    )
    suspend fun refresh(@RequestBody request: RefreshTokenRequest): RefreshTokenResponse {
        return executor.refreshAccount(request)
    }

}