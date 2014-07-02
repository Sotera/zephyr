/*
* Licensed to the Apache Software Foundation (ASF) under one
* or more contributor license agreements. See the NOTICE file
* distributed with this work for additional information
* regarding copyright ownership. The ASF licenses this file
* to you under the Apache License, Version 2.0 (the
* "License"); you may not use this file except in compliance
* with the License. You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package org.zephyr.util;

import com.fasterxml.uuid.EthernetAddress;
import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.TimeBasedGenerator;

/**
 * Purpose: Utilities to facilitate UUID generation
 */
public class UUIDHelper {

    // Instantiate the object that will handle the generation of UUIDs
    private final static TimeBasedGenerator generator = Generators.timeBasedGenerator(EthernetAddress.fromInterface());

    /**
     * Generates a time based UUID using the mac address of the machine, if available
     *
     * @return
     */
    public static String generateUUID() {
        return generator.generate().toString();
    }

}
