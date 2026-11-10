package ma.tp6.aiagent.discord;

import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
@ConditionalOnProperty(name = "discord.bot.token")
public class DiscordBot implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DiscordBot.class);

    private final ChatClient chatClient;
    private final String token;
    private final ExecutorService executor = Executors.newCachedThreadPool();

    public DiscordBot(ChatClient chatClient, @Value("${discord.bot.token}") String token) {
        this.chatClient = chatClient;
        this.token = token;
    }

    @Override
    public void run(String... args) {
        JDABuilder.createLight(token, List.of(
                        GatewayIntent.GUILD_MESSAGES,
                        GatewayIntent.DIRECT_MESSAGES,
                        GatewayIntent.MESSAGE_CONTENT))
                .addEventListeners(new ListenerAdapter() {
                    @Override
                    public void onMessageReceived(MessageReceivedEvent event) {
                        handle(event);
                    }
                })
                .build();
        log.info("Discord bot started");
    }

    private void handle(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) {
            return;
        }
        boolean direct = event.getChannelType() == ChannelType.PRIVATE;
        boolean mentioned = event.getMessage().getMentions().isMentioned(event.getJDA().getSelfUser());
        if (!direct && !mentioned) {
            return;
        }
        String text = event.getMessage().getContentRaw().replaceAll("<@!?\\d+>", "").trim();
        if (text.isEmpty()) {
            return;
        }
        log.info("Discord message received");
        event.getChannel().sendTyping().queue();

        executor.submit(() -> {
            String answer;
            try {
                answer = chatClient.prompt().user(text).call().content();
            } catch (Exception e) {
                answer = "Sorry, an error occurred: " + e.getMessage();
            }
            if (answer == null || answer.isBlank()) {
                answer = "No answer.";
            }
            if (answer.length() > 1900) {
                answer = answer.substring(0, 1900);
            }
            event.getChannel().sendMessage(answer).queue();
        });
    }
}
