package beauty.shafran.network.utils

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
class MetaEntity(
    @Contextual
    val creationDate: Date = Date(),
)