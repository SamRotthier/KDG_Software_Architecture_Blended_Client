package be.kdg.sa.clients.controller;

import be.kdg.sa.clients.controller.dto.AccountDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/account") //path to be checked
public class AccountController {
    public AccountController() {
    }

    @PostMapping("/create")
    public ResponseEntity<AccountDto> createAccount(@RequestParam String name){
        // TODO
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
