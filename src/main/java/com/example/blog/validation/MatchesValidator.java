package com.example.blog.validation;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class MatchesValidator implements ConstraintValidator<Matches, Object> {

    private String propertyName;
    private String verifierName;

    @Override
    public void initialize(Matches annotation) {
        propertyName = annotation.property();
        verifierName = annotation.verifier();
    }

    @Override
    public boolean isValid(Object target, ConstraintValidatorContext context) {
        if(target == null) {
            return true;
        }

        BeanWrapper beanWrapper
                = PropertyAccessorFactory.forBeanPropertyAccess(target);
        Object property = beanWrapper.getPropertyValue(propertyName);
        Object verifier = beanWrapper.getPropertyValue(verifierName);

        if(property == null || !property.equals(verifier)) {
            buildConstraintValidation(context);
            return false;
        }

        return true;
    }

    private void buildConstraintValidation(ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();
        context
                .buildConstraintViolationWithTemplate(propertyName + " does not match " + verifierName)
                .addPropertyNode(propertyName)
                .addConstraintViolation();
    }
}
