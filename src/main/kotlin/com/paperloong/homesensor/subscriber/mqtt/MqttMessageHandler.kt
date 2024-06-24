package com.paperloong.homesensor.subscriber.mqtt

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.paperloong.homesensor.subscriber.ext.logger
import com.paperloong.homesensor.subscriber.model.LightSensorMessage
import com.paperloong.homesensor.subscriber.service.SensorService
import org.springframework.integration.mqtt.support.MqttHeaders
import org.springframework.messaging.Message
import org.springframework.messaging.MessageHandler
import org.springframework.stereotype.Component

/**
 *
 *
 * @author WangZhiYao
 * @since 2024/6/21
 */
@Component
class MqttMessageHandler(
    private val sensorService: SensorService
) : MessageHandler {

    private val logger by logger()

    private val mapper = jacksonObjectMapper()

    override fun handleMessage(message: Message<*>) {
        val topic = message.headers[MqttHeaders.RECEIVED_TOPIC] as String
        val messagePayload = message.payload as String
        logger.info("Received message from topic: $topic, message: $messagePayload")
        if (topic.endsWith("light")) {
            val lightSensorMessage = mapper.readValue<LightSensorMessage>(messagePayload)
            sensorService.processLightSensorMessage(lightSensorMessage)
        }
    }
}