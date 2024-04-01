package be.kdg.sa.clients.domain.Enum;

public enum LoyaltyLevel {
    BRONZE, //<1000
    SILVER, //<5000
    GOLD, //<=10000
    PLATINUM; //>10000

    public LoyaltyLevel  ReturnLoyaltyLevel (int loyaltyPoints){
        if(loyaltyPoints < 1000){
            return LoyaltyLevel.BRONZE;
        } else if (loyaltyPoints < 5000) {
            return LoyaltyLevel.SILVER;
        }else if (loyaltyPoints <= 10000) {
            return LoyaltyLevel.GOLD;
        }else if (loyaltyPoints > 10000) {
            return LoyaltyLevel.PLATINUM;
        }else{
            return null;
        }
    }
}
