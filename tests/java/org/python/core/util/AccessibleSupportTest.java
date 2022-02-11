package org.python.core.util;

import java.awt.geom.Ellipse2D;
import java.awt.geom.RectangularShape;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

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
            AccessibleSupport.forceSetAccessibleOnSingleMethod(method, declaringClass);
        }
    }

    public void testForceSetAccessibleOnSingleMethod_ChannelInputStream() throws ClassNotFoundException {
        // the class itself is not visible, make sure that no setAccessible(true) happens to the methods
        Class<?> declaringClass = Class.forName("sun.nio.ch.ChannelInputStream");
        assertNotNull(declaringClass);
        Method[] methods = declaringClass.getDeclaredMethods();
        for (Method method : methods) {
            AccessibleSupport.forceSetAccessibleOnSingleMethod(method, declaringClass);
        }
    }

    public void testForceSetAccessibleOnSingleMethod_DatagramSocketAdaptor() throws ClassNotFoundException {
        // the class itself is not visible, make sure that no setAccessible(true) happens to the methods
        Class<?> declaringClass = Class.forName("sun.nio.ch.DatagramSocketAdaptor");
        assertNotNull(declaringClass);
        Method[] methods = declaringClass.getDeclaredMethods();
        for (Method method : methods) {
            AccessibleSupport.forceSetAccessibleOnSingleMethod(method, declaringClass);
        }
    }

    public void testForceSetAccessibleOnSingleMethod_SelectionKeyImpl() throws ClassNotFoundException {
        // the class itself is not visible, make sure that no setAccessible(true) happens to the methods
        Class<?> declaringClass = Class.forName("sun.nio.ch.SelectionKeyImpl");
        Method[] methods = declaringClass.getDeclaredMethods();
        for (Method method : methods) {
            AccessibleSupport.forceSetAccessibleOnSingleMethod(method, declaringClass);
        }
    }
    

    public void testForceSetAccessibleOnSingleMethod_ownPackagePrivateClass() throws Exception {
        PackagePrivate packagePrivateInstance = new PackagePrivate();
        Class<PackagePrivate> declaringClass = PackagePrivate.class;
        Method[] methods = declaringClass.getDeclaredMethods();
        for (Method method : methods) {
            AccessibleSupport.forceSetAccessibleOnSingleMethod(method, declaringClass);
            Object result = method.invoke(packagePrivateInstance);
            assertTrue(result instanceof String);
            assertEquals(method.getName(), result);
        }
    }    
    

}
