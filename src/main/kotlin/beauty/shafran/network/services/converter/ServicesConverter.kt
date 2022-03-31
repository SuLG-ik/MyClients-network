package beauty.shafran.network.services.converter

import beauty.shafran.network.services.data.*
import beauty.shafran.network.services.enity.ServiceConfigurationEntity
import beauty.shafran.network.services.enity.ServiceDeactivationEntity
import beauty.shafran.network.services.enity.ServiceEntity
import beauty.shafran.network.services.enity.ServiceInfoEntity

interface ServicesConverter {


    fun CreateConfigurationRequest.toNewEntity(): ServiceConfigurationEntity
    fun DeactivateServiceConfigurationRequest.toNewEntity(): ServiceDeactivationEntity

    fun ServiceInfoEntity.toData(): ServiceInfo
    fun ServiceEntity.toData(): Service
    fun ServiceConfigurationEntity.toData(): ServiceConfiguration


}