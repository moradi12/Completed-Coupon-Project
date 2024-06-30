package Openconnection.example.demo.advice;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class JwtAdvice {
    @ExceptionHandler(value = {SignatureException.class, MalformedJwtException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrDetails handleError(Exception e){
        return new ErrDetails("Error",e.getMessage());
    }
    @ExceptionHandler(value = {ExpiredJwtException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public void jwtExpire(){

    }



}
