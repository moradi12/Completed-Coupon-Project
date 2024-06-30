package Openconnection.example.demo.advice;

import Openconnection.example.demo.Exceptions.CouponNotFoundException;
import Openconnection.example.demo.Exceptions.CompanyNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class CompanyAdvice {

    @ExceptionHandler(value = {CouponNotFoundException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrDetails handleCouponNotFoundException(CouponNotFoundException e){
        return new ErrDetails("Coupon Error", e.getMessage());
    }

    @ExceptionHandler(value = {CompanyNotFoundException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrDetails handleCompanyNotFoundException(CompanyNotFoundException e){
        return new ErrDetails("Company Error", e.getMessage());
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException exception){
        Map<String, String> errors = new HashMap<>();

        exception.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        System.out.println(exception.getBindingResult().getAllErrors());
        return errors;
    }
}
