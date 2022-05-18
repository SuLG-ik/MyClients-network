package ru.sulgik.exposed

import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSDeclaration

class NamedObjectTableAnnotationFilter(
    private val annotationName: String,
    private val logger: KSPLogger,
) : AnnotationFilter {

    override fun filter(resolver: Resolver): List<KSClassDeclaration> {
        return resolver.getSymbolsWithAnnotation(annotationName)
            .filterIsInstance<KSClassDeclaration>().filter {
                if (it.classKind != ClassKind.OBJECT)
                    logger.error("Only objects is able to be annotated as TableToCreation", it)
                if ("org.jetbrains.exposed.sql.Table" !in it.flatSuperTypes())
                    logger.error("Only exposed Table() is able to be annotated as TableToCreation", it)
                true
            }.toList()
    }

    private fun KSDeclaration.flatSuperTypes(): List<Any?> {
        if (this is KSClassDeclaration) {
            return superTypes.flatMap {
                val declaration = it.resolve().declaration
                val name = declaration.qualifiedName?.asString()
                if (name.isNullOrEmpty()) {
                    declaration.flatSuperTypes()
                } else {
                    declaration.flatSuperTypes() + name
                }
            }.distinct().toList()
        }
        return emptyList()
    }

}