package io.pivotal.iot.mobilesensor.model;

/**
 * @author sgupta
 * @since 7/1/15.
 */
public class EventItem {
  private final String deviceId;
  private final SensorDataItem[] dataItems;

  EventItem() {
    this(null, null);
  }

  public EventItem(String deviceId, SensorDataItem... dataItems) {
    this.deviceId = deviceId;
    this.dataItems = dataItems;
  }

  public String getDeviceId() {
    return deviceId;
  }

  public SensorDataItem[] getDataItems() {
    return dataItems;
  }
}
