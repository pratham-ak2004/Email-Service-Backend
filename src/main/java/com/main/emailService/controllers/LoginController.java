package com.main.emailService.controllers;

import com.main.emailService.models.Email;
import com.main.emailService.repo.EmailsRepository;
import com.main.emailService.search.EmailSearchRepositoryImplement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class LoginController {

    @Autowired
    private EmailsRepository emailsRepository;

    @Autowired
    private EmailSearchRepositoryImplement emailSearchRepositoryImplement;

    /*
    @GetMapping("/getAll")
    public ResponseEntity getAll(){
        return ResponseEntity.ok(this.emailsRepository.findAll());
    }
    */

    @PostMapping("/signUp")
    public ResponseEntity signUp(@RequestBody Email email){
        if(emailSearchRepositoryImplement.isEmailPresent(email)){
            return new ResponseEntity<>(HttpStatus.IM_USED);
        }else {
            this.emailsRepository.save(email);
            return new ResponseEntity<>(email.getId(), HttpStatus.CREATED);
        }
    }

    @PostMapping("/login")
    public ResponseEntity logIn(@RequestBody Email email){
        if(emailSearchRepositoryImplement.isEmailPresent(email)){
            if(emailSearchRepositoryImplement.isPasswordPresent(email)){
                return new ResponseEntity<>( emailSearchRepositoryImplement.getEmailId(email).toString() , HttpStatus.ACCEPTED);
            }else {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        }else {
            return new ResponseEntity<>("Email not present" , HttpStatus.FORBIDDEN);
        }
    }

}
