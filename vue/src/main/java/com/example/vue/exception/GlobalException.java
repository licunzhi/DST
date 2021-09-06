package com.example.vue.exception;

import com.example.vue.bean.ResponseBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.el.MethodNotFoundException;
import java.net.BindException;

/**
 * @ClassName GlobalException
 * @Description 全局异常处理 禁止系统异常暴露
 * @Author lcz
 * @Date 2019/08/16 13:55
 */
/**
 * 定包处理全局异常
 * @ControllerAdvice(basePackages ="com.example.demo.controller")
 */
@ControllerAdvice
@ResponseBody
@Slf4j
public class GlobalException {

    /**
     * 全局异常处理
     * <p>
     * @notice e 可以持续扩展
     * </p>
     * @param e 参数缺失异常
     * @return 异常提示
     */
    @ExceptionHandler
    public ResponseEntity handleException(Exception e) {
        log.error(">>>>>>>> error message info: ");
        e.printStackTrace();
        if (e instanceof MissingServletRequestParameterException) {
            log.error("请求参数异常： {}", e);
            return ResponseEntity.badRequest().body(new ResponseBean<>(HttpStatus.BAD_REQUEST.value(), "参数格式异常", "参数格式异常"));
        } else if (e instanceof HttpMessageNotReadableException) {
            log.error("参数解析异常： {}", e);
            return ResponseEntity.badRequest().body(new ResponseBean<>(HttpStatus.BAD_REQUEST.value(), "参数解析异常", "参数解析异常"));
        } else if (e instanceof MethodArgumentNotValidException) {
            log.error("参数验证失败： {}", e);
            return ResponseEntity.badRequest().body(new ResponseBean<>(HttpStatus.BAD_REQUEST.value(), "参数验证失败", "参数验证失败"));
        } else if (e instanceof BindException) {
            log.error("参数绑定失败： {}", e);
            return ResponseEntity.badRequest().body(new ResponseBean<>(HttpStatus.BAD_REQUEST.value(), "参数绑定失败", "参数绑定失败"));
        }  else if (e instanceof HttpRequestMethodNotSupportedException) {
            log.error("请求方式不支持： {}", e);
            return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(new ResponseBean<>(HttpStatus.METHOD_NOT_ALLOWED.value(), "请求方式不支持", "请求方式不支持"));
        }  else if (e instanceof HttpMediaTypeNotSupportedException) {
            log.error("不支持媒体类型： {}", e);
            return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(new ResponseBean<>(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value(), "不支持媒体类型", "不支持媒体类型"));
        } else if (e instanceof NullPointerException) {
            log.error("空指针异常： {}", e);
            return ResponseEntity.badRequest().body(new ResponseBean<>(HttpStatus.BAD_REQUEST.value(), "获取参数异常", "获取参数异常"));
        }  else if (e instanceof MethodNotFoundException) {
            log.error("方法不存在： {}", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseBean<>(HttpStatus.NOT_FOUND.value(), "请求路径错误", "请求路径错误"));
        } else if (e instanceof NLException) {
            /*自定义逻辑异常*/
            log.error("逻辑异常报错： {}", e);
            NLException nlException = (NLException) e;
            if (nlException.getCode() == HttpStatus.FORBIDDEN.value()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ResponseBean<>(nlException.getCode(), nlException.getMessage(), nlException.getMessage()));
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseBean<>(nlException.getCode(), nlException.getMessage(), nlException.getMessage()));
            }
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseBean<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "服务器异常", "服务器异常"));
        }
    }
}
