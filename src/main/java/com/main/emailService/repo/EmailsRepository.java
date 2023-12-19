package com.main.emailService.repo;

import com.main.emailService.models.Email;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EmailsRepository extends MongoRepository<Email,Integer> {
}
