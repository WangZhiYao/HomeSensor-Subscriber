package com.paperloong.homesensor.subscriber.service

import com.paperloong.homesensor.subscriber.model.LightSensorData
import com.paperloong.homesensor.subscriber.repository.SensorDataMongoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import org.springframework.stereotype.Service

/**
 *
 *
 * @author WangZhiYao
 * @since 2024/6/22
 */
@Service
class SensorDataService(private val mongoRepository: SensorDataMongoRepository) : ScopedService() {

    suspend fun insertSensorData(lightSensorData: LightSensorData) =
        flow {
            emit(mongoRepository.save(lightSensorData))
        }
            .flowOn(Dispatchers.IO)
}