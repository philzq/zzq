package zzq.zzqsimpleframeworkcommon.exception;

import zzq.zzqsimpleframeworkcommon.entity.CommonError;
import zzq.zzqsimpleframeworkcommon.entity.HttpStatus;
import lombok.Getter;

/**
 * 通用异常
 *
 * @author zhouzhiqiang
 * @version 1.0
 * @date 2023-01-12 9:55
 */
public class GeneralException extends RuntimeException {

    /**
     * 错误实体
     */
    @Getter
    private CommonError commonError;

    public GeneralException(String message) {
        this(0, message);
    }

    public GeneralException(int businessCode, String message) {
        this(HttpStatus.ERROR.getCode(), businessCode, message);
    }

    public GeneralException(int code, int businessCode, String message) {
        this(code,businessCode,message,null);
    }

    public GeneralException(Throwable cause) {
        this(null, cause);
    }

    public GeneralException(String message, Throwable cause) {
        this(0, message, cause);
    }

    public GeneralException(int businessCode, String message, Throwable cause) {
        this(HttpStatus.ERROR.getCode(), businessCode, message, cause);
    }

    public GeneralException(int code, int businessCode, String message, Throwable cause) {
        super(CommonError.builder().code(code).businessCode(businessCode).message(message).build().toString(), cause);
        this.commonError = CommonError.builder().code(code).businessCode(businessCode).message(message).build();
    }

    public static void main(String[] args){
        try {
            throw new GeneralException("异常了");
        }catch (GeneralException e){
            System.out.println(e.getCommonError().toString());
            e.printStackTrace();
        }
    }
}
