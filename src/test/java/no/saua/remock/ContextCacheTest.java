package no.saua.remock;

import no.saua.remock.ContextCacheTest.*;
import no.saua.remock.exampleapplication.AnInterface;
import no.saua.remock.exampleapplication.AnInterfaceImplTwo;
import no.saua.remock.exampleapplication.SomeService;
import no.saua.remock.exampleapplication.SomeServiceWithDependencies;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;

import javax.inject.Inject;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Tests various forms of cache scenarios. Note that the ordering in the @SuiteClasses is significant.
 */
@RunWith(Suite.class)
@SuiteClasses({SomeTestClass.class, SomeTestClassEqual1.class, SomeTestClassEqual2.class, SomeTestClassNotEqual0.class,
        SomeTestClassNotEqual1.class, SomeTestClassNotEqual2.class, SomeTestClassNotEqual3.class})
public class ContextCacheTest {

    private static ApplicationContext cachedSpringContextWithRejectAndSpy;
    private static ApplicationContext cachedSpringContextWithReject;
    private static ApplicationContext cachedSpringContextWithSpy;
    private static ApplicationContext cachedSpringContextWithImpl;


    // :: The context of this test will be cached
    @ContextConfiguration(classes = SomeService.class)
    @Reject(SomeServiceWithDependencies.class)
    @WrapWithSpy(AnInterface.class)
    @ReplaceWithImpl(value = AnInterface.class, with = AnInterfaceImplTwo.class)
    public static class SomeTestClass extends CommonTest {

        @Inject
        private ApplicationContext springContext;

        @Test
        public void test() {
            assertNotNull("Spring context shall not be null", springContext);
            cachedSpringContextWithRejectAndSpy = springContext;
        }
    }

    // :: This test should use same spring context as previous test.
    @ContextConfiguration(classes = SomeService.class)
    @Reject(SomeServiceWithDependencies.class)
    @WrapWithSpy(AnInterface.class)
    @ReplaceWithImpl(value = AnInterface.class, with = AnInterfaceImplTwo.class)
    public static class SomeTestClassEqual1 extends CommonTest {

        @Inject
        private ApplicationContext springContext;

        @Test
        public void test() {
            // Verify that the previous test has run
            assertNotNull("cachedSpringContextWithRejectAndSpy shall be set", cachedSpringContextWithRejectAndSpy);

            assertNotNull("Spring context shall not be null", springContext);
            assertTrue("Should get the exact same spring context", cachedSpringContextWithRejectAndSpy ==
                    springContext);
        }
    }

    // :: This test should use same spring context a first test because we extend it.
    public static class SomeTestClassEqual2 extends SomeTestClass {

        @Inject
        private ApplicationContext springContext;

        @Test
        public void test() {
            // Verify that the previous test has run
            assertNotNull("cachedSpringContextWithRejectAndSpy shall be set", cachedSpringContextWithRejectAndSpy);

            assertNotNull("Spring context shall not be null", springContext);
            assertTrue("Should get the exact same spring context", cachedSpringContextWithRejectAndSpy ==
                    springContext);
        }
    }

    // :: This test should NOT use same spring context as previous test.
    @DisableLazyInit
    @ContextConfiguration(classes = SomeService.class)
    @Reject(SomeServiceWithDependencies.class)
    @WrapWithSpy(AnInterface.class)
    @ReplaceWithImpl(value = AnInterface.class, with= AnInterfaceImplTwo.class)
    public static class SomeTestClassNotEqual0 extends CommonTest {

        @Inject
        private ApplicationContext springContext;

        @Test
        public void test() {
            // Verify that the previous test has run
            assertNotNull("cachedSpringContextWithRejectAndSpy shall be set", cachedSpringContextWithRejectAndSpy);


            assertNotNull("Spring context shall not be null", springContext);
            assertNotEquals("Should NOT get the exact same spring context", cachedSpringContextWithRejectAndSpy,
                            springContext);
        }
    }

    // :: This test should NOT use same spring context as first test.
    @ContextConfiguration(classes = SomeService.class)
    @Reject(SomeServiceWithDependencies.class)
    public static class SomeTestClassNotEqual1 extends CommonTest {

        @Inject
        private ApplicationContext springContext;

        @Test
        public void test() {
            // Verify that the previous test has run
            assertNotNull("cachedSpringContextWithRejectAndSpy shall be set", cachedSpringContextWithRejectAndSpy);


            assertNotNull("Spring context shall not be null", springContext);
            assertNotEquals("Should get different spring context", cachedSpringContextWithRejectAndSpy, springContext);
            cachedSpringContextWithReject = springContext;
        }
    }

    // :: This test should NOT use same spring context as first test.
    @ContextConfiguration(classes = SomeService.class)
    @WrapWithSpy(AnInterface.class)
    public static class SomeTestClassNotEqual2 extends CommonTest {

        @Inject
        private ApplicationContext springContext;

        @Test
        public void test() {
            // Verify that the previous test has run
            assertNotNull("cachedSpringContextWithRejectAndSpy shall be set", cachedSpringContextWithRejectAndSpy);
            assertNotNull("cachedSpringContextWithReject shall be set", cachedSpringContextWithReject);


            assertNotNull("Spring context shall not be null", springContext);
            assertNotEquals("Should get different spring context", cachedSpringContextWithRejectAndSpy, springContext);
            assertNotEquals("Should get different spring context", cachedSpringContextWithReject, springContext);
            cachedSpringContextWithSpy = springContext;
        }
    }

    // :: This test should NOT use same spring context as first test.
    @ContextConfiguration(classes = SomeService.class)
    @ReplaceWithImpl(value = AnInterface.class, with= AnInterfaceImplTwo.class)
    public static class SomeTestClassNotEqual3 extends CommonTest {

        @Inject
        private ApplicationContext springContext;;

        @Test
        public void test() {
            // Verify that the previous test has run
            assertNotNull("cachedSpringContextWithRejectAndSpy shall be set", cachedSpringContextWithRejectAndSpy);
            assertNotNull("cachedSpringContextWithReject shall be set", cachedSpringContextWithReject);
            assertNotNull("cachedSpringContextWithSpy shall be set", cachedSpringContextWithSpy);


            assertNotNull("Spring context shall not be null", springContext);
            assertNotEquals("Should get different spring context", cachedSpringContextWithRejectAndSpy, springContext);
            assertNotEquals("Should get different spring context", cachedSpringContextWithReject, springContext);
            cachedSpringContextWithImpl = springContext;
        }
    }

    // :: This test class does not rejects something, thus it should get a fresh spring context
    @ContextConfiguration(classes = SomeService.class)
    public static class SomeTestClassNotEqual4 extends CommonTest {

        @Inject
        private ApplicationContext springContext;

        @Test
        public void test() {
            // Verify that the previous test has run
            assertNotNull("cachedSpringContextWithRejectAndSpy shall be set", cachedSpringContextWithRejectAndSpy);
            assertNotNull("cachedSpringContextWithReject shall be set", cachedSpringContextWithReject);
            assertNotNull("cachedSpringContextWithSpy shall be set", cachedSpringContextWithSpy);
            assertNotNull("cachedSpringContextWithImpl shall be set", cachedSpringContextWithImpl);


            assertNotNull("Spring context shall not be null", springContext);
            assertNotEquals("Should get different spring context", cachedSpringContextWithRejectAndSpy, springContext);
            assertNotEquals("Should get different spring context", cachedSpringContextWithImpl, springContext);
            assertNotEquals("Should get different spring context", cachedSpringContextWithReject, springContext);
            assertNotEquals("Should get different spring context", cachedSpringContextWithSpy, springContext);
        }
    }

}
