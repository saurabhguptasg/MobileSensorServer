package io.pivotal.iot.mobilesensor;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;

@SpringBootApplication
@ComponentScan
@EnableAutoConfiguration
public class MobileSensorApplication {


  public static void main(String[] args) {
    SpringApplication.run(MobileSensorApplication.class, args);
  }

}
