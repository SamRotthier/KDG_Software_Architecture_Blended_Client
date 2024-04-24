package be.kdg.sa.clients.controller;

import be.kdg.sa.clients.domain.Product;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/orders")
public class OrderController {

    @GetMapping("/")
    public void getOrders (@RequestParam){
    }
    @PostMapping("/")
    public void CreateOrder (@RequestBody String xml){

    }


    @PutMapping("/{orderid}/confirm")
    public void ConfirmOrder (@RequestParam int  orderNr){

    }

    @PutMapping("/{orderid}/cancel")
    public void CancelOrder (@RequestParam int orderNr){

    }

    @GetMapping("/{orderid}")
    public void GetOrder (@RequestParam int orderNr){

    }

}
