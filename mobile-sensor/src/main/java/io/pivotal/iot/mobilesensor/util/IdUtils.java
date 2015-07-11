package io.pivotal.iot.mobilesensor.util;

import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.TimeZone;

/**
 * @author sgupta
 * @since 6/26/15.
 */
public class IdUtils {
  public static final Random random = new SecureRandom();
  public static final char[] chars = "abcdefghijklmnopqrstuvwxyz0123456789".toCharArray();

  public static final String format = "YYYY/MM/dd/HH/mm/ss.SSS";
  public static final TimeZone timeZone = TimeZone.getTimeZone("GMT");

  public static String newId() {
    StringBuilder builder = new StringBuilder(Long.toString(System.currentTimeMillis(), 36));
    for (int i = 0; i < 8; i++) {
      builder.append(chars[random.nextInt(chars.length)]);
    }
    return  builder.toString();
  }

  public static String delimitedIdFromTimestamp(Long timestamp) {
    Date date = new Date(timestamp);
    SimpleDateFormat dateFormat = new SimpleDateFormat(format);
    dateFormat.setTimeZone(timeZone);
    return dateFormat.format(date);
  }
}
