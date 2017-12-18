package com.autoban.conectCarConsumer;

import com.autoban.helper.ConsumerImpl;
import com.autoban.helper.GetProperties;
import com.autoban.helper.JmsConnectionFactory;
import com.autoban.helper.JmsServerConnection;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.WebApplicationInitializer;

import javax.jms.ConnectionFactory;
import java.util.HashMap;

@ComponentScan
@SpringBootApplication
public class ConectCarConsumerApplication extends SpringBootServletInitializer implements WebApplicationInitializer {

	private static JmsConnectionFactory jmsConnectionFactory = null;
	private static JmsServerConnection jmsServerConnection = null;
	private static HashMap<String, JmsServerConnection> jmsServerProperites = GetProperties.getServerProperties();
	private static HashMap<String, JmsConnectionFactory> jmsProperites = GetProperties.getProperties();
	private static ConnectionFactory connectionFactory = null;
	private static ConsumerImpl consumer = null;
	private static String username = null;
	private static String password = null;
	private static String jmsConnectionFactoryContext = null;

	public static  void init(){
		ConsumerImpl consumer = new ConsumerImpl();
		jmsServerConnection = jmsServerProperites.get("OSA3");
		username = jmsProperites.get("PassagemProcessadaRemoteOSA31012").getUsername();
		password = jmsProperites.get("PassagemProcessadaRemoteOSA31012").getPassword();
		jmsConnectionFactoryContext = jmsProperites.get("PassagemProcessadaRemoteOSA31012").getJmsFactory();
		connectionFactory = consumer.connectionFactory(jmsServerConnection, username, password, jmsConnectionFactoryContext);
	}

	public static void main(String[] args) {
		SpringApplication.run(ConectCarConsumerApplication.class, args);
		init();
		Runnable passagemProcessadaConsumer = () -> {
				jmsConnectionFactory = jmsProperites.get("PassagemProcessadaRemoteOSA31012");
				consumer.startConnectionFactory("PassagemProcessadaRemoteOSA31012", jmsConnectionFactory,connectionFactory);
		};
		Runnable requisitaImagemConsumer = () -> {
				jmsConnectionFactory = jmsProperites.get("RequisitaImagemRemoteOSA31012");
				consumer.startConnectionFactory("RequisitaImagemRemoteOSA31012", jmsConnectionFactory,connectionFactory);
		};
		Runnable falhaConsumer = () -> {
				jmsConnectionFactory = jmsProperites.get("FalhaComunicacaoRemoteOSA31012");
				consumer.startConnectionFactory("FalhaComunicacaoRemoteOSA31012", jmsConnectionFactory,connectionFactory);
		};

		passagemProcessadaConsumer.run();
		requisitaImagemConsumer.run();
		falhaConsumer.run();
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(ConectCarConsumerApplication.class);
	}
}