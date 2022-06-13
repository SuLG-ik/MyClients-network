package ru.sulgik.config.processor

import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration

class MultipleProducer(
    private val producer: SingleClassProducer,
) : ClassProducer {

    override fun produce(tables: List<KSClassDeclaration>): List<KSAnnotated> {
        return tables.map { producer.produce(it) }
    }

}

interface ClassProducer {

    fun produce(tables: List<KSClassDeclaration>): List<KSAnnotated>

}

interface SingleClassProducer {

    fun produce(table: KSClassDeclaration): KSAnnotated

}