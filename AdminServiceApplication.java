package com.janaushadhi.adminservice;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableFeignClients
@ImportAutoConfiguration({FeignAutoConfiguration.class})
@EnableDiscoveryClient
@SpringBootApplication
@EnableWebMvc
public class AdminServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AdminServiceApplication.class, args);
	}

}
