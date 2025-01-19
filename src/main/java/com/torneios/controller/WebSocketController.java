package com.torneios.controller;

import com.torneios.dto.MensagemDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class WebSocketController {

    @MessageMapping("/mensagem")
    @SendTo("/topic/mensagens")
    public MensagemDTO processarMensagem(MensagemDTO mensagem) {
        return mensagem;
    }
} 