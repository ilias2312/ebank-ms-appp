package ma.tp6.aiagent.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatConfig {

    @Bean
    public ChatClient chatClient(ChatClient.Builder builder, ToolCallbackProvider mcpTools) {
        return builder
                .defaultSystem("""
                        You are the assistant of the e-bank application.
                        Use the available tools to read or create customers and bank accounts.
                        Never invent data: if a tool returns nothing, say so clearly.
                        Answer in the same language as the user.
                        """)
                .defaultToolCallbacks(mcpTools)
                .build();
    }
}
