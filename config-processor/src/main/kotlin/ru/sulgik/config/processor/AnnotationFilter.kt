package ru.sulgik.config.processor

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSClassDeclaration

interface AnnotationFilter {

    fun filter(resolver: Resolver): List<KSClassDeclaration>

}