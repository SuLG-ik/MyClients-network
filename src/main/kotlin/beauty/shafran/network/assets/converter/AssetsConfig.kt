package beauty.shafran.network.assets.converter

data class AssetsConfig(
    val url: String = System.getenv("PUBLIC_ASSETS_URL"),
)
