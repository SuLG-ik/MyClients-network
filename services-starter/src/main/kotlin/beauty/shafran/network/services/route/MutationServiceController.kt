package beauty.shafran.network.services.route

import beauty.shafran.network.companies.repository.CompanyPlacementRepository
import beauty.shafran.network.companies.repository.CompanyRepository
import beauty.shafran.network.services.entities.*
import beauty.shafran.network.services.repository.*
import graphql.schema.DataFetchingEnvironment
import org.springframework.data.repository.findByIdOrNull
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.SchemaMapping
import org.springframework.stereotype.Controller
import org.springframework.transaction.annotation.Transactional

class ServicesMutation

class AddTimesLimitedServiceInput(
    val companyId: Long,
    val title: String,
    val description: String,
    val times: Int,
    val durationOfWork: Long,
)


class AddServiceToPlacementInput(
    val placementId: Long,
    val serviceId: Long,
)

@Controller
class MutationServiceController(
    private val serviceRepository: ServiceRepository,
    private val serviceDataRepository: ServiceDataRepository,
    private val companyRepository: CompanyRepository,
    private val placementRepository: CompanyPlacementRepository,
    private val serviceToPlacementRepository: ServiceToPlacementRepository,
    private val timesLimitedServiceRepository: TimesLimitedServiceRepository,
    private val serviceInfoRepository: ServiceInfoRepository,
) {


    @MutationMapping
    fun services() = ServicesMutation()


    @Transactional
    @SchemaMapping(typeName = "ServicesMutation")
    fun addTimesLimitedService(
        @Argument input: AddTimesLimitedServiceInput,
        environment: DataFetchingEnvironment,
    ): Service {
        val service = serviceRepository.save(
            ServiceEntity(
                company = companyRepository.getReferenceById(input.companyId)
            )
        )
        serviceDataRepository.save(
            ServiceDataEntity(
                service = service,
                title = input.title,
                description = input.description,
            )
        )
        timesLimitedServiceRepository.save(
            TimesLimitedServiceEntity(times = input.times, service = service)
        )
        serviceInfoRepository.save(
            ServiceInfoEntity(
                durationOfWork = input.durationOfWork,
                service = service,
            )
        )
        return Service(
            id = service.id,
            companyId = service.company.id
        )
    }

    @Transactional
    @SchemaMapping(typeName = "ServicesMutation")
    fun addToPlacement(@Argument input: AddServiceToPlacementInput, environment: DataFetchingEnvironment): Service {
        val service = serviceRepository.findByIdOrNull(input.serviceId) ?: TODO()
        val placement = placementRepository.findByIdOrNull(input.placementId) ?: TODO()
        if (service.company.id != placement.company.id)
            TODO()
        if (serviceToPlacementRepository.existsByServiceAndPlacement(service, placement))
            TODO()
        serviceToPlacementRepository.save(ServiceToPlacementEntity(placement = placement, service = service))
        return Service(
            id = service.id,
            companyId = service.company.id
        )
    }


}