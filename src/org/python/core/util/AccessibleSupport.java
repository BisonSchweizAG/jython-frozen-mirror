package org.python.core.util;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper class to set the accessible modifier without illegal access
 */
public final class AccessibleSupport {

    /**
     * Setting this system property to {@code true} prevents blocking of illegal method calls.
     * <p>
     * The default is {@code false}.
     * <p>
     * This might be useful if the internal jdk modules are opened with {@code --add-opens}, and scripts like
     * {@code socket.py} or {@code select.py} operate on {@code sun.nio.*} classes directly.
     */
    public static final String ALLOW_ILLEGAL_METHOD_CALLS = "python.allow.illegal.method.calls";

    // Non-final to enable testing
    private static boolean ALLOW_ANY_METHOD_CALLS = Boolean
                    .valueOf(System.getProperty(ALLOW_ILLEGAL_METHOD_CALLS, "false"));

    /**
     * The forbidden packages for setAccessible(true)
     */
    private static List<String> FORBIDDEN_PACKAGES;

    static {
        FORBIDDEN_PACKAGES = new ArrayList<String>();
        FORBIDDEN_PACKAGES.add("java.awt.");
        FORBIDDEN_PACKAGES.add("java.io.");
        FORBIDDEN_PACKAGES.add("java.lang.");
        FORBIDDEN_PACKAGES.add("sun.");
    }

    /**
     * The forbidden packages for reflective method calls
     */
    private static List<String> FORBIDDEN_PACKAGES_FOR_CALLS;

    static {
        FORBIDDEN_PACKAGES_FOR_CALLS = new ArrayList<String>();
        FORBIDDEN_PACKAGES_FOR_CALLS.add("sun.nio.");
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

    /**
     * Get all accessible fields of the declaring class.
     * <p>
     * If the modifier of the field is not public, try to {@code setAccessible(true)} - but only if allowed.
     * 
     * @param declaringClass
     *            The class for which the fields are returned
     * 
     * @return All the accessible fields of the declaring class
     */
    public static Field[] getAccessibleFields(Class<?> declaringClass) {
        List<Field> accessibleFieldsList = getAccessibleFieldsAsList(declaringClass);
        Field[] accessibleFields = new Field[accessibleFieldsList.size()];
        return accessibleFieldsList.toArray(accessibleFields);
    }

    /**
     * Get all accessible methods on the declaring class and all its super classes.
     * <p>
     * If the modifier of the method is not public, try to {@code setAccessible(true)} - but only if allowed.
     * 
     * @param declaringClass
     *            The class for which the methods are returned
     * 
     * @return All the accessible methods of the declaring class and its super classes
     */
    public static List<Method> getAccessibleMethodsInHierarchy(Class<?> declaringClass) {
        List<Method> accessibleMethods = new ArrayList<Method>();
        for (Class<?> currentClass = declaringClass; currentClass != null; currentClass = currentClass
                        .getSuperclass()) {
            for (Method method : currentClass.getDeclaredMethods()) {
                if (Modifier.isPublic(method.getModifiers())) {
                    accessibleMethods.add(method);
                } else {
                    if (setAccessible(method, currentClass)) {
                        accessibleMethods.add(method);
                    }
                }
            }
        }
        return accessibleMethods;
    }

    /**
     * Try to {@code setAccessible(true)} on a single method <b>regardless</b> of its modifier - but only if allowed.
     * 
     * @param method
     *            The method to be made accessible
     */
    public static void forceSetAccessibleOnSingleMethod(Method method) {
        setAccessible(method, method.getDeclaringClass());
    }

    /**
     * Invoke a method, if allowed.
     * <p>
     * Throws a {@code Py.JavaError} if the invocation failed.
     * 
     * @param method
     *            The method to be invoked
     * @param cself
     *            The object to invoke the method on
     * @param argsArray
     *            The parameters to the method
     * 
     * @return The result of the call
     * 
     * @throws IllegalAccessException
     *             If the call to the method is blocked
     * @throws InvocationTargetException
     *             If the call does not succeed
     * @throws IllegalArgumentException
     *             If there are wrong arguments to the method
     */
    public static Object invokeMethod(Method method, Object cself, Object[] argsArray)
                    throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Class<?> declaringClass = method.getDeclaringClass();
        if (blocksCallsToForbiddenPackage(declaringClass)) {
            String message = String.format("Call to method '%s' of class '%s' would lead to an illegal access.",
                            method.getName(), declaringClass.getName());
            throw new IllegalAccessException(message);
        } else {
            return method.invoke(cself, argsArray);
        }
    }

    private static List<Constructor<?>> getAccessibleConstructorsAsList(Class<?> declaringClass) {
        List<Constructor<?>> constructors = new ArrayList<Constructor<?>>();
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

    private static List<Field> getAccessibleFieldsAsList(Class<?> declaringClass) {
        List<Field> fields = new ArrayList<Field>();
        Field[] fieldCandidates = declaringClass.getDeclaredFields();
        for (Field field : fieldCandidates) {
            if (Modifier.isPublic(field.getModifiers())) {
                fields.add(field);
            } else {
                if (setAccessible(field, declaringClass)) {
                    fields.add(field);
                }
            }
        }
        return fields;
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
        String fullClassName = declaringClass.getName();
        for (String forbiddenPackage : FORBIDDEN_PACKAGES) {
            if (fullClassName.startsWith(forbiddenPackage)) {
                return true;
            }
        }
        return false;
    }

    private static boolean blocksCallsToForbiddenPackage(Class<?> declaringClass) {
        if (!ALLOW_ANY_METHOD_CALLS) {
            String fullClassName = declaringClass.getName();
            for (String forbiddenPackageForCalls : FORBIDDEN_PACKAGES_FOR_CALLS) {
                if (fullClassName.startsWith(forbiddenPackageForCalls)) {
                    return true;
                }
            }
        }
        return false;
    }

}
