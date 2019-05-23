/**
 *
 */
package org.cs.portfolio.reports.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DateHelper {

    public static boolean isValidDate(String inDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        try {
            dateFormat.parse(inDate.trim());
        } catch (ParseException pe) {
            return false;
        }
        return true;
    }

}
