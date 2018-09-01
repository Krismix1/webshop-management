package dk.cristi.app.webshop.management.validators;

import org.apache.commons.lang.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Validator to allow a field to either not be set or not be empty.
 */
public class IsNullOrIsNotBlankValidator implements ConstraintValidator<IsNullOrIsNotBlankConstraint, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value == null || StringUtils.isNotBlank(value);
    }
}
