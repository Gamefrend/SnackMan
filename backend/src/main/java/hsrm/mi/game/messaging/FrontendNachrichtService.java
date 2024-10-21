package hsrm.mi.game.messaging;

import hsrm.mi.game.messaging.FrontendNachrichtEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class FrontendNachrichtService {

    Logger logger = LoggerFactory.getLogger(FrontendNachrichtService.class);
    private SimpMessagingTemplate template;

    public FrontendNachrichtService(SimpMessagingTemplate templ){
        this.template = templ;
    }

    public void sendEvent(FrontendNachrichtEvent event){
        template.convertAndSend("/topic/playerPosition",event);
        logger.info("Event gesendet: ",event);
    }
}
