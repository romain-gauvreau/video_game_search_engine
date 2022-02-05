package fr.lernejo.fileinjector;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.ietf.jgss.MessageProp;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

@SpringBootApplication
public class Launcher {

    private static final String GAME_INFO_QUEUE = "game_info";

    public static void main(String[] args) throws IOException {
        try (AbstractApplicationContext springContext = new AnnotationConfigApplicationContext(Launcher.class)) {
            if (args.length == 1) {
                ObjectMapper objectMapper = new ObjectMapper();
                List<Map<String, Object>> games = objectMapper
                    .readValue(Paths.get(args[0]).toFile(), new TypeReference<>() {
                    });

                RabbitTemplate rabbitTemplate = springContext.getBean(RabbitTemplate.class);
                rabbitTemplate.setRoutingKey(GAME_INFO_QUEUE);

                for (Map<?, ?> game : games) {
                    String jsonObjectMapper = new ObjectMapper().writeValueAsString(game);
                    MessageBuilder messageBuilder = MessageBuilder
                        .withBody(jsonObjectMapper.getBytes(StandardCharsets.UTF_8));
                    Message message = messageBuilder
                        .setContentType(MessageProperties.CONTENT_TYPE_JSON)
                        .setHeader("game_id", game.get("id"))
                        .build();
                    rabbitTemplate.convertAndSend(message);
                }
            }
        }
    }
}
