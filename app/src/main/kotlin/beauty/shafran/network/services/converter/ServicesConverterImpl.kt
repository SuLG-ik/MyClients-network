package beauty.shafran.network.services.converter

import beauty.shafran.network.assets.converter.AssetsConverter
import beauty.shafran.network.services.data.*
import beauty.shafran.network.services.enity.*


class ServicesConverterImpl(
    private val converter: AssetsConverter,
) : ServicesConverter {

    override fun EditableServiceData.toNewEntity(): ServiceInfoEntity {
        return ServiceInfoEntity(
            title = title,
            description = description,
        )
    }

    override fun CreateConfigurationRequest.toNewEntity(): ServiceConfigurationEntityData {
        return ServiceConfigurationEntityData(
            title = data.title,
            description = data.description,
            cost = data.cost,
            parameters = TypedServiceConfigurationEntity.WithAmountLimit(data.amount),
        )
    }

    override fun ServiceConfigurationEntity.toData(): ServiceConfiguration {
        return ServiceConfiguration(
            title = data.title,
            description = data.description,
            cost = data.cost,
            amount = (data.parameters as TypedServiceConfigurationEntity.WithAmountLimit).amount,
            serviceId = serviceId,
            id = id,
        )
    }

    override fun ServiceEntity.toData(): Service {
        return Service(
            id = id,
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
    )

}