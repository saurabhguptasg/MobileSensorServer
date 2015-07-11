package io.pivotal.iot.mobilesensor.model;

import java.util.Map;

/**
 * @author sgupta
 * @since 6/26/15.
 */
public class SensorDataItem {
  private final String type;
  private final Long timestamp;
  private final Map attributes;

  SensorDataItem() {
    this(null, null, null);
  }

  public SensorDataItem(String type, Long timestamp, Map attributes) {
    this.type = type;
    this.timestamp = timestamp;
    this.attributes = attributes;
  }

  public String getType() {
    return type;
  }

  public Long getTimestamp() {
    return timestamp;
  }

  public Map getAttributes() {
    return attributes;
  }
}
