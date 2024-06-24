package com.paperloong.homesensor.subscriber.service

import com.paperloong.homesensor.subscriber.ext.logger
import com.paperloong.homesensor.subscriber.model.LightSensorData
import com.paperloong.homesensor.subscriber.model.LightSensorMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.springframework.stereotype.Service

/**
 *
 *
 * @author WangZhiYao
 * @since 2024/6/22
 */
@Service
class SensorService(
    private val mongoRepository: com.paperloong.homesensor.subscriber.repository.SensorMongoRepository,
    private val sensorDataService: SensorDataService
) : ScopedService() {

    private val logger by logger()

    @OptIn(ExperimentalCoroutinesApi::class)
    fun processLightSensorMessage(message: LightSensorMessage) {
        serviceScope.launch {
            flow {
                emit(mongoRepository.findSensorByMac(message.mac))
            }
                .flatMapConcat { sensors -> sensors.asFlow() }
                .map { sensor ->
                    LightSensorData(
                        sensor = sensor,
                        data = LightSensorData.Data(message.lux, message.timestamp)
                    )
                }
                .flatMapConcat { lightSensorData ->
                    sensorDataService.insertSensorData(lightSensorData)
                }
                .flowOn(Dispatchers.IO)
                .catch { ex ->
                    logger.error("Failed to save sensor data: ", ex)
                }
                .collect { data ->
                    logger.info("Save sensor data success: $data")
                }
        }
    }
}