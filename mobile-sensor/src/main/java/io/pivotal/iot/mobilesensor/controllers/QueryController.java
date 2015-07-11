package io.pivotal.iot.mobilesensor.controllers;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import io.pivotal.iot.mobilesensor.data.Datastore;
import io.pivotal.iot.mobilesensor.model.EventItemList;
import io.pivotal.iot.mobilesensor.msg.GenericStatusResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * @author sgupta
 * @since 6/26/15.
 */
@Controller
@RequestMapping(value = "/q/**", produces = "application/json")
public class QueryController {

  @Autowired
  Datastore datastore;

  @RequestMapping(value = "/**", method = RequestMethod.GET)
  @ResponseBody
  public ResponseEntity<EventItemList> getData(HttpServletRequest request,
                                               @RequestParam(value = "token", required = false)String token) {
    String path = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
    String prefix = path.substring("/q/".length());

    EventItemList list = datastore.getByPrefix(prefix, 100, token);
    return new ResponseEntity<>(list, HttpStatus.OK);
  }

}
