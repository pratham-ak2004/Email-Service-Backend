package com.main.emailService.controllers;

import com.main.emailService.models.Email;
import com.main.emailService.models.Mail;
import com.main.emailService.repo.MailsRepository;
import com.main.emailService.search.MailSearchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class InboxController {

    @Autowired
    private MailsRepository mailsRepository;

    @Autowired
    private MailSearchRepository mailSearchRepository;

    @GetMapping("/{USER}/getAll")
    public ResponseEntity getAllMails(@PathVariable String USER , @RequestBody Email email){
        return ResponseEntity.ok(this.mailSearchRepository.findAllMails(email));
    }

    @PostMapping("/addMail")
    public ResponseEntity addNewMail(@RequestBody Mail mail){
        return new ResponseEntity(this.mailsRepository.save(mail), HttpStatus.OK);
    }

    @GetMapping("/{USER}/getSearch")
    public ResponseEntity getSearchMails(@PathVariable String USER , @RequestBody Email email){
        return new ResponseEntity<>(this.mailSearchRepository.findBySearchText(email, USER) , HttpStatus.OK);
    }


}
