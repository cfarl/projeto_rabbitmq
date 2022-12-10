package com.example.rabbitmq.configuration;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.amqp.support.converter.SimpleMessageConverter;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ParamentoAMQPConfiguration {

    //---------------------------------------------------------
    /** Cria fila de nome "fila.texto" no RabbitMQ
     *  Definido apenas na aplicacao que envia mensagem */
    //---------------------------------------------------------
    @Bean
    public Queue criaFilaTexto(){
        return QueueBuilder.nonDurable("fila.texto").build();
    }

    //--------------------------------------------------------------------
    /** Cria fila de nome "fila.pagamento" no RabbitMQ
     *  Definido apenas no consumidor, se estivermos usando exchanges */
    //--------------------------------------------------------------------
    @Bean
    public Queue criaFilaJson(){
        return QueueBuilder
                .nonDurable("fila.pagamento")
                .deadLetterExchange("pagamentos.dlx") // Exchange que vai receber as mensagens não processadas (com erro)
                .build();
    }

    //-------------------------------------------------------------------------------------------------
    /** Recupera a referencia à uma exchange (fanout envia mensagem para vários consumidores/filas)
     *  Fanout: Quando enviamos uma mensagem para uma exchange desse tipo, ela vai ser enviada para
     *  todas as filas que estiverem ligadas a essa exchange. Ou seja, se existirem 30 filas ligadas
     *  a essa exchange, essas 30 filas receberão a mensagem. Para conectar uma fila a uma exchange
     *  é preciso criar um bind, que é uma relação (ou vínculo) entre uma fila e uma exchange. */
    //-------------------------------------------------------------------------------------------------
    @Bean
    public FanoutExchange fanoutExchange() {
        // return new FanoutExchange("pagamentos.ex"); // No lado de quem envia a mensagem
        return ExchangeBuilder.fanoutExchange("pagamentos.ex" ).build() ; // No lado de quem recebe a mensagem
    }

    //-------------------------------------------------------------
    /** Lado do consumidor: associa fila com o exchange */
    //-------------------------------------------------------------
    @Bean
    public Binding bindPagamentoPedido() {
        return BindingBuilder.bind(criaFilaJson())
                .to(fanoutExchange());
    }

    //---------------------------------------------------------------------------------------
    /** Definido na aplicacao que envia mensagem (também no consumidor se usar exchange) */
    //---------------------------------------------------------------------------------------
    @Bean
    public RabbitAdmin criaRabbitAdmin(ConnectionFactory conn) {
        return new RabbitAdmin(conn);
    }

    //---------------------------------------------------------------------------------------
    /** Definido na aplicacao que envia mensagem (também no consumidor se usar exchange) */
    //---------------------------------------------------------------------------------------
    @Bean
    public ApplicationListener<ApplicationReadyEvent> inicializaAdmin(RabbitAdmin rabbitAdmin){
        return event -> rabbitAdmin.initialize();
    }

    //---------------------------------------------------------------
    /** Conversor padrao para o RabbitTemplate */
//    //---------------------------------------------------------------
//    @Bean
//    public MessageConverter messageConverter() {
//        return new SimpleMessageConverter();
//    }

    //---------------------------------------------------------------
    /** Conversor de DTO para Json para o RabbitTemplate */
    //---------------------------------------------------------------
    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    //-----------------------------------------------------------------------------
    /** Retorna um RabbitTemplate preparado para converter um bean em Json */
    //-----------------------------------------------------------------------------
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter messageConverter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory) ;
        rabbitTemplate.setMessageConverter(messageConverter);
        return rabbitTemplate ;
    }
}
