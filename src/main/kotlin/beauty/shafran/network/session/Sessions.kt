package beauty.shafran.network.session

import beauty.shafran.network.session.data.*
import beauty.shafran.network.session.executor.SessionsExecutor
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid


@RequestMapping("/v1/sessions")
@RestController()
class Sessions(
    private val executor: SessionsExecutor,
) {

    @RequestMapping(
        value = ["/getSessionUsagesHistory"],
        method = [RequestMethod.GET],
        produces = [MediaType.APPLICATION_JSON_VALUE],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
    )
    suspend fun getSessionUsagesHistory(@Valid @RequestBody request: GetSessionUsagesHistoryRequest): GetSessionUsagesHistoryResponse {
        return executor.getSessionUsagesHistory(request)
    }

    @RequestMapping(
        value = ["/getAllServiceSessionsForCustomer"],
        method = [RequestMethod.GET],
        produces = [MediaType.APPLICATION_JSON_VALUE],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
    )
    suspend fun getAllServiceSessionsForCustomer(@Valid @RequestBody request: GetSessionsForCustomerRequest): GetSessionsForCustomerResponse {
        return executor.getAllSessionsForCustomer(request)
    }

    @RequestMapping(
        value = ["/getServiceSessionsIgnoreDeactivatedForCustomer"],
        method = [RequestMethod.GET],
        produces = [MediaType.APPLICATION_JSON_VALUE],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
    )
    suspend fun getServiceSessionsIgnoreDeactivatedForCustomer(@Valid @RequestBody request: GetSessionsForCustomerRequest): GetSessionsForCustomerResponse {
        return executor.getSessionsIgnoreDeactivatedForCustomer(request)
    }


    @RequestMapping(
        value = ["/getServiceSessionsForCustomer"],
        method = [RequestMethod.GET],
        produces = [MediaType.APPLICATION_JSON_VALUE],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
    )
    @Deprecated(message = "Must be replaced in next release",
        replaceWith = ReplaceWith("getServiceSessionsIgnoreDeactivatedForCustomer(request)"))
    suspend fun getServiceSessionsForCustomer(@Valid @RequestBody request: GetSessionsForCustomerRequest): GetSessionsForCustomerResponse {
        return getServiceSessionsIgnoreDeactivatedForCustomer(request)
    }

    @RequestMapping(
        value = ["/useServiceSession"],
        method = [RequestMethod.PUT],
        produces = [MediaType.APPLICATION_JSON_VALUE],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
    )
    suspend fun useServiceSession(@Valid @RequestBody request: UseSessionRequest): UseSessionResponse {
        return executor.useSession(request)
    }


    @RequestMapping(
        value = ["/createServiceSessionsForCustomer"],
        method = [RequestMethod.POST],
        produces = [MediaType.APPLICATION_JSON_VALUE],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
    )
    suspend fun getSessionUsagesHistory(@Valid @RequestBody request: CreateSessionForCustomerRequest): CreateSessionForCustomerResponse {
        return executor.createSessionsForCustomer(request)
    }

    @RequestMapping(
        value = ["/deactivateServiceSession"],
        method = [RequestMethod.DELETE],
        produces = [MediaType.APPLICATION_JSON_VALUE],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
    )
    suspend fun deactivateServiceSession(@Valid @RequestBody request: DeactivateSessionRequest): DeactivateSessionResponse {
        return executor.deactivateSession(request)
    }

}