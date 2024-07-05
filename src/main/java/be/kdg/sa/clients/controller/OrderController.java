package be.kdg.sa.clients.controller;

import be.kdg.sa.clients.controller.dto.AccountDto;
import be.kdg.sa.clients.controller.dto.LoyaltyDto;
import be.kdg.sa.clients.controller.dto.OrderDto;
import be.kdg.sa.clients.domain.Enum.OrderStatus;
import be.kdg.sa.clients.domain.Order;
import be.kdg.sa.clients.domain.Product;
import be.kdg.sa.clients.services.AccountService;
import be.kdg.sa.clients.services.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
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

    @GetMapping("/")
    public ResponseEntity<?> getOrders (){
        List<Order> foundOrders = orderService.getOrders();
        return ResponseEntity.ok(foundOrders);
    }

    @PreAuthorize("hasAnyAuthority('user')")
    @PostMapping("/create")
    public ResponseEntity<?> createOrder (@Valid @RequestBody OrderDto orderDto){
        orderService.createOrder(orderDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("The order was successfully created");
    }

    @PreAuthorize("hasAnyAuthority('user')")
    @PostMapping("/{orderId}/create")
    public ResponseEntity<?> createCopyOrder (@Valid @PathVariable UUID orderId){
       orderService.createCopyOrder(orderId);
        return ResponseEntity.status(HttpStatus.CREATED).body("The order was successfully created");
    }

    @PreAuthorize("hasAnyAuthority('user')")
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
    @PreAuthorize("hasAnyAuthority('user')")
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

    @PreAuthorize("hasAnyAuthority('user', 'admin')")
    @GetMapping("/{orderId}")
    public ResponseEntity<?> getOrder (@PathVariable UUID orderId){
         Optional<Order> foundOrder = orderService.getOrderByOrderId(orderId);
        return foundOrder.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/PurchaseOrderXml")
    public ResponseEntity<?> PurchaseOrderWithXML (@RequestParam MultipartFile file) throws IOException {
       orderService.PurchaseOrderWithXML(file.getInputStream());
        return ResponseEntity.status(HttpStatus.CREATED).body("The order was confirmed");
    }

    @PreAuthorize("hasAnyAuthority('admin')")
    @GetMapping("/report")
    public ResponseEntity<?> generateSalesReport (
            @RequestParam(value = "product", required = false) UUID productId,
            @RequestParam(value = "date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime orderDate,
            @RequestParam(value = "user", required = false) UUID accountId){
        if ((productId != null && orderDate != null) ||
                (productId != null && accountId != null) ||
                (orderDate != null && accountId != null)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Only one parameter (product, date or user) should be provided.");
        }

        List<?> salesReport = orderService.generateSalesReport(productId, orderDate, accountId);
        if(salesReport != null){
            return ResponseEntity.status(HttpStatus.OK).body(salesReport);
        } else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No sales found for given parameters");
        }
    }


}
