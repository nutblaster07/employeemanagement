
package com.abhishek.service;

import com.abhishek.entity.Employee;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Service
public class EmailNotificationService {

    private static final Logger logger =
            Logger.getLogger(EmailNotificationService.class.getName());

    private final RestClient restClient;

    @Value("${SENDGRID_API_KEY:}")
    private String apiKey;

    @Value("${SENDGRID_FROM_EMAIL:}")
    private String fromEmail;

    public EmailNotificationService(RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder
                .baseUrl("https://api.sendgrid.com")
                .build();
    }

    public boolean sendInactiveNotification(Employee employee) {

        if (apiKey.isBlank() || fromEmail.isBlank()) {
            logger.warning(
                    "SendGrid is not configured. Email not sent for employee ID "
                            + employee.getId()
            );
            return false;
        }

        String recipientEmail = employee.getEmail();

        if (recipientEmail == null || recipientEmail.isBlank()) {
            logger.warning(
                    "Employee has no email address. Employee ID: "
                            + employee.getId()
            );
            return false;
        }

        String subject = "Employee Status Changed to Inactive";

        String message = """
                Hello %s,

                Your employee status has been changed to INACTIVE.

                Employee ID: %d
                Name: %s
                Department: %s
                Current Status: INACTIVE

                If you believe this change is incorrect, please contact HR.

                This is an automated notification.
                """.formatted(
                employee.getName(),
                employee.getId(),
                employee.getName(),
                employee.getDepartment()
        );

        Map<String, Object> requestBody = Map.of(
                "personalizations", List.of(
                        Map.of(
                                "to", List.of(
                                        Map.of("email", recipientEmail)
                                ),
                                "subject", subject
                        )
                ),
                "from", Map.of("email", fromEmail),
                "content", List.of(
                        Map.of(
                                "type", "text/plain",
                                "value", message
                        )
                )
        );

        try {
            restClient.post()
                    .uri("/v3/mail/send")
                    .header("Authorization", "Bearer " + apiKey)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(requestBody)
                    .retrieve()
                    .toBodilessEntity();

            logger.info(
                    "SendGrid accepted email for employee ID "
                            + employee.getId()
                            + " to " + recipientEmail
            );

            return true;

        } catch (RestClientException ex) {
            logger.warning(
                    "SendGrid email request failed for employee ID "
                            + employee.getId()
                            + ". Reason: " + ex.getMessage()
            );

            return false;
        }
    }
}
