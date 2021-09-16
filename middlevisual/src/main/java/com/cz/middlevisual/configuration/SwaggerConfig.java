package com.cz.middlevisual.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @program: DST
 * @description: SwaggerConfiguration
 * @author: Cai.Min
 * @create: 2021-09-06 19:05
 **/
@EnableSwagger2
@Configuration
public class SwaggerConfig {

    @Bean
    public Docket docketZookeeper() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.cz.middlevisual.controller.zookeeper"))
                .paths(PathSelectors.any())
                .build().groupName("zookeeper");
    }

    @Bean
    public Docket docketKafka() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.cz.middlevisual.controller.kafka"))
                .paths(PathSelectors.any())
                .build().groupName("kafka");
    }

    public ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Plug-in visualization")
                .description("插件可视化相关接口")
                .termsOfServiceUrl("")
                .version("1.0")
                .build();
    }

}
