package io.pivotal.iot.mobilesensor.controllers;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.document.*;
import io.pivotal.iot.mobilesensor.data.Datastore;
import io.pivotal.iot.mobilesensor.model.EventItem;
import io.pivotal.iot.mobilesensor.model.EventItemList;
import io.pivotal.iot.mobilesensor.msg.CreateDeviceRequest;
import io.pivotal.iot.mobilesensor.msg.GenericStatusResponse;
import io.pivotal.iot.mobilesensor.util.IdUtils;
import io.pivotal.iot.mobilesensor.util.KeyValuePair;
import io.pivotal.iot.mobilesensor.util.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * @author sgupta
 * @since 6/26/15.
 */
@Controller
@RequestMapping(value = "/event/**", produces = "application/json")
public class EventController {
  public static final Logger LOGGER = Logger.getLogger(EventController.class.getName());

  @Autowired
  Datastore datastore;


  @RequestMapping(value = "/**", method = RequestMethod.POST)
  @ResponseBody
  public ResponseEntity<GenericStatusResponse> putUpdate(@RequestHeader("x-mobilesensor-deviceId") String deviceId,
                                                         @RequestBody String jsonString) {
    LOGGER.info("deviceId = " + deviceId);
    LOGGER.info("jsonString = " + jsonString);
    String eventId = deviceId + "/" + IdUtils.delimitedIdFromTimestamp(System.currentTimeMillis());
    LOGGER.info("eventId = " + eventId);

    datastore.putEvent(eventId, jsonString);

    return new ResponseEntity<>(GenericStatusResponse.okWithData(MapUtils.createMap(new KeyValuePair<>("eventId", eventId))), HttpStatus.OK);
  }

  @RequestMapping(value = "/**", method = RequestMethod.GET)
  @ResponseBody
  public ResponseEntity<GenericStatusResponse> getEvent(HttpServletRequest request) {
    String path = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
    String eventId = path.substring("/event/".length());
    LOGGER.info(">>>>>>> >>>>>> >>>>> (1) eventId: " + eventId);
    try {
      String eventItem = datastore.getEvent(eventId);
      if(eventItem == null) {
        return new ResponseEntity<>(GenericStatusResponse.ERR,
                                    HttpStatus.NOT_FOUND);
      }
      else {
        return new ResponseEntity<>(GenericStatusResponse.okWithData(eventItem),
                                    HttpStatus.OK);
      }
    }
    catch (IOException e) {
      e.printStackTrace();
      return new ResponseEntity<>(GenericStatusResponse.errWithData(e.getLocalizedMessage()),
                                  HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @RequestMapping(value = "/list/**", method = RequestMethod.GET)
  @ResponseBody
  public ResponseEntity<GenericStatusResponse> getEventList(HttpServletRequest request) {
    String devicePath = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
    LOGGER.info(">>>>>>> >>>>>> >>>>> (2) devicePath = " + devicePath);
    return new ResponseEntity<>(GenericStatusResponse.OK, HttpStatus.OK);
  }
}
