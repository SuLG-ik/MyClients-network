package beauty.shafran.network.config

import beauty.shafran.network.assets.converter.AssetsConfig

class PreferencesConfiguration : Configuration {
    override fun buildMongoClientConfig(): MongoClientConfig {
        return MongoClientConfig(
            url = System.getenv("DATABASE_URL"),
            username = System.getenv("DATABASE_USERNAME"),
            password = System.getenv("DATABASE_PASSWORD"),
            authDatabase = System.getenv("DATABASE_AUTH_NAME"),
            database = System.getenv("DATABASE_NAME")
        )
    }

    override fun buildAssetsConfig(): AssetsConfig {
        return AssetsConfig(
            "ПОСОСИ"
        )
    }

    override fun buildSecureConfig(): SecureConfiguration {
        return SecureConfiguration(
            apiKey = System.getenv("api_key"),
            keyName = "apiKey",
        )
    }
}