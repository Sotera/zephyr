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
