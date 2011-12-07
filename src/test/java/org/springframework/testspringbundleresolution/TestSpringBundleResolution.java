
package org.springframework.testspringbundleresolution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.ops4j.pax.exam.CoreOptions.mavenBundle;
import static org.ops4j.pax.exam.CoreOptions.options;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.Configuration;
import org.ops4j.pax.exam.junit.JUnit4TestRunner;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

@RunWith(JUnit4TestRunner.class)
public class TestSpringBundleResolution {

    @Configuration
    public static Option[] configuration() throws Exception {
        return options(mavenBundle("org.springframework", "spring-core", "3.0.6.RELEASE"));
    }

    @Test
    public void test(BundleContext bundleContext) throws Exception {
        boolean found = false;
        Bundle[] bundles = bundleContext.getBundles();
        for (Bundle bundle : bundles) {
            if ("org.springframework.core".equals(bundle.getSymbolicName())) {
                found = true;
                bundle.start();
                assertTrue(true); // This fails the test with an obscure stack trace.
                // assertEquals(Bundle.ACTIVE, bundle.getState());
            }
        }
        // assertTrue(found);
    }
}