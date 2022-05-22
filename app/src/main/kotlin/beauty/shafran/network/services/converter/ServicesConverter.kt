package beauty.shafran.network.services.converter

import beauty.shafran.network.services.data.*
import beauty.shafran.network.services.enity.ServiceConfigurationEntity
import beauty.shafran.network.services.enity.ServiceConfigurationEntityData
import beauty.shafran.network.services.enity.ServiceEntity
import beauty.shafran.network.services.enity.ServiceInfoEntity

interface ServicesConverter {
    fun CreateConfigurationRequest.toNewEntity(): ServiceConfigurationEntityData
    fun EditableServiceData.toNewEntity(): ServiceInfoEntity
    fun ServiceInfoEntity.toData(): ServiceInfo
    fun ServiceEntity.toData(): Service
    fun ServiceConfigurationEntity.toData(): ServiceConfiguration
}