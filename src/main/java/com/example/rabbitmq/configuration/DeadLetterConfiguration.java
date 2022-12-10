package com.example.rabbitmq.configuration;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//-------------------------------------------------------------------------------------
/** Essa configuração deve estar no lado do consumidor de mensagems.
 *  Uma Dead Letter Queue é uma fila para onde serão direcionadas as mensagens
 *  que não puderam ser processadas.
 */
//-------------------------------------------------------------------------------------
@Configuration
public class DeadLetterConfiguration {

    //-------------------------------------------------------------------------------------------------
    /** Recupera a referencia à uma exchange. Essa exchange será associada á uma Dead Letter Queue. */
    //-------------------------------------------------------------------------------------------------
    @Bean
    public FanoutExchange deadLetterExchange() {
        return ExchangeBuilder.fanoutExchange("pagamentos.dlx" ).build() ; // No lado de quem recebe a mensagem
    }

    //--------------------------------------------------------------------
    /** Cria fila para as dlqs */
    //--------------------------------------------------------------------
    @Bean
    public Queue criaFilaDlq(){
        return QueueBuilder.nonDurable("fila.pagamento-dlq").build();
    }

    //-------------------------------------------------------------
    /** Associa fila com o exchange */
    //-------------------------------------------------------------
    @Bean
    public Binding bindDlxPagamentoPedido() {
        return BindingBuilder.bind(criaFilaDlq())
                .to(deadLetterExchange());
    }
}
