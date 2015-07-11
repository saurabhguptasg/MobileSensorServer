package io.pivotal.iot.mobilesensor;

import io.pivotal.iot.mobilesensor.data.Datastore;
import io.pivotal.iot.mobilesensor.model.Device;
import io.pivotal.iot.mobilesensor.model.DeviceType;
import io.pivotal.iot.mobilesensor.msg.CreateDeviceRequest;
import io.pivotal.iot.mobilesensor.msg.GenericStatusResponse;
import io.pivotal.iot.mobilesensor.util.IdUtils;
import io.pivotal.iot.mobilesensor.util.KeyValuePair;
import io.pivotal.iot.mobilesensor.util.MapUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.ResponseErrorHandler;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Map;

/**
 * @author sgupta
 * @since 6/29/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MobileSensorApplication.class)
@WebIntegrationTest
public class DeviceControllerTests {

  TestRestTemplate restTemplate = new TestRestTemplate();
  private final Object LOCK = new Object();

  @Autowired
  Datastore datastore;

  @PostConstruct
  public void initialize() {
    restTemplate.setErrorHandler(new ResponseErrorHandler() {
      @Override
      public boolean hasError(ClientHttpResponse clientHttpResponse) throws IOException {
        System.out.println("hasError: clientHttpResponse = " + clientHttpResponse);
        return false;
      }

      @Override
      public void handleError(ClientHttpResponse clientHttpResponse) throws IOException {
        System.out.println("handleError: clientHttpResponse = " + clientHttpResponse);
      }
    });
  }


  @Test
  public void testPutDevice() {
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


    synchronized (LOCK) {
      try {
        LOCK.wait(2500);
      }
      catch (InterruptedException e) {
        e.printStackTrace();
      }
    }

    responseBody =
        restTemplate.getForEntity("http://localhost:8080/device/{deviceId}",
                                  GenericStatusResponse.class,
                                  MapUtils.createMap(new KeyValuePair<>("deviceId", id)));
    assert responseBody.getStatusCode() == HttpStatus.OK;
    assert responseBody.getBody().getData() instanceof Map;
    System.out.println("responseBody.getBody() = " + responseBody.getBody());
    assert ((Map) responseBody.getBody().getData()).get("deviceId").equals(id);
    assert ((Map) responseBody.getBody().getData()).get("deviceType").equals(DeviceType.IOS.name());

    restTemplate.delete("http://localhost:8080/device/{deviceId}",
                        MapUtils.createMap(new KeyValuePair<>("deviceId", id)));

    synchronized (LOCK) {
      try {
        LOCK.wait(5000);
      }
      catch (InterruptedException e) {
        e.printStackTrace();
      }
    }


    ResponseEntity<GenericStatusResponse> responseEntity =
        restTemplate.getForEntity("http://localhost:8080/device/{deviceId}",
                                  GenericStatusResponse.class,
                                  MapUtils.createMap(new KeyValuePair<>("deviceId", id)));
    System.out.println("---> --> responseEntity.getBody() = " + responseEntity.getBody());
    assert responseEntity.getStatusCode() == HttpStatus.NOT_FOUND;
  }

}
