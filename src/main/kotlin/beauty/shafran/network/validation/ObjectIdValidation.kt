package beauty.shafran.network.validation

import jakarta.validation.Constraint
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import jakarta.validation.Payload
import jakarta.validation.constraintvalidation.SupportedValidationTarget
import jakarta.validation.constraintvalidation.ValidationTarget
import org.bson.types.ObjectId
import kotlin.reflect.KClass


@Constraint(validatedBy = [ObjectIdParametersValidator::class])
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class ObjectIdParameter(
    val message: String = "Illegal id",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = [],
)

@SupportedValidationTarget(ValidationTarget.PARAMETERS)
class ObjectIdParametersValidator :
    ConstraintValidator<ObjectIdParameter?, String> {

    override fun isValid(value: String, context: ConstraintValidatorContext?): Boolean {
        return ObjectId.isValid(value)
    }
}
