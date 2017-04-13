package com.example.blog.validation;

import com.example.blog.util.EntityUtils;
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

        String identifierName = EntityUtils.getIdentifierName(target);

        // Get property values from the specified bean
        BeanWrapper beanWrapper
                = PropertyAccessorFactory.forBeanPropertyAccess(target);
        Object identifier = beanWrapper.getPropertyValue(identifierName);
        Object property = beanWrapper.getPropertyValue(propertyName);

        // Count occurrences of unique values
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object> criteriaQuery = criteriaBuilder.createQuery();

        Root from = criteriaQuery.from(target.getClass());
        criteriaQuery.select(criteriaBuilder.count(from));
        criteriaQuery.where(
                criteriaBuilder.and(
                        criteriaBuilder.equal(from.get(propertyName), property),
                        identifier == null
                                ? criteriaBuilder.isNotNull(from.get(identifierName))
                                : criteriaBuilder.notEqual(from.get(identifierName), identifier)
                )
        );

        Query query = entityManager.createQuery(criteriaQuery);
        Long count = (Long) query.getSingleResult();
        if(count > 0) {
            buildConstraintViolation(context);
            return false;
        }

        return true;
    }

    private void buildConstraintViolation(ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();
        context
                .buildConstraintViolationWithTemplate(propertyName + " must be unique")
                .addPropertyNode(propertyName)
                .addConstraintViolation();
    }
}
