package com.main.emailService.search;

import com.main.emailService.models.Email;
import com.main.emailService.models.Mail;

import java.util.List;

public interface MailSearchRepository {
    List<Mail> findAllMails(Email email);

    List<Mail> findAllSent(Email email);

    List<Mail> findAllReceived(Email email);

    List<Mail> findBySearchText(Email email, String text);
}
