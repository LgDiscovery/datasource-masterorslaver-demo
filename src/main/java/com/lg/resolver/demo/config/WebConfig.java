package com.lg.resolver.demo.config;

import com.lg.resolver.demo.component.CurrentUserNameHandlerMethodArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig  implements WebMvcConfigurer {

    /**
     *  扩展springMVC 添加自定义的参数解析器
     * @param resolvers
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new CurrentUserNameHandlerMethodArgumentResolver());
    }
}
