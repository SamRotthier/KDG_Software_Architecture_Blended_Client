package be.kdg.sa.clients.controller;

import be.kdg.sa.clients.domain.Product;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/product")
public class OrderController {

    @PostMapping("/createorder")
    public void CreateOrder (@RequestBody String xml){

    }

    @PostMapping("/confirmorder")
    public void ConfirmOrder (@RequestBody int  orderNr){

    }

    @PostMapping("/cancelorder")
    public void CancelOrder (@RequestBody int orderNr){

    }

    @GetMapping("/getorder")
    public void GetOrder (@RequestBody int orderNr){

    }

}
