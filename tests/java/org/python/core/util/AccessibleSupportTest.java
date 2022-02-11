package org.python.core.util;

import java.awt.geom.Ellipse2D;
import java.awt.geom.RectangularShape;
import java.lang.reflect.Constructor;
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
    
}
