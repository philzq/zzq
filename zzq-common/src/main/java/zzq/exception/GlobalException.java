package zzq.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import zzq.utils.R;

/**
 * 〈功能简述〉<br>
 * 〈全局异常封装〉
 *
 * @author zhouzhiqiang
 * @create 2018-11-25
 */
@RestControllerAdvice
public class GlobalException {

    @ExceptionHandler({Exception.class})
    public R exception(Exception ex){
        ex.printStackTrace();
        return R.error(ex.getMessage());
    }
}
