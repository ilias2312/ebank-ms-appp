package ma.tp6.customerservice.mcp;

import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class McpConfig {
    @Bean
    public ToolCallbackProvider customerToolCallbacks(CustomerTools customerTools) {
        return MethodToolCallbackProvider.builder().toolObjects(customerTools).build();
    }
}
