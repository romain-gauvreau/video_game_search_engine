package fr.lernejo.search.api;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;


public class GameInfoListenerTest {

    @Test
    void should_message_be_send() {
        try (AbstractApplicationContext springContext = new AnnotationConfigApplicationContext(Launcher.class)) {
            RabbitTemplate rabbitTemplate = springContext.getBean(RabbitTemplate.class);
            rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
            String game = """
                {
                             "id": 7,
                             "title": "Armored Warfare",
                             "thumbnail": "https:\\/\\/www.freetogame.com\\/g\\/7\\/thumbnail.jpg",
                             "short_description": "A modern team-based MMO tank game from Obsidian Entertainment.",
                             "game_url": "https:\\/\\/www.freetogame.com\\/open\\/armored-warfare",
                             "genre": "Shooter",
                             "platform": "PC (Windows)",
                             "publisher": "My.com (Mail.ru Group)",
                             "developer": "Obsidian Entertainment",
                             "release_date": "2015-10-08",
                             "freetogame_profile_url": "https:\\/\\/www.freetogame.com\\/armored-warfare"
                }""";
            rabbitTemplate.convertAndSend("", "game_info", game, m -> {
                m.getMessageProperties().getHeaders().put("game_id", "7");
                return m;
            });
        }
    }
}
