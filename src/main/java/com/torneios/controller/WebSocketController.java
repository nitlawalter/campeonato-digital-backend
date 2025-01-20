package com.torneios.controller;

import com.torneios.dto.MensagemDTO;
import com.torneios.model.enums.TipoMensagem;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import java.security.Principal;

@Controller
@RequiredArgsConstructor
@Tag(name = "WebSocket", description = "Endpoints WebSocket para comunicação em tempo real")
public class WebSocketController {

    @Operation(summary = "Enviar mensagem",
            description = "Envia uma mensagem para todos os clientes conectados ao tópico /topic/mensagens")
    @Parameter(name = "mensagem", 
              description = "Mensagem a ser enviada",
              required = true,
              content = @Content(schema = @Schema(implementation = MensagemDTO.class)))
    @MessageMapping("/mensagem")
    @SendTo("/topic/mensagens")
    public MensagemDTO processarMensagem(MensagemDTO mensagem) {
        return mensagem;
    }

    @Operation(summary = "Verificar status",
            description = "Verifica o status da conexão WebSocket do usuário")
    @Parameter(name = "principal", 
              description = "Informações do usuário autenticado",
              required = true)
    @MessageMapping("/status")
    @SendTo("/topic/status")
    public MensagemDTO getStatus(Principal principal) {
        return new MensagemDTO(
            TipoMensagem.SISTEMA,
            "Conexão ativa para usuário: " + principal.getName(),
            null,
            null,
            null
        );
    }
} 