package com.main.emailService.search;

import com.main.emailService.models.Email;
import org.bson.types.ObjectId;

public interface EmailSearchRepository {

    boolean isEmailPresent(Email user);

    boolean isPasswordPresent(Email user);

    ObjectId getEmailId(Email email);
}
