package beauty.shafran.network.employees.converters

import beauty.shafran.network.assets.converter.AssetsConverter
import beauty.shafran.network.assets.entity.AssetEntity
import beauty.shafran.network.employees.data.Employee
import beauty.shafran.network.employees.data.EmployeeData
import beauty.shafran.network.employees.data.EmployeeId
import beauty.shafran.network.employees.data.EmployeeLayoff
import beauty.shafran.network.employees.entity.EmployeeDataEntity
import beauty.shafran.network.employees.entity.EmployeeEntity
import beauty.shafran.network.employees.entity.EmployeeLayoffEntity
import org.koin.core.annotation.Single

@Single
class EmployeesConverterImpl(
    private val assetsConverter: AssetsConverter,
) : EmployeesConverter {

    override fun buildEmployee(
        employeeEntity: EmployeeEntity,
        employeeDataEntity: EmployeeDataEntity,
        employeeLayoffEntity: EmployeeLayoffEntity?,
        image: AssetEntity?,
    ): Employee {
        return Employee(
            id = EmployeeId(employeeEntity.id),
            data = EmployeeData(
                name = employeeDataEntity.name,
                description = employeeDataEntity.description,
                gender = employeeDataEntity.gender
            ),
            image = with(assetsConverter) { image?.toData() },
            layoff = employeeLayoffEntity?.let {
                EmployeeLayoff(
                    date = it.creationDate,
                    note = it.note,
                )
            },
        )
    }

}