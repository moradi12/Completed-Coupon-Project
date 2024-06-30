package Openconnection.example.demo.Exceptions;

public class    CouponAlreadyPurchasedException extends RuntimeException {

    public CouponAlreadyPurchasedException(String message) {
        super(message +"You Already Have this Coupon");
    }
}
