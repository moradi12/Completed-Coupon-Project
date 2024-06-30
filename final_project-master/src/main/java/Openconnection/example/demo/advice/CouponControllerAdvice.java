package Openconnection.example.demo.advice;
import Openconnection.example.demo.Exceptions.CouponNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;


@RestControllerAdvice
public class CouponControllerAdvice {
    @ExceptionHandler(value = {CouponNotFoundException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrDetails handleError(Exception e){
        return new ErrDetails("Error",e.getMessage());
    }


    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)

    /// first string is the normal one-second showing the error .
    public Map<String,String> handleValidationExceptions(MethodArgumentNotValidException exception){
        Map<String,String> errors = new HashMap<>();

        //
        exception.getBindingResult().getAllErrors().forEach((error)->{
            String fieldName = ((FieldError)error).getField();

            String errorMessage = error.getDefaultMessage();

            // showing the errors + error name
            errors.put(fieldName,errorMessage);
        });
        System.out.println(exception.getBindingResult().getAllErrors());
        return errors;
    }
}