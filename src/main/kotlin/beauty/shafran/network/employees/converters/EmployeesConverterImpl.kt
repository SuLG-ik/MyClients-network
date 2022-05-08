package beauty.shafran.network.employees.converters

import beauty.shafran.network.assets.converter.AssetsConverter
import beauty.shafran.network.employees.data.Employee
import beauty.shafran.network.employees.data.EmployeeData
import beauty.shafran.network.employees.data.EmployeeLayoff
import beauty.shafran.network.employees.data.LayoffEmployeeRequest
import beauty.shafran.network.employees.entity.EmployeeDataEntity
import beauty.shafran.network.employees.entity.EmployeeEntity
import beauty.shafran.network.employees.entity.EmployeeLayoffEntity
import beauty.shafran.network.utils.getZonedDateTime
import org.koin.core.annotation.Single

@Single
class EmployeesConverterImpl(
    private val assetsConverter: AssetsConverter,
) : EmployeesConverter {
    override suspend fun EmployeeEntity.toData(): Employee {
        return Employee(
            id = id.toString(),
            data = data.toData(),
            image = with(assetsConverter) { image?.toData() },
            layoff = layoff?.toData(),
        )
    }


    override suspend fun EmployeeDataEntity.toData(): EmployeeData {
        return EmployeeData(
            name = name,
            description = description,
            gender = gender,
        )
    }

    override suspend fun EmployeeLayoffEntity.toData(): EmployeeLayoff {
        return EmployeeLayoff(
            reason = reason,
            date = id.getZonedDateTime()
        )
    }

}