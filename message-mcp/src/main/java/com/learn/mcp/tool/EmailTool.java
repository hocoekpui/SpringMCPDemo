package com.learn.mcp.tool;

import com.learn.service.McpTool;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.data.MutableDataSet;
import jakarta.annotation.Resource;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class EmailTool implements McpTool {

    @Resource
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    @Data
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EmailInfo {
        @ToolParam(description = "收件人邮箱")
        private String email;
        @ToolParam(description = "发送邮件的标题")
        private String subject;
        @ToolParam(description = "发送邮件的消息")
        private String content;
    }

    @Tool(description = "获取我的邮箱地址")
    public String getEmail() {
        return from;
    }

    @Tool(description = "给指定邮箱发送邮件信息")
    public void sendMail(EmailInfo emailInfo) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);

            helper.setFrom(from);
            helper.setTo(emailInfo.getEmail());
            helper.setSubject(emailInfo.getSubject());
            helper.setText(emailInfo.getContent());

            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            log.error("EmailTool#sendMail exception", e);
        }
    }

    public static String convertToHtml(String markdownContent) {
        MutableDataSet dataset = new MutableDataSet();
        Parser parser = Parser.builder(dataset).build();
        HtmlRenderer htmlRenderer = HtmlRenderer.builder(dataset).build();
        return htmlRenderer.render(parser.parse(markdownContent));
    }
}
