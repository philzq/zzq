package zzq.zzqsimpleframeworkweb.exception;

import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
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
public class GlobalExceptionHandler {

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
    public CommonRsp<Object> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        String defaultMessage = e.getBindingResult().getFieldError().getDefaultMessage();
        LogUtilFactory.SYSTEM_ERROR.error(HttpStatus.BAD_REQUEST.getMessage(), defaultMessage, e);
        CommonRsp<Object> errorCommonRsp = CommonRsp.error(HttpStatus.BAD_REQUEST.getCode(), BusinessCodeEnum.BAD_REQUEST.getBusinessCode(), defaultMessage);
        return errorCommonRsp;
    }

    @ResponseBody
    @ExceptionHandler(BindException.class)
    public CommonRsp<Object> bindExceptionHandler(BindException e) {
        String defaultMessage = e.getFieldError().getDefaultMessage();
        LogUtilFactory.SYSTEM_ERROR.error(HttpStatus.BAD_REQUEST.getMessage(), defaultMessage, e);
        CommonRsp<Object> errorCommonRsp = CommonRsp.error(HttpStatus.BAD_REQUEST.getCode(), BusinessCodeEnum.BAD_REQUEST.getBusinessCode(), defaultMessage);
        return errorCommonRsp;
    }

    @ResponseBody
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public CommonRsp<Object> methodMissingServletRequestParameterExceptionHandler(MissingServletRequestParameterException e) {
        String defaultMessage = e.getMessage();
        LogUtilFactory.SYSTEM_ERROR.error(HttpStatus.BAD_REQUEST.getMessage(), defaultMessage, e);
        CommonRsp<Object> errorCommonRsp = CommonRsp.error(HttpStatus.BAD_REQUEST.getCode(), BusinessCodeEnum.BAD_REQUEST.getBusinessCode(), defaultMessage);
        return errorCommonRsp;
    }
}
