package fi.helsinki.cs.titokone;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class SvcNames {
    public static final Map<String, Integer> SVCS;

    static {
        Map<String, Integer> devs = new HashMap<String, Integer>();
        devs.put("halt", 11);
        devs.put("read", 12);
        devs.put("write", 13);
        devs.put("time", 14);
        devs.put("date", 15);
        SVCS = Collections.unmodifiableMap(devs);
    }

    public static Integer lookupIgnoringCase(String name) {
        return SVCS.get(name.toLowerCase());
    }

    public static String lookupByValue(int value) {
        for(Map.Entry<String, Integer> entries : SVCS.entrySet())
            if(entries.getValue() == value)
                return entries.getKey();
        return null;
    }
}
