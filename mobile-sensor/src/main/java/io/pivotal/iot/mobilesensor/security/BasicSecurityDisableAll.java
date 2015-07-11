package io.pivotal.iot.mobilesensor.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * @author sgupta
 * @since 6/29/15.
 */
@Configuration
@EnableWebSecurity
public class BasicSecurityDisableAll extends WebSecurityConfigurerAdapter {
  @Override
  public void configure(WebSecurity web) throws Exception {
    web.ignoring().anyRequest();
  }
}
