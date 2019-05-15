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
            if(DateHelper.isValidDate((String)value)) {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                Date date = null;
                try {
                    date = formatter.parse((String)value);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return date;
            } else {
                return (String) value;
            }
        else if (value instanceof Integer)
            return (Integer) value;
        else if (value instanceof Long)
            return value;
        else if (value instanceof Double)
            return (Double) value;
        else if (value instanceof Boolean)
            return (Boolean) value;
        return null;
    }

}
