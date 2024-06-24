package com.paperloong.homesensor.subscriber.repository

import com.paperloong.homesensor.subscriber.model.Sensor
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

/**
 *
 *
 * @author WangZhiYao
 * @since 2024/6/21
 */
@Repository
interface SensorMongoRepository : MongoRepository<Sensor, String> {

    fun findSensorByMac(mac: String): List<Sensor>

}