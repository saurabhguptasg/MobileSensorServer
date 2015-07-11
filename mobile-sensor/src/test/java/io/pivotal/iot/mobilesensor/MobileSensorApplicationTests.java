package io.pivotal.iot.mobilesensor;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringApplicationConfiguration(classes = MobileSensorApplication.class)
//@WebAppConfiguration
@RunWith(Suite.class)
@Suite.SuiteClasses(value = {IdUtilsTest.class,
                             DeviceControllerTests.class,
                             ListAllDevices.class,
                             IngestControllerTests.class})
public class MobileSensorApplicationTests {

//	@Test
//	public void contextLoads() {
//	}

}
