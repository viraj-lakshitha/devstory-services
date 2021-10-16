package lk.devstory.devstory.utils;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class MailUtils {

    @Value("${sendgrid.api-key}")
    private String apiKey;

    @Value("${sendgrid.sender}")
    private String sender;

    @Value("${sendgrid.sender-name}")
    private String senderName;

    @Value("${sendgrid.template-id}")
    private String templateId;

    @Value("${sendgrid.base-link}")
    private String baseLink;

    // Initializing Email Service - Verification Email
    public void sendVerificationEmail(String email, String token) {
        log.info("Initializing to Send Verification Email");

        // Sender
        Email fromEmail = new Email(sender);
        fromEmail.setName(senderName);

        // Receiver
        Email toEmail = new Email(email);

        // Add DynamicTemplatePersonalization
        DynamicTemplatePersonalization dynamicTemplatePersonalization = new DynamicTemplatePersonalization();
        dynamicTemplatePersonalization.addTo(toEmail);
        dynamicTemplatePersonalization.addDynamicTemplateData("username", email);
        dynamicTemplatePersonalization.addDynamicTemplateData("verification_link",baseLink+token);

        // Setting up Email
        Mail mail = new Mail();
        mail.setFrom(fromEmail);
        mail.addPersonalization(dynamicTemplatePersonalization);
        mail.setTemplateId(templateId);

        // Send Email
        sendEmail(mail);
        log.info("Complete Send Verification Email");

    }

    // Send Email Service
    private void sendEmail(Mail mail) {
        SendGrid sendGrid = new SendGrid(apiKey);
        Request request = new Request();

        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());

            Response response = sendGrid.api(request);
            log.info(String.valueOf(response.getStatusCode()));
            log.info(response.getBody());
            log.info(String.valueOf(response.getHeaders()));
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    // Implement Dynamic Template with Custom Key
    private static class DynamicTemplatePersonalization extends Personalization {

        @JsonProperty(value = "dynamic_template_data")
        private Map<String, Object> dynamic_template_data;

        @JsonProperty("dynamic_template_data")
        public Map<String, Object> getDynamicTemplateData() {
            if (dynamic_template_data == null) {
                return Collections.emptyMap();
            }
            return dynamic_template_data;
        }

        public void addDynamicTemplateData(String key, Object value) {
            if (dynamic_template_data == null) {
                dynamic_template_data = new HashMap<>();
                dynamic_template_data.put(key, value);
            } else {
                dynamic_template_data.put(key, value);
            }
        }
    }

}
