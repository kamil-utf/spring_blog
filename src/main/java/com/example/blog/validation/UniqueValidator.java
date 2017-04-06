package com.example.blog.validation;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqueValidator implements ConstraintValidator<Unique, Object> {

    @PersistenceContext
    private EntityManager entityManager;

    private String propertyName;

    @Override
    public void initialize(Unique annotation) {
        propertyName = annotation.property();
    }

    @Override
    public boolean isValid(Object target, ConstraintValidatorContext context) {
        if(target == null) {
            return true;
        }

        BeanWrapper beanWrapper
                = PropertyAccessorFactory.forBeanPropertyAccess(target);
        Object property = beanWrapper.getPropertyValue(propertyName);
        Long count = countOccurrences(target, propertyName, property);
        if(count > 0) {
            buildConstraintViolation(context);
            return false;
        }

        return true;
    }

    private Long countOccurrences(Object target, String propertyName, Object property) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object> criteriaQuery = criteriaBuilder.createQuery();

        Root from = criteriaQuery.from(target.getClass());
        criteriaQuery.select(criteriaBuilder.count(from));
        criteriaQuery.where(criteriaBuilder.equal(from.get(propertyName), property));

        // Execute query
        Query query = entityManager.createQuery(criteriaQuery);
        return (Long) query.getSingleResult();
    }

    private void buildConstraintViolation(ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();
        context
                .buildConstraintViolationWithTemplate(propertyName + " must be unique")
                .addPropertyNode(propertyName)
                .addConstraintViolation();
    }
}
