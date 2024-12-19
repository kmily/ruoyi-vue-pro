/*
 * Copyright (C) 2012-present the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.iocoder.yudao.module.iot.controller.admin.plugininfo;

import cn.iocoder.yudao.module.iot.api.Greeting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 打招呼 测试用例
 */
@Component
public class Greetings {

    @Autowired
    private List<Greeting> greetings;

    public Integer printGreetings() {
        System.out.printf("找到扩展点的 %d 个扩展 '%s'%n", greetings.size(), Greeting.class.getName());
        for (Greeting greeting : greetings) {
            System.out.println(">>> " + greeting.getGreeting());
        }
        return greetings.size();
    }

}