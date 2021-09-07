package com.cz.middlevisual.base;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @program: DST
 * @description: 基础返回结果
 * @author: Cai.Min
 * @create: 2021-09-06 19:34
 **/
public class BaseResult<T> implements Serializable {

    private static final long serialVersionUID = -8410083111719175072L;

    public static final int FAILED = 0;
    public static final int SUCCESS = 1;
    public static final int VALIDATOR = 2;
    private int code;
    private List<String> messageList;
    private T data;

    public BaseResult() {
    }

    public BaseResult(String message) {
        List<String> messageList = new ArrayList<>();
        messageList.add(message);
        this.messageList = messageList;
    }

    public BaseResult(Boolean b) {
        if (true == b) {
            code = 1;
            List<String> messageList = new ArrayList<>();
            messageList.add("操作成功");
        } else {
            code = 0;
            List<String> messageList = new ArrayList<>();
            messageList.add("操作失败");
        }
    }

    public BaseResult(int code, List<String> messageList, T data) {
        this.code = code;
        this.messageList = messageList;
        this.data = data;
    }

    public static BaseResult failResultCreate(List<String> message) {
        return new BaseResult(0, message, (Object) null);
    }

    public static BaseResult failResultCreate(String message) {
        List<String> messageList = new ArrayList<>();
        messageList.add(message);
        return new BaseResult(0, messageList, (Object) null);
    }

    public static BaseResult failResultCreate(List<String> messageList, Object data) {

        return new BaseResult(0, messageList, data);
    }

    public static BaseResult failResultCreate(String message, Object data) {
        List<String> messageList = new ArrayList<>();
        messageList.add(message);
        return new BaseResult(0, messageList, data);
    }

    public static <T> BaseResult<T>  successResultCreate(T data) {
        List<String> messageList = new ArrayList<>();
        messageList.add("操作成功");
        return new BaseResult(1, messageList, data);
    }

    public static <T> BaseResult<T> successResultCreate(String message, T data) {
        List<String> messageList = new ArrayList<>();
        messageList.add(message);
        return new BaseResult(1, messageList, data);
    }

    public int getCode() {
        return this.code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<String> getMessageList() {
        return messageList;
    }

    public void setMessageList(List<String> messageList) {
        this.messageList = messageList;
    }

    public T getData() {
        return this.data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
