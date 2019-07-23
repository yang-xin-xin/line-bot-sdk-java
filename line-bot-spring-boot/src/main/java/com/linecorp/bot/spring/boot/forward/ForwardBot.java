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

package com.linecorp.bot.spring.boot.forward;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Autowired;


import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;


/**
 * 仅做转发通道使用
 * 使用http加host调用预发链接
 * @author yangxinxin
 * 2019-07-22 20:25
 */
@Slf4j
@RestController
@RequestMapping("/social/connect/forward")
public class ForwardBot {

    @Autowired
    HttpRequest request;

    /**
     * 接收回调
     */
    @RequestMapping(method = {RequestMethod.POST, RequestMethod.GET}, value = "/{country}/{path}")
    public ResponseEntity<Object> onReceived(
            @PathVariable("country") String country,
            @PathVariable("path") String path,
            @RequestBody String payload) {
        log.info("Callback from ForwardBot begin, country:{}, payload:{}, path:{}", country, payload, path);
        HttpHeaders httpHeaders = request.getHeaders();
        HttpMethod httpMethod = request.getMethod();

        // 处理数据
        String host = HostMapping.getHost(country);
        httpHeaders.add("Host", host);
        String url = generateUrl(path, host);




        ResponseEntity<Object> entity = null;
        HttpEntity<String> httpEntity = new HttpEntity<>(payload, httpHeaders);
        RestTemplate restTemplate = new RestTemplate();
        switch (httpMethod) {
            case POST:
                entity = restTemplate.postForEntity(url, httpEntity, Object.class);
                break;
            default:
                log.error("not support: " + httpMethod);
        }

        return entity;
    }

    private static String generateUrl(String url, String host) {
        try {
            url = URLDecoder.decode(url, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            log.error("decode err", e);
        }
        return url.replace(getDomain(url), host);
    }

    private static String getDomain(String url) {
        return url.substring(url.indexOf("//") + 2, url.indexOf("/", 9));
    }


    public static void main(String[] agrs) {
        String url = "https://pre-gcx.alibaba.com/icbu/xiaohe/portal.htm?pageId=365272&_param_digest_=cf6a6aaf27013cab82b822c1e3178ffd";
        String host = HostMapping.getHost("hz");
        System.out.println(generateUrl(url,host));
    }
}
