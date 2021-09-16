package zzq.config;

import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;

@Configuration
public class FirstMqConfiguration {
    @Autowired
    @Qualifier("firstConnectionFactory")
    private ConnectionFactory connectionFactory;

    /**
     * https://docs.spring.io/spring-amqp/docs/2.1.7.RELEASE/reference/html/#_common_properties
     * 该链接有ConnectionFactory配置的系列属性
     *
     * @return
     */
    @Bean(name = "firstConnectionFactory")
    @ConfigurationProperties("spring.rabbitmq.first")
    @Primary
    public ConnectionFactory firstConnectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        MqConfigurationUtil.initMqConnectionFactory(connectionFactory);
        return connectionFactory;
    }

    @Bean(name = "firstRabbitTemplate")
    @Primary
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public RabbitTemplate firstRabbitTemplate(
            @Qualifier("firstConnectionFactory") ConnectionFactory connectionFactory
    ) {
        RabbitTemplate firstRabbitTemplate = new RabbitTemplate(connectionFactory);
        MqConfigurationUtil.initRabbitTemplate(firstRabbitTemplate);
        return firstRabbitTemplate;
    }

    @Bean(name = "firstFactory")
    public SimpleRabbitListenerContainerFactory firstFactory(
            SimpleRabbitListenerContainerFactoryConfigurer configurer,
            @Qualifier("firstConnectionFactory") ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        MqConfigurationUtil.initSimpleRabbitListenerContainerFactory(factory);
        configurer.configure(factory, connectionFactory);

        rabbitMqInit();
        return factory;
    }

    private void rabbitMqInit() {
        try {
            connectionFactory.createConnection().createChannel(false).queueDeclare("testFirst", true, false, false, null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
