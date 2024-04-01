package be.kdg.sa.clients.domain;

import be.kdg.sa.clients.domain.Enum.OrderStatus;

import java.util.List;

public class Order {

    private List<String> products;
    private OrderStatus status;
    //private boolean cancelRequest;


 public void cancelOrder (){
    if (status == OrderStatus.PENDING){
        status = OrderStatus.CANCELLED;
    }
 }

}
