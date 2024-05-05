package be.kdg.sa.clients.controller;

import be.kdg.sa.clients.controller.dto.AccountDto;
import be.kdg.sa.clients.controller.dto.OrderDto;
import be.kdg.sa.clients.domain.Enum.OrderStatus;
import be.kdg.sa.clients.domain.Order;
import be.kdg.sa.clients.services.AccountService;
import be.kdg.sa.clients.services.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/")
    public ResponseEntity<?> GetOrders (){
        Optional<List<Order>> foundOrders = orderService.getOrders();
        return foundOrders.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/create")
    public ResponseEntity<?> CreateOrder (@Valid @RequestBody OrderDto orderDto, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return ResponseEntity.badRequest().body("Invalid request body for the creation of an order");
        }
        orderService.createOrder(orderDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("The order was successfully created");
    }


    @PutMapping("/{orderid}/confirm") //TODO
    public ResponseEntity<?> ConfirmOrder (@PathVariable UUID orderId, BindingResult bindingResult){
        Optional<Order> foundOrder = orderService.getOrderByOrderId(orderId);
        if(bindingResult.hasErrors() || foundOrder.isEmpty()){
            return ResponseEntity.badRequest().body("Invalid orderId");
        }

        if (foundOrder.get().getStatus() == OrderStatus.CONFIRMED || foundOrder.get().getStatus() == OrderStatus.CANCELLED){
            return ResponseEntity.badRequest().body("We could not confirm the order. The order is already: " + foundOrder.get().getStatus());
        }else{
            orderService.ConfirmOrder(foundOrder);
            return ResponseEntity.status(HttpStatus.CREATED).body("The order was confirmed");
        }
    }

    @PutMapping("/{orderid}/cancel") //TODO
    public ResponseEntity<?> CancelOrder (@PathVariable UUID orderId, BindingResult bindingResult){
        Optional<Order> foundOrder = orderService.getOrderByOrderId(orderId);
        if(bindingResult.hasErrors() || foundOrder.isEmpty()){
            return ResponseEntity.badRequest().body("Invalid orderId");
        }

        if (foundOrder.get().getStatus() == OrderStatus.CONFIRMED || foundOrder.get().getStatus() == OrderStatus.CANCELLED){
            return ResponseEntity.badRequest().body("We could not confirm the order. The order is already: " + foundOrder.get().getStatus());
        }else{
            orderService.CancelOrder(foundOrder);
            return ResponseEntity.status(HttpStatus.CREATED).body("The order was confirmed");
        }
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<?> GetOrder (@PathVariable UUID orderId){
         Optional<Order> foundOrder = orderService.getOrderByOrderId(orderId);
        return foundOrder.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }


}
