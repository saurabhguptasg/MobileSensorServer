package io.pivotal.iot.mobilesensor;

import com.amazonaws.services.simpledb.AmazonSimpleDB;
import com.amazonaws.services.simpledb.model.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * @author sgupta
 * @since 6/29/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MobileSensorApplication.class)
public class ListAllDevices {
  @Autowired
  AmazonSimpleDB simpleDB;

  @Test
  public void list() {
    SelectResult result = simpleDB.select(new SelectRequest("select * from devices"));
    List<Item> items = result.getItems();
    for (Item item : items) {
      System.out.println("item = " + item);
      simpleDB.deleteAttributes(new DeleteAttributesRequest("devices", item.getName()));
    }
  }
}
