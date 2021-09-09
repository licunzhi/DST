package com.cz.middlevisual;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MiddlevisualApplication {

    public static void main(String[] args) {
        SpringApplication.run(MiddlevisualApplication.class, args);

        /*fixme lcz 使用监听器单独实现启动项目打开某个地址*/
        try {
            Runtime.getRuntime().exec("cmd /c start http://localhost:8080/cz/doc.html");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
