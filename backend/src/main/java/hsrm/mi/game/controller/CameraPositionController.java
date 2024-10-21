package hsrm.mi.game.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import hsrm.mi.game.messaging.FrontendNachrichtEvent;

@Controller
public class CameraPositionController {

    @MessageMapping("/send-position") // Empfängt Nachrichten von '/app/send-position'
    @SendTo("/topic/tour")            // Sendet die Nachrichten an '/topic/tour'
    public FrontendNachrichtEvent broadcastPosition(FrontendNachrichtEvent event) {
        // Logik, um die Nachricht an alle zu verteilen, kann hier hinzugefügt werden
        return event;
    }
}