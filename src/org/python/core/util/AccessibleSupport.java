package org.python.core.util;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper class to set the accessible modifier without illegal access
 */
public final class AccessibleSupport {

    private static List<String> FORBIDDEN_PACKAGES;

    static {
        FORBIDDEN_PACKAGES = new ArrayList<>();
        FORBIDDEN_PACKAGES.add("java.awt.");
        FORBIDDEN_PACKAGES.add("java.io.");
        FORBIDDEN_PACKAGES.add("java.lang.");
        FORBIDDEN_PACKAGES.add("sun.");
    }

    /**
     * Get all accessible constructors of the declaring class.
     * <p>
     * If the modifier of the constructor is not public, try to {@code setAccessible(true)} - but only if allowed.
     * 
     * @param declaringClass
     *            The class for which the constructors are returned
     * 
     * @return All the accessible constructors of the declaring class
     */
    public static Constructor<?>[] getAccessibleConstructors(Class<?> declaringClass) {
        List<Constructor<?>> accessibleConstructorsList = getAccessibleConstructorsAsList(declaringClass);
        Constructor<?>[] accessibleConstructors = new Constructor<?>[accessibleConstructorsList.size()];
        return accessibleConstructorsList.toArray(accessibleConstructors);
    }

    private static List<Constructor<?>> getAccessibleConstructorsAsList(Class<?> declaringClass) {
        List<Constructor<?>> constructors = new ArrayList<>();
        Constructor<?>[] constructorCandidates = declaringClass.getDeclaredConstructors();
        for (Constructor<?> constructor : constructorCandidates) {
            if (Modifier.isPublic(constructor.getModifiers())) {
                constructors.add(constructor);
            } else {
                if (setAccessible(constructor, declaringClass)) {
                    constructors.add(constructor);
                }
            }
        }
        return constructors;
    }

    private static boolean setAccessible(AccessibleObject accessibleObject, Class<?> declaringClass) {
        boolean isAccessible = false;
        if (!belongsToForbiddenPackage(declaringClass)) {
            try {
                accessibleObject.setAccessible(true);
                isAccessible = true;
            } catch (SecurityException se) {
            }
        }
        return isAccessible;
    }

    private static boolean belongsToForbiddenPackage(Class<?> declaringClass) {
        boolean forbidden = false;
        String fullClassName = declaringClass.getName();
        for (String forbiddenPackage : FORBIDDEN_PACKAGES) {
            if (fullClassName.startsWith(forbiddenPackage)) {
                forbidden = true;
                break;
            }
        }
        return forbidden;
    }

}
