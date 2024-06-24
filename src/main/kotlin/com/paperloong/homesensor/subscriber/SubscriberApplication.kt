package com.paperloong.homesensor.subscriber

import org.springframework.boot.WebApplicationType
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories

@SpringBootApplication
@ConfigurationPropertiesScan
@EnableMongoRepositories
class SubscriberApplication

fun main(args: Array<String>) {
    runApplication<SubscriberApplication>(*args) {
        webApplicationType = WebApplicationType.NONE
    }
}

