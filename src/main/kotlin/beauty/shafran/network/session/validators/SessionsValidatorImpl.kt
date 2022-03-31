package beauty.shafran.network.session.validators

import beauty.shafran.network.session.data.CreateSessionForCustomerRequest
import beauty.shafran.network.session.data.GetSessionUsagesHistoryRequest
import beauty.shafran.network.session.data.GetSessionsForCustomerRequest
import beauty.shafran.network.session.data.UseSessionRequest
import beauty.shafran.network.validation.validateAndThrow
import jakarta.validation.Validator

class SessionsValidatorImpl(
    private val validator: Validator,
) : SessionsValidator {
    override suspend fun getSessionUsagesHistory(data: GetSessionUsagesHistoryRequest): GetSessionUsagesHistoryRequest {
        return validator.validateAndThrow(data)
    }

    override suspend fun getSessionsForCustomer(data: GetSessionsForCustomerRequest): GetSessionsForCustomerRequest {
        return validator.validateAndThrow(data)
    }

    override suspend fun createSessionsForCustomer(data: CreateSessionForCustomerRequest): CreateSessionForCustomerRequest {
        return validator.validateAndThrow(data)
    }

    override suspend fun useSession(data: UseSessionRequest): UseSessionRequest {
        return validator.validateAndThrow(data)
    }
}