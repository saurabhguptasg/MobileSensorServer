package io.pivotal.iot.mobilesensor.model;

import java.util.List;

/**
 * @author sgupta
 * @since 7/1/15.
 */
public class EventItemList {
  private final String deviceId;
  private final String prefix;
  private final List<String> eventIds;
  private final String nextToken;

  public EventItemList() {
    this(null, null, null, null);
  }

  public EventItemList(String deviceId, String prefix, List<String> eventIds, String nextToken) {
    this.deviceId = deviceId;
    this.prefix = prefix;
    this.eventIds = eventIds;
    this.nextToken = nextToken;
  }

  public String getDeviceId() {
    return deviceId;
  }

  public String getPrefix() {
    return prefix;
  }

  public List<String> getEventIds() {
    return eventIds;
  }

  public String getNextToken() {
    return nextToken;
  }
}
