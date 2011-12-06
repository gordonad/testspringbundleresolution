
package org.springframework.testspringbundleresolution;

import static org.ops4j.pax.exam.CoreOptions.equinox;
import static org.ops4j.pax.exam.CoreOptions.mavenBundle;
import static org.ops4j.pax.exam.OptionUtils.combine;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.Configuration;
import org.ops4j.pax.exam.junit.JUnit4TestRunner;

//import static org.ops4j.pax.exam.container.def.PaxRunnerOptions.scanFeatures;

@RunWith(JUnit4TestRunner.class)
public class TestSpringBundleResolution {

    @Configuration
    public static Option[] configuration() throws Exception {
        return combine(null, mavenBundle("org.springframework", "spring-core", "3.0.6.RELEASE"), equinox().version("3.7.0"));
    }

    @Test
    public void test() throws Exception {
        System.out.println("Hello from a testcase!");
    }
}