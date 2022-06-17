package beauty.shafran.network.services.route

import beauty.shafran.network.database.Transactional
import beauty.shafran.network.services.converter.ServiceConverter
import beauty.shafran.network.services.repository.ServiceRepository

internal class ServicesRouterImpl(
    private val transactional: Transactional,
    private val converter: ServiceConverter,
    private val serviceRepository: ServiceRepository,
) : ServicesRouter {

}