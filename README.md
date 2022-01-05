# Recruiter
### Employee search application. Angular + Spring. Microservice architecture.
---
The project consists of three parts:
- Client - written in Angular;
- Server - written in Java (Spring);
- MailSender - written in Java (Spring);
---
Interaction between frontend and backend parts is implemented using REST.
The interaction between the server and the mailsender is implemented using a message broker Kafka.
---
Technologies used:
- Spring (Boot, Security, Data, Mail);
- Angular (Material, Bootstrap);
- Kafka;
- Maven, REST, PostgeSQL.
---
![ALT TEXT](https://github.com/babay1990/RepoForReadMe/blob/main/rec.png)
The application allows you to register as a recruiter or an employee Recruiter can add vacancies. The employee can respond to them. When registering, the user will be sent a message to the specified email, which will contain a link to confirm registration. When the recruiter adds an employee to the list of approved candidates, the employee will also be sent a message to the mail specified during registration.

A demo version of the application without using Apache Kafka is presented at the link:
https://recruiterclient.herokuapp.com
