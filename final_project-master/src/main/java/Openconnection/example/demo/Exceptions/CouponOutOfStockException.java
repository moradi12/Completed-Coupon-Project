package Openconnection.example.demo.Exceptions;

public class CouponOutOfStockException extends Exception {
    public CouponOutOfStockException(String message) {
        super(message);
    }
}