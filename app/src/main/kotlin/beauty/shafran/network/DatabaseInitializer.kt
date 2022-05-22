package beauty.shafran.network

import org.jetbrains.exposed.sql.Database

interface DatabaseInitializer {

    fun initialize(configuration: DatabaseConfiguration): Database

}