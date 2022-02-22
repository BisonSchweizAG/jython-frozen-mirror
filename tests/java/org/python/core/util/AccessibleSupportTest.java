package org.python.core.util;

import java.awt.geom.Ellipse2D;
import java.awt.geom.RectangularShape;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.TestCase;

public class AccessibleSupportTest extends TestCase {

    public void testConstructor_Ellipse2D() {
        assertEquals(0, AccessibleSupport.getAccessibleConstructors(Ellipse2D.class).length);
    }

    public void testConstructor_RectangularShape() {
        assertEquals(0, AccessibleSupport.getAccessibleConstructors(RectangularShape.class).length);
    }

    public void testConstructor_Enum() {
        assertEquals(0, AccessibleSupport.getAccessibleConstructors(Enum.class).length);
    }

    public void testConstructor_Throwable() {
        Constructor<?>[] constructors = AccessibleSupport.getAccessibleConstructors(Throwable.class);
        assertEquals(4, constructors.length);
        for (Constructor<?> constructor : constructors) {
            assertTrue(Modifier.isPublic(constructor.getModifiers()));
        }
    }

    public void testConstructor_Error() {
        Constructor<?>[] constructors = AccessibleSupport.getAccessibleConstructors(Error.class);
        assertEquals(4, constructors.length);
        for (Constructor<?> constructor : constructors) {
            assertTrue(Modifier.isPublic(constructor.getModifiers()));
        }
    }

    public void testForceSetAccessibleOnSingleMethod_EllipseIterator() throws ClassNotFoundException {
        // the class itself is not visible, make sure that no setAccessible(true) happens to the methods
        Class<?> declaringClass = Class.forName("java.awt.geom.EllipseIterator");
        assertNotNull(declaringClass);
        Method[] methods = declaringClass.getDeclaredMethods();
        for (Method method : methods) {
            AccessibleSupport.forceSetAccessibleOnSingleMethod(method);
        }
    }

    public void testForceSetAccessibleOnSingleMethod_ChannelInputStream() throws ClassNotFoundException {
        // the class itself is not visible, make sure that no setAccessible(true) happens to the methods
        Class<?> declaringClass = Class.forName("sun.nio.ch.ChannelInputStream");
        assertNotNull(declaringClass);
        Method[] methods = declaringClass.getDeclaredMethods();
        for (Method method : methods) {
            AccessibleSupport.forceSetAccessibleOnSingleMethod(method);
        }
    }

    public void testForceSetAccessibleOnSingleMethod_DatagramSocketAdaptor() throws ClassNotFoundException {
        // the class itself is not visible, make sure that no setAccessible(true) happens to the methods
        Class<?> declaringClass = Class.forName("sun.nio.ch.DatagramSocketAdaptor");
        assertNotNull(declaringClass);
        Method[] methods = declaringClass.getDeclaredMethods();
        for (Method method : methods) {
            AccessibleSupport.forceSetAccessibleOnSingleMethod(method);
        }
    }

    public void testForceSetAccessibleOnSingleMethod_SelectionKeyImpl() throws ClassNotFoundException {
        // the class itself is not visible, make sure that no setAccessible(true) happens to the methods
        Class<?> declaringClass = Class.forName("sun.nio.ch.SelectionKeyImpl");
        Method[] methods = declaringClass.getDeclaredMethods();
        for (Method method : methods) {
            AccessibleSupport.forceSetAccessibleOnSingleMethod(method);
        }
    }

    public void testForceSetAccessibleOnSingleMethod_ownPackagePrivateClass() throws Exception {
        PackagePrivate packagePrivateInstance = new PackagePrivate();
        Class<PackagePrivate> declaringClass = PackagePrivate.class;
        Method[] methods = declaringClass.getDeclaredMethods();
        for (Method method : methods) {
            AccessibleSupport.forceSetAccessibleOnSingleMethod(method);
            Object result = method.invoke(packagePrivateInstance);
            assertTrue(result instanceof String);
            assertEquals(method.getName(), result);
        }
    }

    public void testGetAccessibleMethodsInHierarchy_EllipseIterator() throws ClassNotFoundException {
        Class<?> declaringClass = Class.forName("java.awt.geom.EllipseIterator");
        assertNotNull(declaringClass);
        List<Method> accessibleMethods = AccessibleSupport.getAccessibleMethodsInHierarchy(declaringClass);
        Set<String> methodNames = new HashSet<String>();
        for (Method method : accessibleMethods) {
            methodNames.add(method.getName());
        }
        assertFalse(methodNames.isEmpty());
        // EllipseIterator: getWindingRule, isDone
        assertTrue(methodNames.contains("getWindingRule"));
        assertTrue(methodNames.contains("isDone"));
        // Object: equals, hashCode, toString
        assertTrue(methodNames.contains("equals"));
        assertTrue(methodNames.contains("hashCode"));
        assertTrue(methodNames.contains("toString"));
        // Object: clone, finalize
        assertFalse(methodNames.contains("clone"));
        assertFalse(methodNames.contains("finalize"));
    }

    public void testGetAccessibleMethodsInHierarchy_Class() throws ClassNotFoundException {
        List<Method> accessibleMethods = AccessibleSupport.getAccessibleMethodsInHierarchy(Class.class);
        assertTrue(accessibleMethods.size() >= 75);
        Set<String> methodNames = new HashSet<String>();
        for (Method method : accessibleMethods) {
            methodNames.add(method.getName());
        }
        assertFalse(methodNames.isEmpty());
        // getName, getPackage, getInterfaces
        assertTrue(methodNames.contains("getName"));
        assertTrue(methodNames.contains("getPackage"));
        assertTrue(methodNames.contains("getInterfaces"));
        // - checkMemberAccess, checkPackageAccess, resolveName
        assertFalse(methodNames.contains("checkMemberAccess"));
        assertFalse(methodNames.contains("checkPackageAccess"));
        assertFalse(methodNames.contains("resolveName"));
    }

    public void testGetAccessibleMethodsInHierarchy_Enum() throws ClassNotFoundException {
        List<Method> accessibleMethods = AccessibleSupport.getAccessibleMethodsInHierarchy(Enum.class);
        assertTrue(accessibleMethods.size() >= 15);
        Set<String> methodNames = new HashSet<String>();
        for (Method method : accessibleMethods) {
            methodNames.add(method.getName());
        }
        assertFalse(methodNames.isEmpty());
        // Enum: equals, hashCode, toString, compareTo
        assertTrue(methodNames.contains("equals"));
        assertTrue(methodNames.contains("hashCode"));
        assertTrue(methodNames.contains("toString"));
        assertTrue(methodNames.contains("compareTo"));
        // Enum: clone, finalize, readObject, readObjectNoData
        assertFalse(methodNames.contains("clone"));
        assertFalse(methodNames.contains("finalize"));
        assertFalse(methodNames.contains("readObject"));
        assertFalse(methodNames.contains("readObjectNoData"));
    }

    public void testGetAccessibleMethodsInHierarchy_Throwable() throws ClassNotFoundException {
        List<Method> accessibleMethods = AccessibleSupport.getAccessibleMethodsInHierarchy(Throwable.class);
        assertTrue(accessibleMethods.size() >= 20);
        Set<String> methodNames = new HashSet<String>();
        for (Method method : accessibleMethods) {
            methodNames.add(method.getName());
        }
        assertFalse(methodNames.isEmpty());
        assertTrue(methodNames.contains("fillInStackTrace"));
        assertTrue(methodNames.contains("getMessage"));
        assertTrue(methodNames.contains("printStackTrace"));
    }

    public void testGetAccessibleMethodsInHierarchy_AnnotatedElement() throws ClassNotFoundException {
        List<Method> accessibleMethods = AccessibleSupport.getAccessibleMethodsInHierarchy(AnnotatedElement.class);
        assertTrue(accessibleMethods.size() >= 7);
        Set<String> methodNames = new HashSet<String>();
        for (Method method : accessibleMethods) {
            methodNames.add(method.getName());
        }
        assertFalse(methodNames.isEmpty());
        assertTrue(methodNames.contains("isAnnotationPresent"));
        assertTrue(methodNames.contains("getAnnotation"));
        assertTrue(methodNames.contains("getAnnotations"));
        assertTrue(methodNames.contains("getAnnotationsByType"));
        assertTrue(methodNames.contains("getDeclaredAnnotation"));
        assertTrue(methodNames.contains("getDeclaredAnnotations"));
        assertTrue(methodNames.contains("getDeclaredAnnotationsByType"));
    }

    public void testGetAccessibleFields_EllipseIterator() throws ClassNotFoundException {
        // the class itself is not visible, make sure that no setAccessible(true) happens to the fields
        Class<?> declaringClass = Class.forName("java.awt.geom.EllipseIterator");
        assertNotNull(declaringClass);
        Field[] fields = AccessibleSupport.getAccessibleFields(declaringClass);
        assertEquals(1, fields.length);
        assertEquals("CtrlVal", fields[0].getName());
    }

    public void testGetAccessibleFields_Class() {
        Field[] fields = AccessibleSupport.getAccessibleFields(Class.class);
        assertEquals(0, fields.length);
    }

    public void testGetAccessibleFields_Enum() {
        Field[] fields = AccessibleSupport.getAccessibleFields(Enum.class);
        assertEquals(0, fields.length);
    }

    public void testGetAccessibleFields_Error() {
        Field[] fields = AccessibleSupport.getAccessibleFields(Error.class);
        assertEquals(0, fields.length);
    }

    public void testGetAccessibleFields_OutOfMemoryError() {
        Field[] fields = AccessibleSupport.getAccessibleFields(OutOfMemoryError.class);
        assertEquals(0, fields.length);
    }

    public void testGetAccessibleFields_Throwable() {
        Field[] fields = AccessibleSupport.getAccessibleFields(Throwable.class);
        assertEquals(0, fields.length);
    }

    public void testGetAccessibleFields_VirtualMachineError() {
        Field[] fields = AccessibleSupport.getAccessibleFields(VirtualMachineError.class);
        assertEquals(0, fields.length);
    }

    public void testGetAccessibleFields_PackagePrivate() {
        Field[] fields = AccessibleSupport.getAccessibleFields(PackagePrivate.class);
        assertEquals(4, fields.length);
    }

    public void testInvokeMethod_blocked() throws ClassNotFoundException, NoSuchMethodException, SecurityException,
                    IllegalArgumentException, InvocationTargetException {
        Class<?> declaringClass = Class.forName("sun.nio.ch.FileChannelImpl");
        assertNotNull(declaringClass);
        Method method = declaringClass.getDeclaredMethod("getMappedBufferPool");
        assertNotNull(method);
        try {
            AccessibleSupport.invokeMethod(method, declaringClass, new Object[0]);
            fail("IllegalAccessException expected");
        } catch (IllegalAccessException re) {
            assertTrue(re.getMessage().contains("would lead to an illegal access"));
        }
    }

    public void testInvokeMethod_illegalButExplicitlyAllowed() throws ClassNotFoundException, NoSuchMethodException,
                    SecurityException, IllegalArgumentException, InvocationTargetException, IllegalAccessException {
        Class<?> declaringClass = Class.forName("sun.nio.ch.FileChannelImpl");
        assertNotNull(declaringClass);
        Method method = declaringClass.getDeclaredMethod("getMappedBufferPool");
        assertNotNull(method);
        try {
            setAllowAnyMethodCalls(true);
            Object result = AccessibleSupport.invokeMethod(method, declaringClass, new Object[0]);
            assertNotNull(result);
        } finally {
            setAllowAnyMethodCalls(false); // back to the default
        }
    }

    public void testInvokeMethod_normal() throws NoSuchMethodException, SecurityException, IllegalAccessException,
                    IllegalArgumentException, InvocationTargetException {
        Method method = Class.class.getDeclaredMethod("getName");
        assertNotNull(method);
        assertEquals("java.math.BigDecimal", AccessibleSupport.invokeMethod(method, BigDecimal.class, new Object[0]));
    }

    private void setAllowAnyMethodCalls(boolean allowAnyMethodCalls) {
        try {
            Field field = AccessibleSupport.class.getDeclaredField("ALLOW_ANY_METHOD_CALLS");
            assertNotNull(field);
            field.setAccessible(true);
            field.setBoolean(null, allowAnyMethodCalls);
        } catch (Exception e) {
            fail("error setting private field: " + e.getMessage());
        }
    }

}
