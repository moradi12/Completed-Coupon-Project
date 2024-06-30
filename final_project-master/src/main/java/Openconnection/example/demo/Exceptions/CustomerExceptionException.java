package Openconnection.example.demo.Exceptions;

public class CustomerExceptionException extends Exception {
    public CustomerExceptionException(ErrMsg errMsg) {
        super(errMsg.getMsg());
    }

    public CustomerExceptionException(ErrMsg errMsg, Throwable cause) {
        super(errMsg.getMsg(), cause);
    }
}
