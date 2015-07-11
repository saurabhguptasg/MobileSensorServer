package io.pivotal.iot.mobilesensor.util;

/**
 * @author sgupta
 * @since 6/29/15.
 */
public final class KeyValuePair<K,V> {
  private final K key;
  private final V val;

  public KeyValuePair(K key, V val) {
    this.key = key;
    this.val = val;
  }

  public K getKey() {
    return key;
  }

  public V getVal() {
    return val;
  }
}
