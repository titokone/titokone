// Copyright © 2004-2006 University of Helsinki, Department of Computer Science
// Copyright © 2012 various contributors
// This software is released under GNU Lesser General Public License 2.1.
// The license text is at http://www.gnu.org/licenses/lgpl-2.1.html

package fi.helsinki.cs.titokone.devices;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class DeviceNames {
    public static final Map<String, Integer> DEVICES;

    static {
        Map<String, Integer> devs = new HashMap<String, Integer>();
        devs.put("crt", 0);
        devs.put("kbd", 1);
        devs.put("stdin", 6);
        devs.put("stdout", 7);
        // TODO: add rest of the device names here.
        // or perhaps have a name for each of the device objects, and automatically assign these values
        DEVICES = Collections.unmodifiableMap(devs);
    }

    public static Integer lookupIgnoringCase(String name) {
        return DEVICES.get(name.toLowerCase());
    }

    public static String lookupByValue(int value) {
        for(Map.Entry<String, Integer> entries : DEVICES.entrySet())
            if(entries.getValue() == value)
                return entries.getKey();
        return null;
    }
}
