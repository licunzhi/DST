package com.example.vue.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName NLException
 * @Description 一句话描述功能
 * @Author lcz
 * @Date 2019/08/16 16:35
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NLException extends Exception{
    private int code;
    private String message;

}
