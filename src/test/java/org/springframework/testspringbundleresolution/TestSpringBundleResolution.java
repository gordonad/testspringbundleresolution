/*
 * Copyright 2011 VMware Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

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

    private static final String SPRINGFRAMEWORK_MILESTONE_MAVEN_URL = "http://maven.springframework.org/milestone";

    private static final String EBR_EXTERNAL_MAVEN_URL = "http://repository.springsource.com/maven/bundles/external";

    private static final String SPRING_VERSION = "3.1.0.RC2";

    @Inject
    private BundleContext bundleContext;

    @Configuration
    public static Option[] configuration() throws Exception {
        return options(//
            provisionSpringBundle("spring-core"), //
            provisionSpringBundle("spring-beans"), //
            provisionSpringBundle("spring-aop"), //
            provisionSpringBundle("spring-asm"), //
            provisionSpringBundle("spring-aspects"), //
            provisionSpringBundle("spring-context"), //
            provisionSpringBundle("spring-context-support"), //
            provisionSpringBundle("spring-expression"), //
            provisionSpringBundle("spring-jdbc"), //
            provisionSpringBundle("spring-jms"), //
            provisionSpringBundle("spring-orm"), //
            provisionSpringBundle("spring-oxm"), //
            provisionSpringBundle("spring-tx"), //
            provisionSpringBundle("spring-web"), //
            provisionSpringBundle("spring-webmvc"), //
            provisionSpringBundle("spring-webmvc-portlet"), //

            // mandatory dependencies common to multiple Spring bundles
            provisionExternalBundle("org.apache.commons", "com.springsource.org.apache.commons.logging", "1.1.1"), //
            
            // spring-aop mandatory dependencies
            provisionExternalBundle("org.aopalliance", "com.springsource.org.aopalliance", "1.0.0"), //

            // spring-jms mandatory dependencies
            provisionExternalBundle("javax.jms", "com.springsource.javax.jms", "1.1.0"), //

            // spring-web mandatory dependencies
            provisionExternalBundle("javax.servlet", "javax.servlet", "3.0.0.v201103241009"), //

            // spring-webmvc-portlet mandatory dependencies
            provisionExternalBundle("javax.portlet", "com.springsource.javax.portlet", "2.0.0.v20110525"), //

            junitBundles());
    }

    private static Option provisionExternalBundle(String groupId, String artifactId, String version) {
        return provision(bundle(mavenUrl(EBR_EXTERNAL_MAVEN_URL, groupId, artifactId, version)));
    }

    private static Option provisionSpringBundle(String artifactId) {
        return provision(bundle(springBundleUrl(artifactId)));
    }

    private static String springBundleUrl(String artifactId) {
        String version = SPRING_VERSION;
        String groupId = "org.springframework";
        String repositoryUrl = SPRINGFRAMEWORK_MILESTONE_MAVEN_URL;
        return mavenUrl(repositoryUrl, groupId, artifactId, version);
    }

    private static String mavenUrl(String repositoryUrl, String groupId, String artifactId, String version) {
        return repositoryUrl + "/" + groupId.replaceAll("\\.", "/") + "/" + artifactId + "/" + version + "/" + artifactId + "-" + version + ".jar";
    }

    @Test
    public void testSpringCoreIsResolvable() throws Exception {
        int found = 0;
        Bundle[] bundles = this.bundleContext.getBundles();
        for (Bundle bundle : bundles) {
            String symbolicName = bundle.getSymbolicName();
            if (symbolicName.startsWith("org.springframework")) {
                found++;
                bundle.start();
                assertEquals(symbolicName + " is not ACTIVE", Bundle.ACTIVE, bundle.getState());
            }
        }
        assertEquals("Unexpected number of Spring bundles found", 16, found);
    }
}