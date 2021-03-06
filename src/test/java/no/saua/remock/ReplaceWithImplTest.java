package no.saua.remock;

import no.saua.remock.exampleapplication.*;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import javax.inject.Inject;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Tests the {@link no.saua.remock.ReplaceWithImpl} annotation.
 */
@RunWith(Enclosed.class)
public class ReplaceWithImplTest {

    /**
     * Test annotation on the test-class
     */
    @ReplaceWithImpl(value = AnInterfaceImplOne.class, with = AnInterfaceImplTwo.class)
    @ContextConfiguration(classes = {AnInterfaceImplOne.class})
    public static class ReplaceWithImplAnnotatedOnClassTest extends CommonTest {

        @Inject
        private AnInterface implementation;

        @Inject
        private AnInterfaceImplTwo implTwo;

        @Autowired(required = false)
        private AnInterfaceImplOne implOne;

        @Test
        public void test() {
            assertEquals(AnInterfaceImplTwo.class, implementation.getClass());
            assertEquals(AnInterfaceImplTwo.class, implTwo.getClass());
            assertEquals("This will specific class should not exist in the appcontext", null, implOne);
        }
    }

    /**
     * Test annotation on a field. This should automatically inject the field due to meta-annotation.
     */
    @ContextConfiguration(classes = {AnInterfaceImplOne.class})
    public static class ReplaceWithImplAnnotatedOnFieldTest extends CommonTest {

        @ReplaceWithImpl(value = AnInterfaceImplOne.class, with = AnInterfaceImplTwo.class)
        private AnInterface anInterface;

        @Test
        public void test() {
            assertEquals(AnInterfaceImplTwo.class, anInterface.getClass());
        }
    }

    /**
     * Test repeated annotation on a class.
     */
    @ReplaceWithImpl(value = AnInterfaceImplOne.class, with= AnInterfaceImplTwo.class)
    @ReplaceWithImpl(value = SomeServiceWithDependencies.class, with=SomeService.class)
    @ContextConfiguration(classes = {AnInterfaceImplOne.class, SomeServiceWithDependencies.class})
    public static class RepeatedReplaceWithImplAnnotationsTest extends CommonTest {

        @Inject
        private AnInterface anInterface;

        @Inject
        private SomeService someService;

        @Autowired(required = false)
        private SomeServiceWithDependencies someServiceWithDependencies;

        @Test
        public void test() {
            assertEquals(AnInterfaceImplTwo.class, anInterface.getClass());
            assertNotNull(someService);
            assertNull(someServiceWithDependencies);
        }
    }
}
