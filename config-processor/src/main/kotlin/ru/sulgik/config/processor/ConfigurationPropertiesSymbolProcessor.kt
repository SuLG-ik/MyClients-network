package ru.sulgik.config.processor

import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.KSAnnotated

class ConfigurationPropertiesSymbolProcessorProvider : SymbolProcessorProvider {

    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return ConfigurationPropertiesSymbolProcessor(environment.codeGenerator, environment.logger)
    }


}

class ConfigurationPropertiesSymbolProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger,
) : SymbolProcessor {

    var invoked = false

    private val filter = NamedAnnotationFilter(
        annotationName = "ru.sulgik.config.ConfigurationProperties",
        logger = logger,
    )
    private val producer: ClassProducer = MultipleProducer(
        ConfigurationPropertiesProducer(
            logger = logger, codeGenerator = codeGenerator
        )
    )

    override fun process(resolver: Resolver): List<KSAnnotated> {
        if (invoked)
            return emptyList()
        logger.info("Start processing")
        val annotations =
            producer.produce(filter.filter(resolver))
        logger.info("End processing")
        invoked = true
        return annotations
    }


}