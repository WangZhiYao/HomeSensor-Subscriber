package com.paperloong.homesensor.subscriber.service

import com.paperloong.homesensor.subscriber.config.MqttConfig
import com.paperloong.homesensor.subscriber.ext.logger
import org.springframework.stereotype.Component

/**
 *
 *
 * @author WangZhiYao
 * @since 2024/6/23
 */
@Component
class MqttService(private val mqttGateway: MqttConfig.MqttGateway) {

    private val logger by logger()

    fun publish(topic: String, message: String) {
        logger.info("Publishing topic:$topic message:$message")
        mqttGateway.sendToMqtt(topic, message)
    }
}