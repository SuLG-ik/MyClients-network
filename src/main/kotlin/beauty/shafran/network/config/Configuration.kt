package beauty.shafran.network.config

import beauty.shafran.network.assets.converter.AssetsConfig

interface Configuration {

    fun buildMongoClientConfig(): MongoClientConfig

    fun buildAssetsConfig(): AssetsConfig

    fun buildSecureConfig(): SecureConfiguration

}