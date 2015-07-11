package io.pivotal.iot.mobilesensor.msg;

import io.pivotal.iot.mobilesensor.model.Device;

/**
 * @author sgupta
 * @since 6/26/15.
 */
public class CreateDeviceRequest {
  private final Device device;

  public CreateDeviceRequest(Device device) {
    this.device = device;
  }

  public CreateDeviceRequest() {
    this(null);
  }

  public Device getDevice() {
    return device;
  }
}
