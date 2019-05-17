/**
 *
 */
package org.cs.portfolio.reports.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author michael.delacruz
 *
 */
public class Converter {

    public static Object convert(Object value) {
        if (value instanceof String)
            return (String) value;
        else if (value instanceof Integer)
            return (Integer) value;
        else if (value instanceof Long)
            return value;
        else if (value instanceof Double)
            return (Double) value;
        else if (value instanceof Boolean)
            return (Boolean) value;
        else if(value instanceof Iterable)
            return (Iterable) value;
        return null;
    }

}
