package com.example.mailSender.service;

import com.example.mailSender.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Service
public class ConsumerService {

    @Autowired
    private JavaMailSender emailSender;

    @KafkaListener(topics = "messages", groupId = "message_group_id", containerFactory = "kafkaListenerContainerFactory")
    public void consume(Message newMessage) throws MessagingException, IOException, IllegalStateException{

        System.out.println("Сообщение пришло --> " + newMessage);

        MimeMessage message = emailSender.createMimeMessage();

        boolean multipart = true;

        MimeMessageHelper helper = new MimeMessageHelper(message, multipart);

        helper.setFrom("shpaginjava@gmail.com");
        helper.setTo(newMessage.getCandidateEmail());
        helper.setSubject("Отклик на вакансию " + newMessage.getVacancyProfession());

        helper.setText("Добрый день, " + newMessage.getCandidateFirstname() + "!" + System.lineSeparator() +
                "Ваш отклик на вакансию " + newMessage.getVacancyProfession() + " в компанию " + newMessage.getVacancyCompany() +
                " был успешно принят!" + System.lineSeparator() +
                "Для подробностей и дальнейших обсуждений связывайтесь с рекрутером вакансии или дождитесь обратной связи!" + System.lineSeparator() +
                "Контакты рекрутера:" + System.lineSeparator() +
                newMessage.getRecruiterFirstname() + " " + newMessage.getRecruiterLastname() + System.lineSeparator() +
                "Телефон: " + newMessage.getRecruiterPhoneNumber() + System.lineSeparator() +
                "Email: " + newMessage.getRecruiterEmail() + System.lineSeparator() +
                "С наилучшими пожеланиями Recruiter by Shpagin A.S.");


        //File convFile = new File(file.getOriginalFilename());
        //convFile.createNewFile();
        //FileOutputStream fos = new FileOutputStream(convFile);
        //fos.write(file.getBytes());
        //fos.close();

        //FileSystemResource file1 = new FileSystemResource(convFile);
        //helper.addAttachment(file1.getFilename(), file1);

        emailSender.send(message);
    }

    @KafkaListener(topics = "emailRegistration", groupId = "mail_group", containerFactory = "kafkaListenerTwoContainerFactory")
    public void consumeSendRegistrationEmail(String email) throws MessagingException, IOException, IllegalStateException{
        MimeMessage message = emailSender.createMimeMessage();

        boolean multipart = true;

        MimeMessageHelper helper = new MimeMessageHelper(message, multipart);

        helper.setFrom("shpaginjava@gmail.com");
        helper.setTo(email);
        helper.setSubject("Подтверждение регистрации");

        email = email.substring(1, email.length() - 1);
        helper.setText("Добрый день, подтвердите регистрацию на сервисе Recruiter, перейдя по ссылке:" + System.lineSeparator() +
                "http://localhost:8080/api/auth/acceptRegistration?email=" + email);

        emailSender.send(message);
    }

}