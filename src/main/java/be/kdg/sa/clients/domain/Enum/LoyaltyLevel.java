package be.kdg.sa.clients.domain.Enum;

public enum LoyaltyLevel {
    BRONZE(0.00), //<1000
    SILVER(0.05), //<5000
    GOLD(0.10), //<=10000
    PLATINUM(0.20); //>10000

    private final double discount;
    LoyaltyLevel(double discount) {
        this.discount = discount;
    }

    public double getDiscount() {
        return discount;
    }

    public static double getDiscount(int loyaltyPoints){
        if(loyaltyPoints < 1000){
            return LoyaltyLevel.BRONZE.discount;
        } else if (loyaltyPoints < 5000) {
            return LoyaltyLevel.SILVER.discount;
        } else if (loyaltyPoints <= 10000) {
            return LoyaltyLevel.GOLD.discount;
        } else {
            return LoyaltyLevel.PLATINUM.discount;
        }
    }

    public static LoyaltyLevel getLoyaltyLevel(int loyaltyPoints){
        if(loyaltyPoints < 1000){
            return LoyaltyLevel.BRONZE;
        } else if (loyaltyPoints < 5000) {
            return LoyaltyLevel.SILVER;
        } else if (loyaltyPoints <= 10000) {
            return LoyaltyLevel.GOLD;
        } else {
            return LoyaltyLevel.PLATINUM;
        }
    }
}
