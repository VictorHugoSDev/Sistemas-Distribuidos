package br.edu.iftm.tspi.sd.websockets_exemplo.handler;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    @Autowired
    private WebSocketEventListener presenceManager;

    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    @MessageMapping("/chat.send")
    @SendTo("/topic/public")
    public Mensagem enviarTexto(Mensagem mensagem) {
        mensagem.setTipoMensagem(TipoMensagem.ENVIAR_TEXTO);
        mensagem.setDataHora(Instant.now());
        return mensagem;
    }

    @MessageMapping("/chat.join")
    @SendTo("/topic/public")
    public Mensagem entrar(Mensagem mensagem, SimpMessageHeaderAccessor headerAccessor) {
        String user = mensagem.getOrigem();

        presenceManager.usuarioEntrou(user);

        headerAccessor.getSessionAttributes().put("username", user);

        mensagem.setTipoMensagem(TipoMensagem.ENTRAR);
        mensagem.setDataHora(Instant.now());
        mensagem.setTexto(user + " entrou");
        return mensagem;
    }

    @MessageMapping("/chat.leave")
    @SendTo("/topic/public")
    public Mensagem sair(Mensagem mensagem) {
        String user = mensagem.getOrigem();

        presenceManager.usuarioSaiu(user);

        mensagem.setOrigem(user);

        mensagem.setTipoMensagem(TipoMensagem.SAIR);
        mensagem.setDataHora(Instant.now());
        mensagem.setTexto(mensagem.getOrigem() + " saiu");
        return mensagem;
    }

    @MessageMapping("/chat.private")
    public void enviarPrivado(Mensagem mensagem, SimpMessageHeaderAccessor headerAccessor) {
        String remetente = (String) headerAccessor.getSessionAttributes().get("username");

        mensagem.setOrigem(remetente);

        if (mensagem.getDestino() == null || mensagem.getDestino().trim().isEmpty()) {
            return;
        }

        mensagem.setTipoMensagem(TipoMensagem.PRIVADO);
        mensagem.setDataHora(Instant.now());

        String destinoTopic = "/topic/dm." + mensagem.getDestino();
        messagingTemplate.convertAndSend(destinoTopic, mensagem);
    }
}
