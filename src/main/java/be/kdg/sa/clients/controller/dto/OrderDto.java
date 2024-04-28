package be.kdg.sa.clients.controller.dto;

import java.util.UUID;


public record OrderDto(UUID orderId){
    public OrderDto {
    }
}

/*public record OrderDto(UUID orderId, UUID accountId , LocalDateTime creationDateTime ,OrderStatus status, double totalPrice, List<ProductDto> products) {
    public OrderDto(Order order, Account account) {
        this(order.getOrderId(),
                account.getAccountId(),
                order.getCreationDateTime(),
                order.getStatus(),
                order.getTotalPrice(),
                Collections.unmodifiableList(order.getProducts())
    }
}

// order.getProducts().stream().map(ProductDto::new).toList());
*/



