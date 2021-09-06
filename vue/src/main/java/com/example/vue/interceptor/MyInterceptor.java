package com.example.vue.interceptor;

import com.example.vue.exception.NLException;
import com.example.vue.jwtUtils.JsonWebToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @ClassName MyInterceptor
 * @Description 一句话描述功能
 * @Author lcz
 * @Date 2019/08/16 13:37
 */
@Slf4j
public class MyInterceptor implements HandlerInterceptor {

    /**
     * token校验
     *
     * @return 是否放行
     * @throws Exception 异常
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        log.info("preHandle execute >>> {}", request.getRequestURL());
        /*ResponseEntity responseEntity = JsonWebToken.validateToken(request);
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            return true;
        } else {
            log.info("preHandle execute validate token fail >>> {}", request.getRequestURL());
            String path = request.getContextPath();
            response.sendRedirect(path + "/index");
            throw new NLException(HttpStatus.FORBIDDEN.value(), responseEntity.getBody().toString());
        }*/
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object o, ModelAndView modelAndView) throws Exception {
        log.info("postHandle execute >>> {}", request.getRequestURL());

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object o, Exception e) throws Exception {
        log.info("afterCompletion execute >>> {}", request.getRequestURL());

    }
}
