package ru.sulgik.exposed

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.getAnnotationsByType
import com.google.devtools.ksp.isAnnotationPresent
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ksp.KotlinPoetKspPreview
import com.squareup.kotlinpoet.ksp.toClassName
import com.squareup.kotlinpoet.ksp.toTypeName
import com.squareup.kotlinpoet.ksp.writeTo
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.statements.InsertStatement

class TablesToEntityCreationProducer(
    private val logger: KSPLogger,
    private val codeGenerator: CodeGenerator,
) : TablesProducer {

    private val producer = SingleTableToEntityProducer(
        logger = logger, codeGenerator = codeGenerator
    )

    override fun produce(tables: List<KSClassDeclaration>): List<KSAnnotated> {
        return tables.map { producer.produce(it) }
    }


}

private class SingleTableToEntityProducer(
    private val logger: KSPLogger,
    private val codeGenerator: CodeGenerator,
) : SingleTableProducer {

    @OptIn(KspExperimental::class, KotlinPoetKspPreview::class)
    override fun produce(table: KSClassDeclaration): KSAnnotated {
        val tableName = table.qualifiedName?.asString() ?: throw IllegalStateException("Table has not name")
        logger.info("Generate entity for $tableName")
        val annotation = table.getAnnotationsByType(TableWithEntity::class).first()
        val fileName = table.simpleName.asString().toEntityName()
        val properties = table.getAllEntityProperties()
        val packageName = table.packageName.asString()
        val typeSpec =
            generateEntity(entityName = fileName, annotation = annotation, properties = properties)
        val fileSpec = FileSpec.builder(packageName, fileName)
            .addImport("org.jetbrains.exposed.sql.SqlExpressionBuilder", "eq")
            .addImport("org.jetbrains.exposed.sql", "insert")
            .addImport("org.jetbrains.exposed.sql", "select")
            .addImport("org.jetbrains.exposed.sql", "insertAndGetId")
            .addType(typeSpec)
            .addFunction(
                generateRowToEntity(
                    fileName,
                    ClassName(packageName, fileName),
                    properties,
                    table.toClassName()
                )
            )
            .addFunction(
                generateInsert(
                    properties.filterIgnoreOnInsert(),
                    table
                )
            )
            .addFunction(generateInsertAndGetId(properties.filterIgnoreOnInsert(), table))
            .addFunction(generateSelectById(fileName, ClassName(packageName, fileName), table))
            .addFunction(
                generateInsertToEntity(
                    fileName,
                    ClassName(packageName, fileName),
                    properties,
                    table.toClassName()
                )
            )
            .addFunction(
                generateInsertAndGet(
                    fileName,
                    ClassName(packageName, fileName),
                    properties.filterIgnoreOnInsert(),
                    table
                )
            )
            .build()
        fileSpec.writeTo(codeGenerator, Dependencies(true, table.containingFile!!))

        logger.info("Entity has been generated for ${table.qualifiedName?.asString()}")
        return table
    }


    @OptIn(KotlinPoetKspPreview::class)
    private fun generateSelectById(
        typeName: String,
        type: TypeName,
        table: KSClassDeclaration,
    ): FunSpec {
        return FunSpec.builder("findById")
            .receiver(table.toClassName())
            .addParameter("id", table.getTableIdType().toClassName())
            .returns(type.copy(nullable = true))
            .addCode(
                "return %T.select{ %T.${table.getTableIdProperty()}.eq(id) }",
                table.toClassName(),
                table.toClassName(),
            )
            .addCode(".firstOrNull()")
            .addCode("?.to$typeName()")
            .build()
    }

    private fun String.toEntityName(): String {
        return removeSuffix("Table") + "Entity"
    }

    private fun generateInsertToEntity(
        typeName: String,
        type: TypeName,
        properties: List<EntityProperty>,
        table: TypeName,
    ): FunSpec {
        return FunSpec.builder("to$typeName")
            .receiver(typeNameOf<InsertStatement<Number>>().copy())
            .returns(type)
            .addCode("return %T(", type)
            .addPropertiesToEntityBuilder(properties, table)
            .addCode(")")
            .build()
    }

    private fun generateRowToEntity(
        typeName: String,
        type: TypeName,
        properties: List<EntityProperty>,
        table: TypeName,
    ): FunSpec {
        return FunSpec.builder("to$typeName")
            .receiver(typeNameOf<ResultRow>())
            .returns(type)
            .addCode("return %T(", type)
            .addPropertiesToEntityBuilder(properties, table)
            .addCode(")")
            .build()
    }

    private fun FunSpec.Builder.addPropertiesToEntityBuilder(
        properties: List<EntityProperty>,
        table: TypeName,
    ): FunSpec.Builder {
        return this.apply {
            properties.forEach {
                val valueCall = if (it.tablePropertyType.isEntityId()) ".value" else ""
                addCode("${it.propertyName} = get(%T.${it.tablePropertyName})${valueCall},", table)
            }
        }
    }


    private fun generateEntity(
        entityName: String,
        annotation: TableWithEntity,
        properties: List<EntityProperty>,
    ): TypeSpec {
        return TypeSpec.classBuilder(entityName)
            .addModifiers(KModifier.DATA)
            .primaryConstructor(
                FunSpec.constructorBuilder()
                    .addTablesParameter(properties)
                    .build()
            )
            .addTablesParameter(properties)
            .build()
    }

    @OptIn(KspExperimental::class)
    private fun KSClassDeclaration.getAllEntityProperties(): List<EntityProperty> {
        return getAllProperties().filter { it.isAnnotationPresent(PropertyOfEntity::class) }
            .map {
                it.toEntityProperty()
            }.toList()
    }

    @OptIn(KotlinPoetKspPreview::class)
    private fun generateInsertAndGet(
        typeName: String,
        type: TypeName,
        properties: List<EntityProperty>,
        table: KSClassDeclaration,
    ): FunSpec {
        return FunSpec.builder("insertAndGetEntity")
            .returns(type)
            .receiver(table.toClassName())
            .addInsertParameters(properties)
            .beginControlFlow("return %T.insert", table.toClassName())
            .addPropertiesToInsertBuilder(properties, table)
            .endControlFlow()
            .addCode(".to${typeName}()")
            .build()
    }


    @OptIn(KotlinPoetKspPreview::class)
    private fun generateInsert(
        properties: List<EntityProperty>,
        table: KSClassDeclaration,
    ): FunSpec {
        return FunSpec.builder("insertEntity")
            .receiver(table.toClassName())
            .addInsertParameters(properties)
            .beginControlFlow("return %T.insert", table.toClassName())
            .addPropertiesToInsertBuilder(properties, table)
            .endControlFlow()
            .build()
    }

    @OptIn(KspExperimental::class)
    private fun KSClassDeclaration.getTableIdProperty(): KSPropertyDeclaration {
        return getAllProperties().filter { it.isAnnotationPresent(TableId::class) }.toList().also {
            if (it.size != 1)
                throw IllegalStateException("Table can contain only one @Id")
        }.first()
    }

    @OptIn(KspExperimental::class)
    private fun KSClassDeclaration.getTableIdType(): KSType {
        return getAllProperties().filter { it.isAnnotationPresent(TableId::class) }.toList().also {
            if (it.size != 1)
                throw IllegalStateException("Table can contain only one @TableId")
        }.first().type.resolve().getGeneric()
    }

    @OptIn(KotlinPoetKspPreview::class)
    private fun generateInsertAndGetId(
        properties: List<EntityProperty>,
        table: KSClassDeclaration,
    ): FunSpec {
        return FunSpec.builder("insertEntityAndGetId")
            .receiver(table.toClassName())
            .returns(table.getTableIdType().toClassName())
            .addInsertParameters(properties)
            .beginControlFlow("return %T.insertAndGetId", table.toClassName())
            .addPropertiesToInsertBuilder(properties, table)
            .endControlFlow()
            .addCode(".value")
            .build()
    }

    @OptIn(KotlinPoetKspPreview::class, KspExperimental::class)
    private fun FunSpec.Builder.addPropertiesToInsertBuilder(
        properties: List<EntityProperty>,
        table: KSClassDeclaration,
    ): FunSpec.Builder {
        return this
            .apply {
                properties.forEach {
                    val isWithDefault = it.declaration.isAnnotationPresent(DefaultOnInsert::class)
                    if (isWithDefault)
                        beginControlFlow("if (${it.propertyName} != null)")
                    addStatement("it[%T.${it.tablePropertyName}] = ${it.propertyName}", table.toClassName())
                    if (isWithDefault)
                        endControlFlow()
                }
            }
    }

    @OptIn(KotlinPoetKspPreview::class, KspExperimental::class)
    private fun FunSpec.Builder.addInsertParameters(
        properties: List<EntityProperty>,
    ): FunSpec.Builder {
        return this.apply {
            addParameters(
                properties.map {
                    val isWithDefault = it.declaration.isAnnotationPresent(DefaultOnInsert::class)
                    ParameterSpec
                        .builder(it.propertyName, it.propertyType.toClassName().copy(isWithDefault))
                        .apply {
                            if (isWithDefault) {
                                defaultValue("null")
                            }
                        }
                        .build()
                }
            )
        }
    }

    @OptIn(KspExperimental::class)
    private fun List<EntityProperty>.filterIgnoreOnInsert(): List<EntityProperty> {
        return filter { !it.declaration.isAnnotationPresent(IgnoreOnInsert::class) }
    }

    @OptIn(KotlinPoetKspPreview::class)
    private fun FunSpec.Builder.addTablesParameter(
        properties: List<EntityProperty>,
    ): FunSpec.Builder {
        return this.apply {
            addParameters(properties.map { ParameterSpec(it.propertyName, it.propertyType.toTypeName()) })
        }
    }

    @OptIn(KotlinPoetKspPreview::class)
    private fun TypeSpec.Builder.addTablesParameter(
        properties: List<EntityProperty>,
    ): TypeSpec.Builder {
        return this.apply {
            addProperties(properties.map {
                PropertySpec
                    .builder(it.propertyName, it.propertyType.toTypeName())
                    .initializer(it.propertyName)
                    .build()
            })
        }
    }

    private fun KSPropertyDeclaration.toEntityProperty(): EntityProperty {
        return EntityProperty(
            declaration = this,
            tablePropertyName = simpleName.asString(),
            tablePropertyType = type.resolve().getFirstGeneric(),
            propertyName = simpleName.asString(),
            propertyType = type.resolve().getGeneric()
        )
    }

    private fun KSType.getFirstGeneric(): KSType {
        return arguments.first().type?.resolve() ?: throw IllegalStateException("Type does not have generic")
    }

    private fun KSType.isEntityId(): Boolean {
        return declaration.qualifiedName?.asString()?.startsWith("org.jetbrains.exposed.dao.id.EntityID") == true
    }

    private fun KSType.getGeneric(): KSType {
        val generic = getFirstGeneric()
        if (generic.isEntityId()) {
            return generic.getGeneric()
        }
        return generic
    }


    private data class EntityProperty(
        val declaration: KSPropertyDeclaration,
        val tablePropertyName: String,
        val tablePropertyType: KSType,
        val propertyName: String,
        val propertyType: KSType,
    )

}