package ru.sulgik.exposed

import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.KSAnnotated

class ExposedTableSymbolProcessorProvider : SymbolProcessorProvider {

    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return ExposedTableSymbolProcessor(
            codeGenerator = environment.codeGenerator,
            logger = environment.logger,
            options = environment.options,
        )
    }


}

class ExposedTableSymbolProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger,
    private val options: Map<String, String>,
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
        packageName = options.getOrDefault("tables_package", "ru.sulgik.exposed.generated"),
        fileName = "GeneratedTableSchemaUtils"
    )

    override fun process(resolver: Resolver): List<KSAnnotated> {
        if (invoked)
            return emptyList()
        logger.info("Start processing")
        val annotations = mutableListOf<KSAnnotated>()
        if (options.getOrDefault("generate_creation_utils", "true").toBooleanStrict())
            annotations.addAll(toCreationProducer.produce(toCreationFilter.filter(resolver)))
        if (options.getOrDefault("generate_entities", "true").toBooleanStrict())
            annotations.addAll(generateEntityProducer.produce(generateEntityFilter.filter(resolver)))
        logger.info("End processing")
        invoked = true
        return annotations
    }


}