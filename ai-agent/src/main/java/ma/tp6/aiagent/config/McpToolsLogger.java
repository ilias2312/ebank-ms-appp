package ma.tp6.aiagent.config;

import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class McpToolsLogger {

    @Bean
    public CommandLineRunner logMcpTools(ToolCallbackProvider mcpTools) {
        return args -> {
            ToolCallback[] tools = mcpTools.getToolCallbacks();
            System.out.println("===== MCP tools discovered: " + tools.length + " =====");
            for (ToolCallback tool : tools) {
                System.out.println(" - " + tool.getToolDefinition().name());
            }
        };
    }
}
