package com.paperloong.homesensor.subscriber.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.bind.ConstructorBinding

/**
 *
 *
 * @author WangZhiYao
 * @since 2024/6/21
 */
@ConfigurationProperties(prefix = "mqtt")
data class MqttProperties @ConstructorBinding constructor(
    val url: String,
    val username: String,
    val password: String,
    val clientId: String,
    val topic: String
)