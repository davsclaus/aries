/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.aries.blueprint.itests;

import static org.junit.Assert.assertNotNull;
import static org.ops4j.pax.exam.CoreOptions.options;
import static org.ops4j.pax.exam.CoreOptions.systemProperty;
import static org.ops4j.pax.exam.OptionUtils.combine;

import java.util.Hashtable;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.JUnit4TestRunner;
import org.osgi.framework.Bundle;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;

/**
 * this test is based on blueprint container test, but this test starts the
 * blueprint sample before the blueprint bundle is started so going a slightly 
 * different code path
 *
 */
@RunWith(JUnit4TestRunner.class)
public class BlueprintContainer2Test extends AbstractIntegrationTest {

    @Test
    public void test() throws Exception {
        // Create a config to check the property placeholder
        ConfigurationAdmin ca = getOsgiService(ConfigurationAdmin.class);
        Configuration cf = ca.getConfiguration("blueprint-sample-placeholder", null);
        Hashtable props = new Hashtable();
        props.put("key.b", "10");
        cf.update(props);

        Bundle bundle = getInstalledBundle("org.apache.aries.blueprint.sample");
        Bundle blueprintBundle = getInstalledBundle("org.apache.aries.blueprint");
        assertNotNull(bundle);

        bundle.start();
        blueprintBundle.start();
        
        // do the test
        testBlueprintContainer(bundle);
    }

    @org.ops4j.pax.exam.junit.Configuration
    public static Option[] configuration() {
        Option[] options = options(

                // this is how you set the default log level when using pax logging (logProfile)
                systemProperty("org.ops4j.pax.logging.DefaultServiceLog.level").value("DEBUG"),
    
                // Bundles
                mavenBundle("org.apache.aries.blueprint", "org.apache.aries.blueprint.sample").noStart(),
                mavenBundle("org.apache.aries.blueprint", "org.apache.aries.blueprint").noStart()
    
                // org.ops4j.pax.exam.container.def.PaxRunnerOptions.vmOption("-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=5005"),
                );
        options = combine (getSharedOptions(), options);
        options = updateOptions(options);
        return options;
    }

}
