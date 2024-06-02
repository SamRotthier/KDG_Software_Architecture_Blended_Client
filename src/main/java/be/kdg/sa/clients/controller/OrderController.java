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

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/") //TODO
    public ResponseEntity<?> getOrders (){
        Optional<List<Order>> foundOrders = orderService.getOrders();
        return foundOrders.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/create")
    public ResponseEntity<?> createOrder (@Valid @RequestBody OrderDto orderDto, BindingResult bindingResult){
        orderService.createOrder(orderDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("The order was successfully created");
    }


    @PutMapping("/{orderId}/confirm")
    public ResponseEntity<?> confirmOrder (@Valid @PathVariable UUID orderId){
        Optional<Order> foundOrder = orderService.getOrderByOrderId(orderId);
        if(foundOrder.isEmpty()){
            return ResponseEntity.badRequest().body("Invalid orderId");
        }

        if (foundOrder.get().getStatus() == OrderStatus.CONFIRMED || foundOrder.get().getStatus() == OrderStatus.CANCELLED){
            return ResponseEntity.badRequest().body("We could not confirm the order. The order is already: " + foundOrder.get().getStatus());
        }else{
            orderService.confirmOrder(foundOrder);
            return ResponseEntity.status(HttpStatus.CREATED).body("The order was confirmed");
        }
    }

    @PutMapping("/{orderId}/cancel")
    public ResponseEntity<?> cancelOrder (@PathVariable UUID orderId){
        Optional<Order> foundOrder = orderService.getOrderByOrderId(orderId);
        if(foundOrder.isEmpty()){
            return ResponseEntity.badRequest().body("Invalid orderId");
        }

        if (foundOrder.get().getStatus() == OrderStatus.CONFIRMED || foundOrder.get().getStatus() == OrderStatus.CANCELLED){
            return ResponseEntity.badRequest().body("We could not cancel the order. The order is already: " + foundOrder.get().getStatus());
        }else{
            orderService.cancelOrder(foundOrder);
            return ResponseEntity.status(HttpStatus.CREATED).body("The order was cancelled");
        }
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<?> getOrder (@PathVariable UUID orderId){
         Optional<Order> foundOrder = orderService.getOrderByOrderId(orderId);
        return foundOrder.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }


}
