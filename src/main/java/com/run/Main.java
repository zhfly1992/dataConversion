/*
* File name: Main.java								
*
* Purpose:
*
* Functions used and called:	
* Name			Purpose
* ...			...
*
* Additional Information:
*
* Development History:
* Revision No.	Author		Date
* 1.0			guofeilong		2020年7月3日
* ...			...			...
*
***************************************************/

package com.run;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
* @Description:	
* @author: guofeilong
* @version: 1.0, 2020年7月3日
*/
@SpringBootApplication
@ImportResource("classpath:config/spring-context.xml")
@EnableScheduling
public class Main {
	public static void main(String[] args) throws Exception {
		SpringApplication.run(Main.class, args);
	}
}
