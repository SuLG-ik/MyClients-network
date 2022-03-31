package beauty.shafran.network.services.converter

import beauty.shafran.network.assets.converter.AssetsConverter
import beauty.shafran.network.services.data.*
import beauty.shafran.network.services.enity.ServiceConfigurationEntity
import beauty.shafran.network.services.enity.ServiceDeactivationEntity
import beauty.shafran.network.services.enity.ServiceEntity
import beauty.shafran.network.services.enity.ServiceInfoEntity
import org.litote.kmongo.newId

class ServicesConverterImpl(
    private val converter: AssetsConverter,
) : ServicesConverter {

    override fun DeactivateServiceConfigurationRequest.toNewEntity(): ServiceDeactivationEntity {
        return ServiceDeactivationEntity(
            reason = data.reason,
            id = newId(),
        )
    }

    override fun CreateConfigurationRequest.toNewEntity(): ServiceConfigurationEntity {
        return ServiceConfigurationEntity(
            title = data.title,
            description = data.description,
            cost = data.cost,
            amount = data.amount,
        )
    }

    override fun ServiceConfigurationEntity.toData(): ServiceConfiguration {
        return ServiceConfiguration(
            title = title,
            description = description,
            cost = cost,
            amount = amount,
            id = id.toString(),
        )
    }

    override fun ServiceEntity.toData(): Service {
        return Service(
            id = id.toString(),
            data = ServiceData(
                info = info.toData(),
                image = with(converter) { image?.toData() },
                configurations = configurations.map { it.toData() }
            )
        )
    }

    override fun ServiceInfoEntity.toData() = ServiceInfo(
        title = title,
        description = description,
        priority = priority,
        isPublic = isPublic,
    )

}