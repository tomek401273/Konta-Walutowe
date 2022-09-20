package com.tgrajkowski.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CurrencyEnumValidator implements ConstraintValidator<CurrencyEnumValueValidation, CharSequence> {
    private List<String> acceptableValues;


    @Override
    public void initialize(CurrencyEnumValueValidation constraintAnnotation) {
        acceptableValues = Stream.of(constraintAnnotation.currencyEnumClass().getEnumConstants())
                .map(Enum::name)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return acceptableValues.contains(value.toString());
    }
}
