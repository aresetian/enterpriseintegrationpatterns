/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.camel.example.spring.boot;

import org.apache.camel.spring.boot.FatJarRouter;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

//example  http://examples.javacodegeeks.com/enterprise-java/apache-camel/apache-camel-timer-example/
@SpringBootApplication
public class MySpringBootRouter extends FatJarRouter {

	
	
	//http://camel.apache.org/timer.html
	//http://camel.apache.org/log.html
	//http://camel.apache.org/message-translator.html
	//http://camel.apache.org/simple.html
    @Override
    public void configure() {
    	//The timer component doesn’t receive any message, it only generates messages so the inbound message of the generated exchange is null.
    	//Thus the below statement returns null.
    	//exchange.getIn().getBody();
        from("timer:trigger") 
                .transform().simple("ref:myBean")
                .to("rabbitmq://185.14.186.49:5672/spring-boot");
        
        
      //  simple language for evaluating Expression and Predicate without requiring any new dependencies or knowledge of XPath
    }

    @Bean
    String myBean() {
        return "I'm Spring bean!";
    }

}
