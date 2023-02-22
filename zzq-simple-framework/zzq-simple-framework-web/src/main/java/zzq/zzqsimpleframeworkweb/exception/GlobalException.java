package zzq.zzqsimpleframeworkweb.exception;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import zzq.zzqsimpleframeworkcommon.entity.CommonError;
import zzq.zzqsimpleframeworkcommon.entity.CommonRsp;
import zzq.zzqsimpleframeworkcommon.entity.HttpStatus;
import zzq.zzqsimpleframeworkcommon.enums.BusinessCodeEnum;
import zzq.zzqsimpleframeworkcommon.exception.GeneralException;
import zzq.zzqsimpleframeworklog.LogUtilFactory;

/**
 * 全局异常捕获
 *
 * @author zhouzhiqiang
 * @version 1.0
 * @date 2022-10-26 11:24
 */
@ControllerAdvice
public class GlobalException {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public CommonRsp<Object> exceptionHandler(Exception e) {
        CommonRsp<Object> errorCommonRsp = null;
        String message = null;
        if (e instanceof GeneralException) {
            GeneralException generalException = (GeneralException) e;
            CommonError commonError = generalException.getCommonError();
            message = commonError.getMessage();
            errorCommonRsp = CommonRsp.error(commonError.getCode(), commonError.getBusinessCode(), commonError.getMessage());
        } else {
            message = BusinessCodeEnum.SYSTEM_EXCEPTION.getMessage();
            errorCommonRsp = CommonRsp.error(HttpStatus.ERROR.getCode(), BusinessCodeEnum.SYSTEM_EXCEPTION.getBusinessCode(), BusinessCodeEnum.SYSTEM_EXCEPTION.getMessage());
        }
        LogUtilFactory.SYSTEM_ERROR.error(HttpStatus.ERROR.getMessage(), message, e);
        return errorCommonRsp;
    }

    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public CommonRsp<Object> methodArgumentNotValidExceptionHandler(Exception e) {
        LogUtilFactory.SYSTEM_ERROR.error(HttpStatus.BAD_REQUEST.getMessage(), e.getMessage(), e);
        CommonRsp<Object> errorCommonRsp = CommonRsp.error(HttpStatus.BAD_REQUEST.getCode(), 0, e.getMessage());
        return errorCommonRsp;
    }
}
