package ru.sulgik.exposed

import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.KSAnnotated

class ExposedTableSymbolProcessorProvider : SymbolProcessorProvider {

    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return ExposedTableSymbolProcessor(environment.codeGenerator, environment.logger)
    }


}

class ExposedTableSymbolProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger,
) : SymbolProcessor {

    var invoked = false

    private val generateEntityFilter = NamedObjectTableAnnotationFilter(
        annotationName = "ru.sulgik.exposed.TableWithEntity",
        logger = logger,
    )
    private val generateEntityProducer: TablesProducer = TablesToEntityCreationProducer(
        logger = logger, codeGenerator = codeGenerator
    )

    private val toCreationFilter = NamedObjectTableAnnotationFilter(
        annotationName = "ru.sulgik.exposed.TableToCreation",
        logger = logger,
    )
    private val toCreationProducer: TablesProducer = ToCreationProducer(
        logger = logger,
        codeGenerator = codeGenerator,
        packageName = "ru.sulgik.exposed.generated",
        fileName = "GeneratedTableSchemaUtils"
    )

    override fun process(resolver: Resolver): List<KSAnnotated> {
        if (invoked)
            return emptyList()
        logger.info("Start processing")
        val annotations = toCreationProducer.produce(toCreationFilter.filter(resolver)) + generateEntityProducer.produce(generateEntityFilter.filter(resolver))
        logger.info("End processing")
        invoked = true
        return annotations
    }


}