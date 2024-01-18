package com.main.emailService.controllers;

import com.main.emailService.models.Email;
import com.main.emailService.models.Mail;
import com.main.emailService.repo.MailsRepository;
import com.main.emailService.search.MailSearchRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class InboxController {

    @Autowired
    private MailsRepository mailsRepository;

    @Autowired
    private MailSearchRepository mailSearchRepository;

    @GetMapping("/getAll")
    public ResponseEntity getAllMails(@RequestBody Email email){
        return ResponseEntity.ok(this.mailSearchRepository.findAllMails(email));
    }

    @PostMapping("/addMail")
    public ResponseEntity addNewMail(@RequestBody Mail mail){
        return new ResponseEntity(this.mailsRepository.save(mail), HttpStatus.OK);
    }

    @Component
    @Data
    private static class MultiRequest{
        private String targetText;
        private Email email;
    }
    @GetMapping("/getSearch")
    public ResponseEntity getSearchMails(@RequestBody MultiRequest multiRequest){
        return new ResponseEntity<>(this.mailSearchRepository.findBySearchText(multiRequest.getEmail(), multiRequest.getTargetText()) , HttpStatus.OK);
    }

    @GetMapping("/getSent")
    public ResponseEntity getSentMails(@RequestBody Email email){
        return ResponseEntity.ok(this.mailSearchRepository.findAllSent(email));
    }

    @GetMapping("/getReceived")
    public ResponseEntity getReceivedMails(@RequestBody Email email){
        return ResponseEntity.ok(this.mailSearchRepository.findAllReceived(email));
    }

}
