package Openconnection.example.demo.Exceptions;

import lombok.Getter;

@Getter
public enum ErrMsg {

    /// Coupons
    COUPON_ID_NOT_FOUND("Coupon id not found"),
    COUPON_ID_ALREADY_EXISTS("Id already exists!"),
    COUPON_ERROR("Coupon doesn't exist"),
    COUPON_ERROR_HAS_BEEN_USED("Coupon already Used"),
    COUPON_NOT_FOUND("Coupon not found"),
    COUPON_OUT_OF_STOCK("Coupon is out of stock"),
    COUPON_ALREADY_PURCHASED("Coupon purchased successfully"),

    /// Customers
    CUSTOMER_ALREADY_EXISTS("Customer already exists!"),
    CUSTOMER_NOT_FOUND("Customer not found"),
    AUTHENTICATION_FAILED("Authentication failed. Incorrect username or password"),
    UNAUTHORIZED_ACCESS("Unauthorized access. You do not have permission to perform this action"),
    CUSTOMER_ERROR("Error occurred while handling customer operation"),
    ADMIN_NOT_FOUND("Error: Admin not found. Please check the provided details and try again"),

    // Login
    LOGIN_FAILED("Login failed. Invalid username or password"),
    LOGIN_EXCEPTION("Login exception occurred"),
    ADMIN_NOT_ALLOWED("Admin registration is not allowed"),
    INVALID_USER_TYPE("Invalid user type"),

    // DataBase
    PERMISSION_DENIED("Permission denied. You do not have access"),
    DATABASE_CONNECTION_ERROR("Error connecting to the database"),

    // Company
    COMPANY_NOT_FOUND("Company not found"),
    COMPANY_ALREADY_EXISTS("Company already exists!"),
    COMPANY_ERROR("Company error occurred"),
    COMPANY_LOGIN_FAILED("Company login failed. Invalid email or password"),


    // Admin
    ADMIN_ALREADY_EXISTS("Admin already exists!");


    private final String msg;

    ErrMsg(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }
}
