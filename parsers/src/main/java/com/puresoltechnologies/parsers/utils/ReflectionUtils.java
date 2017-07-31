package com.puresoltechnologies.parsers.utils;

import java.lang.reflect.Field;

/**
 * Simple class to provide utilities for reflection.
 * 
 * @author Rick-Rainer Ludwig
 */
public class ReflectionUtils {

    /**
     * Private constructor to avoid instantiation.
     */
    private ReflectionUtils() {
    }

    public static <O, F> void setField(O object, String field, F value)
	    throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
	Field declaredField = object.getClass().getDeclaredField(field);
	declaredField.setAccessible(true);
	declaredField.set(object, value);
    }

}
