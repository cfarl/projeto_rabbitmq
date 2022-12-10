package com.example.rabbitmq.controller;

import com.example.rabbitmq.model.PagamentoDTO;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
public class TesteController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    //--------------------------------------------------------------------------------------
    /** Envia uma mensagem de texto para o RabbitMQ.
     *  Precisa estar com o SimpleMessageConverter habilitado na classe de configuracao
     */
    //--------------------------------------------------------------------------------------
    @GetMapping("/enviarMensagemTexto")
    public String enviarMensagemTexto() {
        String conteudoMensagem = "Enviando mensagem para o RabbitMQ em "
                + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyy hh:mm:ss"))  ;
        Message message = new Message(conteudoMensagem.getBytes()) ;
        rabbitTemplate.send("fila.texto", message);
        return conteudoMensagem ;
    }

    //------------------------------------------------------------------------------------------
    /** Envia um JSON para o RabbitMQ
     *  Precisa estar com o Jackson2JsonMessageConverter habilitado na classe de configuracao
     */
    //------------------------------------------------------------------------------------------
    @PostMapping("/enviarMensagemJson")
    public void enviarMensagemJson(@RequestBody PagamentoDTO pagamentoDTO) {
        // Encaminha mensagem para uma fila
        //rabbitTemplate.convertAndSend("fila.pagamento", pagamentoDTO);

        // Encaminha mensagem para o exchange de pagamento
        // Todas as filas que foram associadas a esse exchange receberão a mensagem
        rabbitTemplate.convertAndSend("pagamentos.ex", "", pagamentoDTO);
    }

}
