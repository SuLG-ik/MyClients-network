package beauty.shafran.network.assets.converter

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding


@ConfigurationProperties("features.assets")
@ConstructorBinding
data class AssetsConfig(
    val url: String,
)
