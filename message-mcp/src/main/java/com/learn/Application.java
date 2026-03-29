package com.learn;

import com.learn.service.McpTool;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
        dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public ToolCallbackProvider registerMCPTools(List<McpTool> tools) {
        return MethodToolCallbackProvider.builder().toolObjects(tools.toArray()).build();
    }
}