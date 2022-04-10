package beauty.shafran.network.validation

import org.bson.types.ObjectId
import javax.validation.Constraint
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext
import javax.validation.Payload
import javax.validation.constraintvalidation.SupportedValidationTarget
import javax.validation.constraintvalidation.ValidationTarget
import kotlin.reflect.KClass


@Constraint(validatedBy = [ObjectIdParametersValidator::class])
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class ObjectIdParameter(
    val message: String = "Illegal id",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = [],
)

@SupportedValidationTarget(ValidationTarget.ANNOTATED_ELEMENT)
class ObjectIdParametersValidator :
    ConstraintValidator<ObjectIdParameter?, String> {

    override fun isValid(value: String, context: ConstraintValidatorContext?): Boolean {
        return ObjectId.isValid(value)
    }
}
