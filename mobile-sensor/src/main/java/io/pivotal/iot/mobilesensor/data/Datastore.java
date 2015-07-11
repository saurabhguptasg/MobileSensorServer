package io.pivotal.iot.mobilesensor.data;

import io.pivotal.iot.mobilesensor.model.Device;
import io.pivotal.iot.mobilesensor.model.EventItemList;
import io.pivotal.iot.mobilesensor.msg.CreateDeviceRequest;

import java.io.IOException;

/**
 * @author sgupta
 * @since 6/29/15.
 */
public interface Datastore {

  public void putDevice(Device device);
  public Device getDevice(String deviceId);
  public void deleteDevice(String deviceId);

  public void putEvent(String eventId, String jsonString);

  String getEvent(String eventId) throws IOException;

  EventItemList getEventIdList(String deviceId, String prefix, int count, String token);

  EventItemList getByPrefix(String prefix, int count, String token);
}
