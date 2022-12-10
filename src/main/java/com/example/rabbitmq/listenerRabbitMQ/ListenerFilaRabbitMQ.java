package com.example.rabbitmq.listenerRabbitMQ;

import com.example.rabbitmq.model.PagamentoDTO;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class ListenerFilaRabbitMQ {

    //--------------------------------------------------------------------------------
    /** Método que é chamado quando chega uma nova mensagem na fila do RabbitMQ */
    //--------------------------------------------------------------------------------
    @RabbitListener(queues = "fila.texto")
    public void recebeMensagemTexto(Message mensagem) {
        String conteudoMensagem = new String(mensagem.getBody()) ;
        System.out.println("Recebi do RabbitMQ: " + conteudoMensagem);
    }

    //--------------------------------------------------------------------------------
    /** Método que é chamado quando chega uma nova mensagem na fila do RabbitMQ */
    //--------------------------------------------------------------------------------
    @RabbitListener(queues = "fila.pagamento")
    public void recebeMensagemJson(PagamentoDTO pagamento) {
        String conteudoMensagem = """
                Id do pagamento: %d
                Descrição do pagamento: %s
                Valor do pagamento: %s 
                """ .formatted(pagamento.getId(), pagamento.getDescricao(), pagamento.getValor()) ;
        System.out.println("Recebi do RabbitMQ: " + conteudoMensagem);

        // Se levantar exceção será feita nova tentativa de processar a mensagem.
        // Após um certo número de tentativas (application.properties) a mensagem será encaminhada
        // para a Dead Letter Queue
        //throw new RuntimeException("Forçando erro no processamento da mensagem");
    }
}
