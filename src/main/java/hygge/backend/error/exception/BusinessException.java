package hygge.backend.error.exception;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {
    private String code;

    public BusinessException(ExceptionInfo exceptionInfo) {
        super(exceptionInfo.getMsg());
        this.code = exceptionInfo.getCode();
    }

}
