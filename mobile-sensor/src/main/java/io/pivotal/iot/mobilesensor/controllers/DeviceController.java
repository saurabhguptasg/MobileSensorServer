package io.pivotal.iot.mobilesensor.controllers;

import io.pivotal.iot.mobilesensor.data.Datastore;
import io.pivotal.iot.mobilesensor.model.Device;
import io.pivotal.iot.mobilesensor.msg.CreateDeviceRequest;
import io.pivotal.iot.mobilesensor.msg.GenericStatusResponse;
import io.pivotal.iot.mobilesensor.util.IdUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * @author sgupta
 * @since 6/26/15.
 */
@RestController
@RequestMapping(value = "/device/**", produces = "application/json")
public class DeviceController {

  @Autowired
  Datastore datastore;


  @RequestMapping(value = "/", method = RequestMethod.POST)
  @ResponseBody
  public ResponseEntity<GenericStatusResponse> putDevice(@RequestBody CreateDeviceRequest createDeviceRequest) {
    Device device = createDeviceRequest.getDevice();
    if(StringUtils.isEmpty(device.getDeviceId())) {
      device = new Device(IdUtils.newId(), device.getDeviceType());
    }
    datastore.putDevice(device);
    return new ResponseEntity<>(GenericStatusResponse.okWithData(device), HttpStatus.OK);
  }

  @RequestMapping(value = "/{deviceId}", method = RequestMethod.GET)
  @ResponseBody
  public ResponseEntity<GenericStatusResponse<Device>> getDevice(@PathVariable("deviceId") String deviceId) {
    Device device = datastore.getDevice(deviceId);
    return device == null ?
           new ResponseEntity<>((GenericStatusResponse<Device>) null, HttpStatus.NOT_FOUND) :
           new ResponseEntity<>(GenericStatusResponse.okWithData(device), HttpStatus.OK);
  }

  @RequestMapping(value = "/{deviceId}", method = RequestMethod.DELETE)
  @ResponseBody
  public ResponseEntity<GenericStatusResponse> deleteDevice(@PathVariable("deviceId") String deviceId) {
    datastore.deleteDevice(deviceId);
    return new ResponseEntity<>(GenericStatusResponse.OK, HttpStatus.OK);
  }

}
