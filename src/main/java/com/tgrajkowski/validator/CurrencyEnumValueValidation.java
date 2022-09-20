package com.tgrajkowski.validator;

import com.tgrajkowski.constant.Constants;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = CurrencyEnumValidator.class)
public @interface CurrencyEnumValueValidation {
    Class<? extends Enum<?>> currencyEnumClass();

    String message() default Constants.NOT_OPERATED_CURRENCY;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
