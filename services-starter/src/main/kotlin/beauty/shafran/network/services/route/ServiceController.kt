package beauty.shafran.network.services.route

import beauty.shafran.network.companies.repository.CompanyRepository
import beauty.shafran.network.companies.route.Company
import beauty.shafran.network.companies.route.CompanyPlacement
import beauty.shafran.network.services.repository.*
import org.springframework.data.repository.findByIdOrNull
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.graphql.data.method.annotation.SchemaMapping
import org.springframework.stereotype.Controller
import org.springframework.transaction.annotation.Transactional

class ServicesQuery

class Service(
    val id: Long,
    val companyId: Long,
)

class ServiceData(
    val title: String,
    val description: String,
)

sealed interface ServiceInfo {
    val durationOfWork: Long
}

class TimesLimitedServiceInfo(
    val times: Int, override val durationOfWork: Long,
) : ServiceInfo

@Controller
class ServiceController(
    private val serviceRepository: ServiceRepository,
    private val companyRepository: CompanyRepository,
    private val serviceDataRepository: ServiceDataRepository,
    private val serviceToPlacementRepository: ServiceToPlacementRepository,
    private val timesLimitedServiceRepository: TimesLimitedServiceRepository,
    private val serviceInfoRepository: ServiceInfoRepository,
) {


    @QueryMapping
    fun services() = ServicesQuery()


    @SchemaMapping
    @Transactional
    fun service(source: ServicesQuery, @Argument id: String): Service {
        val service = serviceRepository.findByIdOrNull(id.toLong()) ?: TODO()
        return Service(
            id = service.id,
            companyId = service.company.id
        )
    }

    @SchemaMapping
    @Transactional
    fun services(source: Company): List<Service> {
        val services = serviceRepository.findAllByCompany(companyRepository.getReferenceById(source.id))
        return services.map { service ->
            Service(
                id = service.id,
                companyId = source.id
            )
        }
    }

    @SchemaMapping
    @Transactional
    fun info(source: Service): ServiceInfo {
        val reference = serviceRepository.getReferenceById(source.id)
        val timesLimited =
            timesLimitedServiceRepository.findByService(reference) ?: TODO()
        val info = serviceInfoRepository.findByService(reference) ?: TODO()
        return TimesLimitedServiceInfo(
            times = timesLimited.times,
            durationOfWork = info.durationOfWork
        )
    }

    @SchemaMapping
    @Transactional
    fun company(source: Service): Company {
        val company = companyRepository.findByIdOrNull(source.companyId) ?: TODO()
        return Company(
            id = company.id,
            codename = company.codename,
            title = company.title
        )
    }


    @SchemaMapping
    @Transactional
    fun data(source: Service): ServiceData {
        val data = serviceDataRepository.findByService(serviceRepository.getReferenceById(source.id))
        return ServiceData(
            title = data.title,
            description = data.description,
        )
    }

    @SchemaMapping
    @Transactional
    fun placements(source: Service): List<CompanyPlacement> {
        val placements =
            serviceToPlacementRepository.findAllByService(serviceRepository.getReferenceById(source.id))
        return placements.map {
            CompanyPlacement(
                id = it.placement.id,
                codename = it.placement.codename,
                companyId = it.placement.company.id,
                title = it.placement.title
            )
        }
    }

}