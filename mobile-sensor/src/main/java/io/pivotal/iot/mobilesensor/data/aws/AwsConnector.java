package io.pivotal.iot.mobilesensor.data.aws;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.services.simpledb.AmazonSimpleDB;
import com.amazonaws.services.simpledb.AmazonSimpleDBClient;
import com.amazonaws.services.simpledb.model.*;
import com.amazonaws.util.StringInputStream;
import io.pivotal.iot.mobilesensor.data.Datastore;
import io.pivotal.iot.mobilesensor.model.Device;
import io.pivotal.iot.mobilesensor.model.DeviceType;
import io.pivotal.iot.mobilesensor.model.EventItemList;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * @author sgupta
 * @since 6/26/15.
 */
@Component
@Profile("aws")
@PropertySource("classpath:application-aws.properties")
public class AwsConnector implements Datastore {
  public static final Logger LOGGER = Logger.getLogger(AwsConnector.class.getName());

  public static final String DEVICE_DOMAIN = "devices";
  public static final String DEVICE_DATA_TABLE = "MobileSensorData";
  public static final String DEVICE_INFO_TABLE = "MobileSensorDeviceInfo";
  public static final Charset CHARSET = Charset.forName("UTF-8");

  @Value("${cloud.aws.access.key}")
  String accessKey;

  @Value("${cloud.aws.secret.key}")
  String secretKey;

  @Value("${cloud.aws.bucket.name}")
  String bucketName;

  private AWSCredentials credentials;
  private AmazonS3 amazonS3;
  private AmazonSimpleDB simpleDB;

  private Executor executor;


  @Bean
  public AmazonS3 getAmazonS3() {
    return amazonS3;
  }

  @Bean
  public AmazonSimpleDB getSimpleDB() {
    return simpleDB;
  }

  @PostConstruct
  void initialize() throws InterruptedException {
    System.out.println("executing !offline post construct");
    for (Map.Entry<String, String> entry : System.getenv().entrySet()) {
      LOGGER.warning("---->>>> env["+entry.getKey()+"]: "+entry.getValue()+"");
    }
    credentials = new BasicAWSCredentials(accessKey, secretKey);
    amazonS3 = new AmazonS3Client(credentials);
    if(!amazonS3.doesBucketExist(bucketName)) {
      amazonS3.createBucket(bucketName);
      LOGGER.info("bucket ["+bucketName+"] created");
    }
    else {
      LOGGER.info("bucket ["+bucketName+"] exists");
    }
    simpleDB = new AmazonSimpleDBClient(credentials);
    simpleDB.createDomain(new CreateDomainRequest(DEVICE_DOMAIN));

    executor = Executors.newSingleThreadExecutor();
  }

  @Override
  public void putDevice(Device device) {
    List<ReplaceableAttribute> attributes = new ArrayList<>();
    attributes.add(new ReplaceableAttribute("deviceType",
                                            device.getDeviceType().name(),
                                            Boolean.TRUE));
    attributes.add(new ReplaceableAttribute("timestamp",
                                            Long.toString(System.currentTimeMillis()),
                                            Boolean.TRUE));
    simpleDB.putAttributes(new PutAttributesRequest(DEVICE_DOMAIN, device.getDeviceId(), attributes));
  }

  @Override
  public Device getDevice(String deviceId) {
    GetAttributesResult result = simpleDB.getAttributes(new GetAttributesRequest(DEVICE_DOMAIN, deviceId));
    List<Attribute> attributes = result.getAttributes();
    Device device = null;
    for (Attribute attribute : attributes) {
      if(attribute.getName().equals("deviceType")) {
        DeviceType type = DeviceType.valueOf(attribute.getValue());
        device = new Device(deviceId, type);
        break;
      }
    }
    return device;
  }

  @Override
  public void deleteDevice(String deviceId) {
    simpleDB.deleteAttributes(new DeleteAttributesRequest(DEVICE_DOMAIN, deviceId));
  }

  @Override
  public void putEvent(String eventId, String jsonString) {
    executor.execute(() -> amazonS3.putObject(new PutObjectRequest(bucketName,
                                            eventId,
                                            new ByteArrayInputStream(jsonString.getBytes(CHARSET)),
                                            null)));
  }

  @Override
  public String getEvent(String eventId) throws IOException {
    S3Object object = amazonS3.getObject(new GetObjectRequest(bucketName, eventId));
    if(object == null) {
      return null;
    }
    else {

      S3ObjectInputStream inputStream = object.getObjectContent();
      StringBuilder builder = new StringBuilder();
      byte[] buffer = new byte[1024];
      int count = -1;

      while ((count = inputStream.read(buffer)) >= 0) {
        builder.append(new String(buffer, 0, count));
      }
      return builder.toString();
    }
  }

  @Override
  public EventItemList getEventIdList(String deviceId, String prefix, int count, String token) {
    String keyPrefix = prefix == null ? deviceId + "/" : deviceId + "/" + prefix;
    ObjectListing listing =
        amazonS3.listObjects(new ListObjectsRequest(bucketName, keyPrefix, token, "/", count));
    List<String> eventIds =
        listing.getObjectSummaries().stream().map(S3ObjectSummary::getKey).collect(Collectors.toCollection(LinkedList::new));
    return new EventItemList(deviceId, prefix, eventIds, token);
  }

  @Override
  public EventItemList getByPrefix(String prefix, int count, String token) {
    if(prefix == null) {
      prefix = "";
    }
    ObjectListing listing =
        amazonS3.listObjects(new ListObjectsRequest(bucketName, prefix, token, "/", count));
    if(listing.getObjectSummaries().size() == 0) {
      List<String> prefixes = listing.getCommonPrefixes();
      return new EventItemList(null, prefix, prefixes, listing.getNextMarker());
    }
    else {
      List<String> eventIds =
          listing.getObjectSummaries().stream().map(S3ObjectSummary::getKey).collect(Collectors.toCollection(LinkedList::new));
      return new EventItemList(null, prefix, eventIds, listing.getNextMarker());
    }
  }
}
