package io.pivotal.iot.mobilesensor;

import io.pivotal.iot.mobilesensor.util.IdUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author sgupta
 * @since 6/26/15.
 */
public class IdUtilsTest {

  @Test
  public void testIdUtils() {
    String id = IdUtils.newId();
    System.out.println("idutils id = " + id);
    assert id != null;
    assert id.length() >= 16;
  }

  @Test
  public void testTimestampIdUtils() {
    Long curr = 1435591662997L;
    System.out.println("curr = " + curr);
    String tsId = IdUtils.delimitedIdFromTimestamp(curr);
    System.out.println("tsId = " + tsId);
    assert tsId.equals("2015/06/29/15/27/42.997");
  }
}
