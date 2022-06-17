package ru.sulgik.exposed

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ksp.KotlinPoetKspPreview
import com.squareup.kotlinpoet.ksp.writeTo

class ToCreationProducer(
    private val logger: KSPLogger,
    private val codeGenerator: CodeGenerator,
    private val packageName: String,
    private val fileName: String,
) : TablesProducer {

    @OptIn(KotlinPoetKspPreview::class)
    override fun produce(tables: List<KSClassDeclaration>): List<KSAnnotated> {
        val file = FileSpec.builder(packageName, fileName)
            .addFunction(
                FunSpec.builder("createAnnotatedMissingTablesAndColumns")
                    .receiver(ClassName("org.jetbrains.exposed.sql", "SchemaUtils"))
                    .addParameter(
                        ParameterSpec.builder("inBatch", typeNameOf<Boolean>())
                            .defaultValue("false")
                            .build()
                    )
                    .addModifiers(KModifier.INTERNAL)
                    .addParameter(
                        ParameterSpec.builder("withLogs", typeNameOf<Boolean>())
                            .defaultValue("true")
                            .build()
                    )
                    .apply {
                        if (tables.isNotEmpty())
                            addCode("""
                        |createMissingTablesAndColumns(
                        |    ${
                                tables.joinToString(",\n    ") {
                                    it.qualifiedName?.asString() ?: throw IllegalArgumentException("Table name cant be null")
                                }
                            },
                        |    inBatch = inBatch,
                        |    withLogs = withLogs,
                        |)
                    """.trimMargin())
                    }
                    .build()
            )
            .addFunction(
                FunSpec.builder("createAnnotatedTablesAndColumns")
                    .receiver(ClassName("org.jetbrains.exposed.sql", "SchemaUtils"))
                    .addParameter(
                        ParameterSpec.builder("inBatch", typeNameOf<Boolean>())
                            .defaultValue("false")
                            .build()
                    )
                    .addModifiers(KModifier.INTERNAL)
                    .apply {
                        if (tables.isNotEmpty())
                            addCode("""
                        |create(
                        |    ${
                                tables.joinToString(",\n    ") {
                                    it.qualifiedName?.asString() ?: throw IllegalArgumentException("Table name cant be null")
                                }
                            },
                        |    inBatch = inBatch,
                        |)
                    """.trimMargin())
                    }
                    .build()
            )
            .build()
        file.writeTo(codeGenerator, Dependencies(true, *tables.mapNotNull { it.containingFile }.toTypedArray()))
        return tables
    }

    private fun FunSpec.Builder.addTypes(types: List<KSClassDeclaration>): FunSpec.Builder {
        return this.apply {
            types.forEach {
                addCode("${it.qualifiedName},")
            }
        }
    }

}