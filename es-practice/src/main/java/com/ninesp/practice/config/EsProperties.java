/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0 and the Server Side Public License, v 1; you may not use this file except
 * in compliance with, at your election, the Elastic License 2.0 or the Server
 * Side Public License, v 1.
 */

package com.ninesp.practice.config;

import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author ninesp
 * @date 2023/5/22
 */
@Data
@Configuration
@ConfigurationProperties(value = "elastic")
@ConditionalOnProperty(prefix = "elastic", name = {"enable"}, havingValue = "true", matchIfMissing = true)
public class EsProperties {
    private String enable;
    private String schema;
    private String address;
    /**
     * username of es 用户名,可缺省
     */
    private String username;
    /**
     * password of es 密码,可缺省
     */
    private String password;
}
