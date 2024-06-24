package com.paperloong.homesensor.subscriber.model

/**
 *
 *
 * @author WangZhiYao
 * @since 2024/6/22
 */
data class LightSensorMessage(
    val mac: String,
    val lux: Int,
    val timestamp: Long
)