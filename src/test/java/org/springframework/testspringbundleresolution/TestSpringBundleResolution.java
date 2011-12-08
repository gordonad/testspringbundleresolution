
package org.springframework.testspringbundleresolution;

import static org.junit.Assert.assertEquals;
import static org.ops4j.pax.exam.CoreOptions.bundle;
import static org.ops4j.pax.exam.CoreOptions.junitBundles;
import static org.ops4j.pax.exam.CoreOptions.options;
import static org.ops4j.pax.exam.CoreOptions.provision;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.Configuration;
import org.ops4j.pax.exam.junit.JUnit4TestRunner;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

@RunWith(JUnit4TestRunner.class)
public class TestSpringBundleResolution {

    private static final String SPRINGFRAMEWORK_MAVEN_URL = "http://maven.springframework.org/milestone";

    private static final String SPRING_VERSION = "3.1.0.RC2";

    @Inject
    private BundleContext bundleContext;

    @Configuration
    public static Option[] configuration() throws Exception {
        return options(provisionSpringBundle("spring-core"), provisionSpringBundle("spring-beans"), junitBundles());
    }

    private static Option provisionSpringBundle(String artifactId) {
        return provision(bundle(springBundleUrl(artifactId)).start());
    }

    private static String springBundleUrl(String artifactId) {
        return SPRINGFRAMEWORK_MAVEN_URL + "/org/springframework/" + artifactId + "/" + SPRING_VERSION + "/" + artifactId + "-" + SPRING_VERSION
            + ".jar";
    }

    @Test
    public void testSpringCoreIsResolvable() throws Exception {
        int found = 0;
        Bundle[] bundles = this.bundleContext.getBundles();
        for (Bundle bundle : bundles) {
            if (bundle.getSymbolicName().startsWith("org.springframework")) {
                found++;
                assertEquals(Bundle.ACTIVE, bundle.getState());
            }
        }
        assertEquals(2, found);
    }
}