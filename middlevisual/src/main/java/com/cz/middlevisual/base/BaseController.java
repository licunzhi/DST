package com.cz.middlevisual.base;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @program: DST
 * @description: 基础Controller
 * @author: Cai.Min
 * @create: 2021-09-06 19:17
 **/
@Slf4j
public class BaseController {

    @ExceptionHandler
    public BaseResult exceptionHandler(HttpServletRequest request, HttpServletResponse response, Exception exception) {
        String ex = this.getErrorInfoFromException(exception);
        log.error("统一异常处理：{}", ex);
        if (null != request.getHeader("X-Requested-With") && request.getHeader("X-Requested-With").equalsIgnoreCase("XMLHttpRequest")) {
            request.setAttribute("requestHeader", "ajax");
        }

        return exception instanceof HttpMessageNotReadableException ? BaseResult.failResultCreate("向后台传递的数据格式有不正确的") : BaseResult.failResultCreate("后台没有响应，请重试。", exception.toString());
    }

    public String getErrorInfoFromException(Exception e) {
        try {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            StringBuffer error = sw.getBuffer();
            return error.toString();
        } catch (Exception var5) {
            return "ErrorInfoFromException";
        }
    }
}
