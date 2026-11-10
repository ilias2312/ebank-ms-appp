package ma.tp6.aiagent.telegram;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@Component
@ConditionalOnProperty(name = "telegram.bot.token")
public class TelegramBot implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(TelegramBot.class);

    private final ChatClient chatClient;
    private final RestClient http = RestClient.create();
    private final String apiUrl;

    public TelegramBot(ChatClient chatClient, @Value("${telegram.bot.token}") String token) {
        this.chatClient = chatClient;
        this.apiUrl = "https://api.telegram.org/bot" + token;
    }

    @Override
    public void run(String... args) {
        Thread thread = new Thread(this::poll, "telegram-poller");
        thread.setDaemon(true);
        thread.start();
        log.info("Telegram bot started (long polling)");
    }

    @SuppressWarnings("unchecked")
    private void poll() {
        long offset = 0;
        while (true) {
            try {
                Map<String, Object> response = http.get()
                        .uri(apiUrl + "/getUpdates?timeout=30&offset=" + offset)
                        .retrieve()
                        .body(new ParameterizedTypeReference<Map<String, Object>>() {});
                if (response == null) {
                    continue;
                }
                List<Map<String, Object>> updates = (List<Map<String, Object>>) response.get("result");
                if (updates == null) {
                    continue;
                }
                for (Map<String, Object> update : updates) {
                    offset = ((Number) update.get("update_id")).longValue() + 1;
                    handle(update);
                }
            } catch (Exception e) {
                log.warn("Telegram polling error: {}", e.getMessage());
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void handle(Map<String, Object> update) {
        Map<String, Object> message = (Map<String, Object>) update.get("message");
        if (message == null || message.get("text") == null) {
            return;
        }
        long chatId = ((Number) ((Map<String, Object>) message.get("chat")).get("id")).longValue();
        String text = (String) message.get("text");
        log.info("Telegram message received from chat {}", chatId);

        String answer;
        try {
            answer = chatClient.prompt().user(text).call().content();
        } catch (Exception e) {
            answer = "Sorry, an error occurred: " + e.getMessage();
        }
        if (answer == null || answer.isBlank()) {
            answer = "No answer.";
        }
        if (answer.length() > 4000) {
            answer = answer.substring(0, 4000);
        }
        http.post()
                .uri(apiUrl + "/sendMessage")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Map.of("chat_id", chatId, "text", answer))
                .retrieve()
                .toBodilessEntity();
    }
}
