package com.main.emailService.repo;

import com.main.emailService.models.Mail;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MailsRepository extends MongoRepository<Mail,Integer> {
}
