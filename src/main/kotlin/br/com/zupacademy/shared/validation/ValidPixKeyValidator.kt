package br.com.zupacademy.shared.validation

import br.com.zupacademy.registra.request.NovaChavePixRequest
import io.micronaut.core.annotation.AnnotationValue
import io.micronaut.validation.validator.constraints.ConstraintValidator
import io.micronaut.validation.validator.constraints.ConstraintValidatorContext
import org.slf4j.LoggerFactory
import javax.inject.Singleton

@Singleton
class ValidPixKeyValidator : ConstraintValidator<ValidPixKey, NovaChavePixRequest> {

    private val logger = LoggerFactory.getLogger(ValidPixKeyValidator::class.java)

    override fun isValid(
        value: NovaChavePixRequest?,
        annotationMetadata: AnnotationValue<ValidPixKey>,
        context: ConstraintValidatorContext
    ): Boolean {
        logger.info("Inicio da validação")

        if (value?.tipoDeChave == null) {
            return false
        }
        return value.tipoDeChave.valida(value.chave)
    }
}