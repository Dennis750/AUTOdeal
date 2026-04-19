package ro.autodeal.service;

import org.springframework.context.event.EventListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import ro.autodeal.event.CarPostCreatedEvent;
import ro.autodeal.event.CarPostDeletedEvent;
import ro.autodeal.event.CarPostUpdatedEvent;

@Service
public class NotificationService {

    private final JavaMailSender mailSender;

    public NotificationService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @EventListener
    public void handleCarPostCreated(CarPostCreatedEvent event) {
        sendEmail(event.getUserEmail(), "AUTOdeal - Car Post Created", event.getMessage());
    }

    @EventListener
    public void handleCarPostUpdated(CarPostUpdatedEvent event) {
        sendEmail(event.getUserEmail(), "AUTOdeal - Car Post Updated", event.getMessage());
    }

    @EventListener
    public void handleCarPostDeleted(CarPostDeletedEvent event) {
        sendEmail(event.getUserEmail(), "AUTOdeal - Car Post Deleted", event.getMessage());
    }

    private void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }
}