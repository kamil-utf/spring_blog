package com.example.blog.util;

import javax.persistence.Id;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public class EntityUtils {

    public static String getIdentifierName(Object object) {
        Field[] fields = object.getClass().getDeclaredFields();
        for(Field field : fields) {
            Annotation[] annotations = field.getDeclaredAnnotations();
            for(Annotation annotation : annotations) {
                if(annotation instanceof Id)
                    return field.getName();
            }
        }

        return null;    // Identifier not found
    }
}
