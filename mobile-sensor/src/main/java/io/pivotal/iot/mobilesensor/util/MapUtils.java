package io.pivotal.iot.mobilesensor.util;

import java.util.HashMap;
import java.util.Map;

/**
 * @author sgupta
 * @since 6/29/15.
 */
public class MapUtils {

  @SafeVarargs
  public static <K,V> Map<K,V> createMap(KeyValuePair<K,V> ... pairs) {
    Map<K,V> map = new HashMap<>();
    for (KeyValuePair<K, V> pair : pairs) {
      map.put(pair.getKey(), pair.getVal());
    }
    return map;
  }
}
