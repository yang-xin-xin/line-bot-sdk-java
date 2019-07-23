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



import org.springframework.util.StringUtils;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;


/**
 * @author yangxinxin
 * 2019-07-23 14:04
 */
public class HostMapping {
    private String country;
    private String host;

    public static final Map<String, String> map = new ConcurrentHashMap<>();

    public HostMapping(String country, String host) {
        this.country = country;
        this.host = host;
        map.put(country, host);
    }

    private static final HostMapping LAZADA = new HostMapping("lazada", "47.89.75.212");
    private static final HostMapping US = new HostMapping("us", "198.11.136.27");
    private static final HostMapping HZ = new HostMapping("hz", "140.205.215.168");

    public static String getHost(String country) {
        if (StringUtils.isEmpty(country)) {
            return HZ.host;
        }

        String ret = map.get(country.toLowerCase());
        if (StringUtils.isEmpty(ret)) {
            return HZ.host;
        }

        return ret;
    }
}
