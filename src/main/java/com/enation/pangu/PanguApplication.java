package com.enation.pangu;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * 盘古application
 * @author kingapex
 * @version 1.0
 * @since 1.0.0
 * 2020/10/9
 */
@SpringBootApplication
@EnableAsync
@MapperScan("com.enation.pangu.mapper")
public class PanguApplication {

	public static void main(String[] args) {
		SpringApplication.run(PanguApplication.class, args);
	}

}
