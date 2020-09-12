package site.javaee.mall.product.execption;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import site.javaee.mall.common.exception.BizCodeEnume;
import site.javaee.mall.common.utils.R;

import java.util.HashMap;
import java.util.Map;

/**
 * 集中处理所有异常
 *
 * @author shkstart
 * @create 2020-09-10 21:13
 */
@Slf4j
@RestControllerAdvice(basePackages = "site.javaee.mall.product.controller")
public class MallExceptionControllerAdvice {


    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public R handleValidException(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        Map<String, String> errorMap = new HashMap<>();
        bindingResult.getFieldErrors().forEach((item) -> {
            String message = item.getDefaultMessage();
            String field = item.getField();
            errorMap.put(field, message);
        });

        R result = R.error(BizCodeEnume.VALID_EXCEPTION.getCode(), BizCodeEnume.VALID_EXCEPTION.getMsg()).put("data", errorMap);
        log.error(result.toString());
        return result;
    }

    @ExceptionHandler(value = Exception.class)
    public R handleException(Exception e) {
        R result = R.error(BizCodeEnume.UNKNOW_EXCEPTION.getCode(), BizCodeEnume.UNKNOW_EXCEPTION.getMsg());
        log.error(result.toString());
        return result;
    }
}
