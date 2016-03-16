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

/**
 * 
 * This class follow the comments of the item <h1>Fat jars and fat wars</h1> find it on the reading<br/>
 * http://camel.apache.org/spring-boot.html.<br/>
 * 
 * 
 * At the moment to execute <h1>mvn spring-boot:run</h1> only this class is execute {@link MySpringBootRouterWarInitializer} is only <br/>
 * to create a war file.<br/>
 * 
 * Recommendation reading
 * 
 * http://camel.apache.org/spring-boot.html : explain how spring-boot works with camel.<br/>
 * http://examples.javacodegeeks.com/enterprise-java/apache-camel/apache-camel-timer-example/  : the timer code was taken from this link.<br/>
 * */
@SpringBootApplication
public class MySpringBootRouter extends FatJarRouter {
    
    
    @Override
    public void configure() {
         from("rabbitmq://185.14.186.49:5672/spring-boot-exchange?queue=spring-boot") 
        .transform(simple("${body}"))
        .to("stream:out");
    }
   
}
