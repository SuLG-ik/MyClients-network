package beauty.shafran.network.employees.route

import beauty.shafran.network.auth.AuthorizedAccount
import beauty.shafran.network.companies.data.CompanyId
import beauty.shafran.network.companies.repository.CompanyPlacementRepository
import beauty.shafran.network.database.Transactional
import beauty.shafran.network.database.invoke
import beauty.shafran.network.employees.converter.EmployeeConverter
import beauty.shafran.network.employees.data.*
import beauty.shafran.network.employees.repository.EmployeeRepository
import kotlinx.coroutines.awaitAll

internal class EmployeesRouterImpl(
    private val transactional: Transactional,
    private val converter: EmployeeConverter,
    private val employeeRepository: EmployeeRepository,
    private val placementsRepository: CompanyPlacementRepository,
) : EmployeesRouter {

    override suspend fun createEmployee(
        request: CreateEmployeeRequest,
        account: AuthorizedAccount,
    ): CreateEmployeeResponse {
        return transactional {
            val (employee, employeeData) = employeeRepository.createEmployee(
                name = request.data.name,
                description = request.data.description,
                companyId = request.companyId
            )
            placementsRepository.isPlacementsInCompanyStrict(CompanyId(employee.companyId), request.companyPlacementIds)
            val employeePlacements = employeeRepository.addEmployeeToCompanyPlacement(
                EmployeeId(employee.id),
                request.companyPlacementIds,
            )
            CreateEmployeeResponse(
                employee = converter.toEmployee(employee, employeeData, employeePlacements)
            )
        }
    }

    override suspend fun addEmployeeToPlacements(
        request: AddEmployeeToCompanyPlacementsRequest,
        account: AuthorizedAccount,
    ): AddEmployeeToCompanyPlacementsResponse {
        return transactional {
            val employee = employeeRepository.getEmployeeEntity(request.employeeId)
                ?: throw EmployeeNotExists()
            placementsRepository.isPlacementsInCompanyStrict(CompanyId(employee.companyId), request.placementIds)
            val placements = employeeRepository.addEmployeeToCompanyPlacement(
                employeeId = request.employeeId,
                placementIds = request.placementIds
            )
            AddEmployeeToCompanyPlacementsResponse(
                employeeCompanyMember = converter.toEmployeeCompanyMember(employee, placements)
            )
        }
    }

    override suspend fun getEmployeeById(
        request: GetEmployeeByIdRequest,
        account: AuthorizedAccount,
    ): GetEmployeeByIdResponse {
        return transactional {
            val (employeeEntity, employeeData) = employeeRepository.getEmployeeEntityAndData(request.employeeId)
                ?: throw EmployeeNotExists()
            val placements = employeeRepository.getEmployeePlacements(request.employeeId)
            GetEmployeeByIdResponse(
                employee = converter.toEmployee(employeeEntity, employeeData, placements)
            )
        }
    }

    override suspend fun getEmployeesByIds(
        request: GetEmployeesByIdsRequest,
        account: AuthorizedAccount,
    ): GetEmployeesByIdsResponse {
        return transactional {
            TODO()
        }
    }

    override suspend fun getCompanyEmployees(
        request: GetCompanyEmployeesRequest,
        account: AuthorizedAccount,
    ): GetCompanyEmployeesResponse {
        return transactional {
            GetCompanyEmployeesResponse(
                employeeIds = employeeRepository.getCompanyEmployeeEntities(request.companyId)
                    .map { EmployeeId(it.id) }
            )
        }
    }

    override suspend fun getCompanyEmployeesAndLoad(
        request: GetCompanyEmployeesAndLoadRequest,
        account: AuthorizedAccount,
    ): GetCompanyEmployeesAndLoadResponse {
        return transactional {
            GetCompanyEmployeesAndLoadResponse(
                employees = employeeRepository.getCompanyEmployeeEntitiesAndData(
                    request.companyId,
                ).map {
                    transactionAsync {
                        converter.toEmployee(
                            employeeEntity = it.first,
                            employeeData = it.second,
                            placements = employeeRepository.getEmployeePlacements(EmployeeId(it.first.id))
                        )
                    }
                }.awaitAll()
            )
        }
    }

    override suspend fun getPlacementEmployees(
        request: GetPlacementEmployeesRequest,
        account: AuthorizedAccount,
    ): GetPlacementEmployeesResponse {
        return transactional {
            GetPlacementEmployeesResponse(
                employeesIds = employeeRepository.getPlacementEmployeeEntities(request.placementsIds)
                    .map { EmployeeId(it.id) }
            )
        }
    }

    override suspend fun getPlacementEmployeesAndLoad(
        request: GetPlacementEmployeesAndLoadRequest,
        account: AuthorizedAccount,
    ): GetPlacementEmployeesAndLoadResponse {
        return transactional {
            GetPlacementEmployeesAndLoadResponse(
                employees = employeeRepository.getPlacementEmployeeEntitiesAndData(
                    request.placementsIds,
                ).map {
                    transactionAsync {
                        converter.toEmployee(
                            employeeEntity = it.first,
                            employeeData = it.second,
                            placements = employeeRepository.getEmployeePlacements(EmployeeId(it.first.id))
                        )
                    }
                }.awaitAll()
            )
        }
    }


}