package com.paperloong.homesensor.subscriber.repository

import com.paperloong.homesensor.subscriber.model.LightSensorData
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

/**
 *
 *
 * @author WangZhiYao
 * @since 2024/6/22
 */
@Repository
interface SensorDataMongoRepository : MongoRepository<LightSensorData, String>