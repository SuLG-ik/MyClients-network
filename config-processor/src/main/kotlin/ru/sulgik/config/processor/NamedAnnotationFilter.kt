package ru.sulgik.config.processor

import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSClassDeclaration

class NamedAnnotationFilter(
    private val annotationName: String,
    private val logger: KSPLogger,
) : AnnotationFilter {

    override fun filter(resolver: Resolver): List<KSClassDeclaration> {
        logger.info("Filtering $annotationName annotations")
        return resolver.getSymbolsWithAnnotation(annotationName)
            .filterIsInstance<KSClassDeclaration>().toList()
    }


}