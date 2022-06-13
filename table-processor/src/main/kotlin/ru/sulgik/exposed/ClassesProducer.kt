package ru.sulgik.exposed

import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration

interface TablesProducer {

    fun produce(tables: List<KSClassDeclaration>): List<KSAnnotated>

}

interface SingleTableProducer {

    fun produce(table: KSClassDeclaration): KSAnnotated

}