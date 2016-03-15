package messaging;


import javax.swing.Spring;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;

/**
 * Esta clase tiene como objectivo definir la configuracion de los beans que se comunicaran con<br/>
 * un servidor de mensajeria RabbitMQ.Especialemente para el caso de un productor de mensajes.<br/>
 * <br/>
 * Esta clase es utilizada para  realizar un ejemplo del patron de integraci&oacute;n empresarial<br/>
 * <br/>
 * <h1>Message Channel</h1>  
 * <br/>
 * SpringBootApplication : indica cual es la clase que contiene la configuracion del proyecto,<br/>
 *                         la clase marcada con esta anotacion contendra los beans y triggers a<br/>
 *                         utilizar en el proyecto. <br/>
 *                         <br/>
 *                         Esta anotaci&oacute;n es equivalente a usar @Configuracion, @EnableAutoConfiguracion<br/>
 *                         y @ComponentScan unidas(Si se observa la documentacion se ve que en las meta<br/> 
 *                         anotaciones esta las tres definidas).<br/>
 *                         <br/>
 *                         
 * CommandLineRunner :  Interface utilizada para realizar tareas una vez los bean are sido instanciados y el<br/>
 *                      contexto creado. EL lugar para realizar las tareas es en el metodo run que le interface<br/>
 *                      declara.<br/>
 *                      <br/>  
 *                      
 *@author  Carlos Andr&eacute;s Garc&iacute:a Garc&iacute:a 
* @version 1.0
* @since   2016-03-15     
* @see http://www.enterpriseintegrationpatterns.com/patterns/messaging/MessageChannel.html                                       
 * */
@SpringBootApplication
public class Application implements CommandLineRunner {

	
	//Define el nombre de la cola donde se enviara el mensaje.
	//Se declara final para evitar que el nombre sea modificado
	final static String queueName = "spring-boot";

	//Se obtiene el contexto de Spring, este contexto  fue creado anteriormente
	//con la anotacion @SpringBootApplication
	@Autowired
	AnnotationConfigApplicationContext context;

	//Se obtiene la plantilla predefinida por Spring para la comunicacion con 
	//RabbitMq. Esta plantilla es obtenida del contexto de Spring que ya fue creado
	//con anterioridad. 
	@Autowired
	RabbitTemplate rabbitTemplate;

	/**
	 * 
	 * Define una cola y la ingresa al contexto de Spring.<br/> 
	 * 
	 * @Bean Permite crear un bean y a&nacute;adirlo al contexto de Spring, para que luego pueda<br/>
	 *       ser utilizado por otros metodos. Esta definici&oacute;n reemplaza la definici&oacute;n de beans por<br/> 
	 *       Spring XML.<br/>
	 */
	@Bean
	Queue queue() {
		// Queue permite definir la cola a la cual nos vamos a comunicar para enviar los mensajes.
		// Si se da el caso que la cola no existe, Queue crea una cola con el nombre que le hemos
		// indicado en queueName.
		// El atributo false indica que la cola NO va a ser durable, es 
		// decir que si el servidor es reiniciado y los mensajes no son consumidos, estos 
		// se perderan. Si para efectos de pruebas este valor cambia a true, indicara que la
		// cola es durable y los valores persistiran si los servidores de RabbitMQ son
		// reiniciados
		return new Queue(queueName, false);
	}

	/**
	 * Metodo  que define el metodo de intercambio de mensajes con RabbitMQ, para este.<br/>
	 * caso se crear un Topic.Esta definici&oacute;n reemplaza la definici&oacute;n de beans por<br/> 
	 * Spring XML.<br/> 
	 * */  
	@Bean
	TopicExchange exchange() {
		return new TopicExchange("spring-boot-exchange");
	}

	/**
	 * Metodo que toma el Destino(pQueue) y el metodo de intercambio(pExchange) para facilitar<br/>
	 * la comunicacion entre este proyecto y el servidor RabbitMQ.<br/>
	 * Esta definici&oacute;n reemplaza la definici&oacute;n de beans por<br/> 
	 * Spring XML. 
	 * 
	 * @param pQueue contine  el nombre de la cola con la cual nos vamos a comunicar.
	 * @param pExchange contiene el metodo de intercambio de mensajes que se utilizara
	 *                  con el servidor. 
	 * */  
	@Bean
	Binding binding(Queue pQueue, TopicExchange pExchange) {
		return BindingBuilder.bind(pQueue).to(pExchange).with(queueName);
	}

	/**
	 * Es el punto de entrada de la aplicacion y  por medio del parametro <br/>
	 * Application.class le indica a Spring que tome la configuracion de la clase<br/>
	 * Application y cree el contexto y los beans definidos en esta clase.<br/>
	 * 
	 * En pocas palabras es la clase que hace hace el llamado a todos las anotaciones<br/>
	 * para poder configurar el proyecto.<br/>
	 * 
	 * @param pArgs parametros que pueder ser pasados por linea de comandos, pero para esta clase estaran<br/
	 *        vacios. Si estos  valores fueran pasados al momento de ejecutar esta clase, dichos parametros<br/
	 *        pasan automaticamente al metodo run de la clase {@link CommandLineRunner} que se<br/
	 *        encuentra al final de esta clase.<br/
	 * 
	 * <br/>
	 * @throws InterruptedException en caso de no poder instanciar la clase Application.class.<br/>
	 * <br/>       
	 * */
    public static void main(final String[] pArgs) throws InterruptedException {
        SpringApplication.run(Application.class, pArgs);
    }
    
    /**
     * Metodo que enviara un mensaje de tipo {@link Spring} a una cola de<br/> 
     * mensajeria RabbitMQ.<br/>
     * 
     * @param pArgs : definido con el metodo Run pero no es utilizado, durante la ejecuci&iacute;n<br/>
     *               de este c&oacute;digo. Los posibles valores que pueda tener este atributo<br/>
     *               seran los que sean pasados por el metodo main cuando ejecuta SpringApplication.run .<br/>
     *               
     * @throws Exception : En caso de generarse un error al momento de ejecutarse este metodo.<br/>
     *                     Una posible excepcion es que no se encuentre el servidor RabbitMQ, lo cual <br/>
     *                     generara un Refused Connection.<br/>
     * */
    @Override
    public void run(final String... pArgs) throws Exception {
        System.out.println("Waiting five seconds...");
        Thread.sleep(5000);
        
        System.out.println("Sending message...");
        rabbitTemplate.convertAndSend(queueName, "Hello from RabbitMQ again!");
        // se elimina el contexto de Spring de la memoria
        context.close();
    }
}
