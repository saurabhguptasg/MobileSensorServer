package io.pivotal.iot.mobilesensor.model;

/**
 * @author sgupta
 * @since 6/29/15.
 */
public class Device {
  private final String deviceId;
  private final DeviceType deviceType;

  public Device() {
    this(null, null);
  }

  public Device(String deviceId, DeviceType deviceType) {
    this.deviceId = deviceId;
    this.deviceType = deviceType;
  }

  public String getDeviceId() {
    return deviceId;
  }

  public DeviceType getDeviceType() {
    return deviceType;
  }
}
