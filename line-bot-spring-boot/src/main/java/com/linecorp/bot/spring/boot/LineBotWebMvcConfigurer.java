/*
 * Copyright 2016 LINE Corporation
 *
 * LINE Corporation licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package com.linecorp.bot.spring.boot;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

import com.linecorp.bot.spring.boot.interceptor.LineBotServerInterceptor;
import com.linecorp.bot.spring.boot.support.LineBotServerArgumentProcessor;

@SuppressWarnings("deprecation"
        /* WebMvcConfigurerAdapter is deprecated in 5.x. TODO: Migrate to WebMvcConfigurer */)
@Configuration
@Import(LineBotWebMvcBeans.class)
@ConditionalOnWebApplication
public class LineBotWebMvcConfigurer
        extends org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter {
    @Autowired
    private LineBotServerInterceptor lineBotServerInterceptor;
    @Autowired
    private LineBotServerArgumentProcessor lineBotServerArgumentProcessor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(lineBotServerInterceptor).addPathPatterns("/callback");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(lineBotServerArgumentProcessor);
    }
}
