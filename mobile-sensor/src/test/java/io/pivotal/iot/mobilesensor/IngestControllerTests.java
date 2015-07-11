package io.pivotal.iot.mobilesensor;

import io.pivotal.iot.mobilesensor.data.Datastore;
import io.pivotal.iot.mobilesensor.model.Device;
import io.pivotal.iot.mobilesensor.model.DeviceType;
import io.pivotal.iot.mobilesensor.model.EventItem;
import io.pivotal.iot.mobilesensor.model.SensorDataItem;
import io.pivotal.iot.mobilesensor.msg.CreateDeviceRequest;
import io.pivotal.iot.mobilesensor.msg.GenericStatusResponse;
import io.pivotal.iot.mobilesensor.util.IdUtils;
import io.pivotal.iot.mobilesensor.util.KeyValuePair;
import io.pivotal.iot.mobilesensor.util.MapUtils;
import jdk.nashorn.internal.runtime.JSONFunctions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.Map;

/**
 * @author sgupta
 * @since 6/29/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MobileSensorApplication.class)
@WebIntegrationTest
public class IngestControllerTests {

  TestRestTemplate restTemplate = new TestRestTemplate();
  private final Object LOCK = new Object();

  @Autowired
  Datastore datastore;

  @Test
  public void putDataItem() {
    String id = IdUtils.newId();
    ResponseEntity<GenericStatusResponse> responseBody =
        restTemplate.postForEntity("http://localhost:8080/device",
                                   new CreateDeviceRequest(new Device(id, DeviceType.IOS)),
                                   GenericStatusResponse.class,
                                   (Object) null);
    assert responseBody.getStatusCode() == HttpStatus.OK;
    assert responseBody.getBody().getData() instanceof Map;
    System.out.println("responseBody.getBody() = " + responseBody.getBody());
    assert ((Map) responseBody.getBody().getData()).get("deviceId").equals(id);

    EventItem eventItem =
        new EventItem(id,
                      new SensorDataItem("location",
                                         System.currentTimeMillis(),
                                         MapUtils.createMap(new KeyValuePair<>("lat", 40.750186),
                                                            new KeyValuePair<>("lon", -73.987683))),
                      new SensorDataItem("gyroscope",
                                         System.currentTimeMillis(),
                                         MapUtils.createMap(new KeyValuePair<>("pitch", 0.1),
                                                            new KeyValuePair<>("roll", 0.23),
                                                            new KeyValuePair<>("yaw", 0.11))));
    ResponseEntity<GenericStatusResponse> eventResponse =
        restTemplate.postForEntity("http://localhost:8080/event",
                                   eventItem,
                                   GenericStatusResponse.class,
                                   MapUtils.createMap(new KeyValuePair<>("deviceId", id)));

    assert eventResponse.getStatusCode() == HttpStatus.OK;
    String eventId = (String) ((Map)eventResponse.getBody().getData()).get("eventId");
    assert eventId != null;

    ResponseEntity<GenericStatusResponse> eventItemResponse =
        restTemplate.getForEntity("http://localhost:8080/event/" + eventId,
                                  GenericStatusResponse.class,
                                  new HashMap<String, Object>()
                                  /*MapUtils.createMap(new KeyValuePair<>("eventId", eventId))*/);

    System.out.println("eventItemResponse = " + eventItemResponse.getBody().getData());
  }
}
