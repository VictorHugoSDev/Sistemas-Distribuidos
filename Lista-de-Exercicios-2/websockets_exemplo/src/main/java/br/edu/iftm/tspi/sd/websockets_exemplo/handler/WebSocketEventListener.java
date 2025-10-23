package br.edu.iftm.tspi.sd.websockets_exemplo.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent; // IMPORT CORRETO

import java.time.Instant;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Component
public class WebSocketEventListener {
  private final Set<String> usuariosOnline = Collections.synchronizedSet(new HashSet<>());

  @Autowired
  private SimpMessageSendingOperations messagingTemplate;

  public void usuarioEntrou(String username) {
    if (usuariosOnline.add(username)) {
      enviarListaDeUsuarios();
    }
  }

  public void usuarioSaiu(String username) {
    if (usuariosOnline.remove(username)) {
      enviarListaDeUsuarios();
    }
  }

  public void enviarListaDeUsuarios() {
    this.messagingTemplate.convertAndSend("/topic/online", this.usuariosOnline);
  }

  @EventListener
  public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
    StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

    String username = (String) headerAccessor.getSessionAttributes().get("username");

    if (username != null) {

      if (usuariosOnline.remove(username)) {

        enviarListaDeUsuarios();

        Mensagem mensagemSaida = new Mensagem();
        mensagemSaida.setTipoMensagem(TipoMensagem.SAIR);
        mensagemSaida.setDataHora(Instant.now());

        mensagemSaida.setOrigem(username);
        mensagemSaida.setTexto(username + " saiu (desconexão inesperada)");

        messagingTemplate.convertAndSend("/topic/public", mensagemSaida);
      }
    }
  }
}