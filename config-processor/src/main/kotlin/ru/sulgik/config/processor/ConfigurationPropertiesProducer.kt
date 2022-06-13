package ru.sulgik.config.processor

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.getAnnotationsByType
import com.google.devtools.ksp.getVisibility
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.symbol.*
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ksp.KotlinPoetKspPreview
import com.squareup.kotlinpoet.ksp.toClassName
import com.squareup.kotlinpoet.ksp.toKModifier
import com.squareup.kotlinpoet.ksp.writeTo
import io.ktor.server.config.*
import ru.sulgik.config.ConfigurationProperties
import ru.sulgik.config.PropertySuffix

class ConfigurationPropertiesProducer(
    private val logger: KSPLogger,
    private val codeGenerator: CodeGenerator,
) : SingleClassProducer {

    @OptIn(KotlinPoetKspPreview::class)
    override fun produce(declaration: KSClassDeclaration): KSAnnotated {
        val tableName = declaration.qualifiedName?.asString() ?: throw IllegalStateException("Table has not name")
        logger.info("Generate entity for $tableName")
        val config = declaration.getConfigDeclaration()
        val packageName = config.getPackageName()
        val fileName = config.getName() + "Converter"
        val fileSpec = FileSpec.builder(packageName, fileName)
            .addConverterFunction(config)
            .build()
        fileSpec.writeTo(codeGenerator, Dependencies(false))
        codeGenerator.associateWithClasses(listOf(declaration), packageName, fileName)
        logger.info("Entity has been generated for ${declaration.qualifiedName?.asString()}")
        return declaration
    }

    private fun FileSpec.Builder.addConverterFunction(config: ConfigDeclaration): FileSpec.Builder {
        return addFunction(
            FunSpec.builder("to${config.getName()}")
                .receiver(typeNameOf<ApplicationConfig>())
                .addConverter(config)
                .addModifiers(config.visibility.toKModifier()!!)
                .build()
        )
    }

    private fun FunSpec.Builder.addConverter(config: ConfigDeclaration): FunSpec.Builder {
        if (config.properties.isEmpty())
            return this
                .addCode("return %T()", config.typeName)
        return this
            .addCode("return %T(", config.typeName)
            .apply {
                config.properties.forEach {
                    addProperty(it)
                }
            }
            .addCode(")")
    }

    private fun FunSpec.Builder.addProperty(property: ConfigProperty): FunSpec.Builder {
        return addCode("${property.propertyName} = property(\"${property.configPath}\").${property.getConfigGetter()}, ")
    }

    private fun ConfigProperty.getConfigGetter(): String {
        return when (typeName) {
            STRING, CHAR_SEQUENCE -> "getString()"
            INT -> "getString().toInt()"
            LONG -> "getString().toLong()"
            BOOLEAN -> "getString().toBooleanStrict()"
            FLOAT -> "getString().toFloat()"
            DOUBLE -> "getString().toDouble()"
            else -> {
                logger.error("Unknown type", value)
                throw IllegalArgumentException("Unknown type")
            }
        }
    }

    private fun ConfigDeclaration.getPackageName(): String {
        return type.declaration.packageName.asString()
    }

    private fun ConfigDeclaration.getName(): String {
        return type.declaration.simpleName.asString()
    }

    @OptIn(KspExperimental::class)
    fun KSClassDeclaration.getConfigDeclaration(): ConfigDeclaration {
        val annotation = getAnnotationsByType(ConfigurationProperties::class).first()
        val preConfig = toConfigDeclaration(annotation)
        val properties = getProperties(preConfig)
        return preConfig.copy(properties = properties)
    }

    private fun KSClassDeclaration.getProperties(parent: ConfigDeclaration): List<ConfigProperty> {
        return primaryConstructor?.parameters?.mapNotNull {
            it.toConfigProperty(parent)
        }?.toList() ?: emptyList()
    }

    private fun KSValueParameter.toConfigProperty(parent: ConfigDeclaration): ConfigProperty {
        return ConfigProperty(
            type = this.type.resolve(),
            propertyName = this.name!!.asString(),
            configPath = getConfigPath(parent),
            hasDefault = hasDefault,
            value = this,
        )
    }

    private fun KSClassDeclaration.toConfigDeclaration(annotation: ConfigurationProperties): ConfigDeclaration {
        return ConfigDeclaration(
            type = asStarProjectedType(),
            configPath = getConfigPath(annotation),
            visibility = getVisibility()
        )
    }

    private fun KSClassDeclaration.getConfigPath(annotation: ConfigurationProperties): String {
        return annotation.prefix
    }

    @OptIn(KspExperimental::class)
    fun KSValueParameter.getConfigPath(parent: ConfigDeclaration): String {
        val suffix = getAnnotationsByType(PropertySuffix::class).firstOrNull()?.name ?: name?.asString()
        return "${parent.configPath}.${suffix?.ifEmpty { name!!.asString() }}"
    }

    sealed interface TypedDeclaration {
        val type: KSType
        val configPath: String
    }

    data class ConfigDeclaration(
        val visibility: Visibility,
        override val type: KSType,
        override val configPath: String,
        val properties: List<ConfigProperty> = emptyList(),
    ) : TypedDeclaration

    data class ConfigProperty(
        override val type: KSType,
        val propertyName: String,
        override val configPath: String,
        val hasDefault: Boolean,
        val value: KSValueParameter,
    ) : TypedDeclaration

    @OptIn(KotlinPoetKspPreview::class)
    val TypedDeclaration.typeName: TypeName get() = type.toClassName()

}