import asyncio
import logging

import paho.mqtt.client as mqtt
from beanie import init_beanie
from motor.motor_asyncio import AsyncIOMotorClient

import models
from setting import settings

logging.basicConfig(level=logging.INFO, format='%(asctime)s - %(levelname)s - %(message)s')

mongo_client = AsyncIOMotorClient(settings.mongo_url)


async def process_message(topic: str, message: str):
    logging.info("Processing message...")
    try:
        sensor_data = models.SensorData.model_validate_json(message)
        await sensor_data.insert()
        logging.info(f"Message inserted with id {sensor_data.id}")
    except Exception as e:
        logging.error(f"Error processing message: {e}")


class MQTTClient:
    def __init__(self, loop):
        self.loop = loop
        self.client = mqtt.Client(
            callback_api_version=mqtt.CallbackAPIVersion.VERSION2,
            client_id=settings.mqtt_client_id
        )
        self.client.username_pw_set(settings.mqtt_username, settings.mqtt_password)
        self.client.on_connect = self.on_connect
        self.client.on_message = self.on_message

    def on_connect(self, client, userdata, flags, reason_code, properties):
        if reason_code == mqtt.MQTT_ERR_SUCCESS:
            logging.info(f"Connected to MQTT broker {settings.mqtt_host}:{settings.mqtt_port}")
            topic = settings.mqtt_subscriber_topic
            result, mid = self.client.subscribe(topic)
            if result == mqtt.MQTT_ERR_SUCCESS:
                logging.info(f"Subscribed to topic {topic}")
            else:
                logging.error(f"Error subscribing to topic [{topic}] {result} {mid}")
        else:
            logging.info(f"Failed to connect to MQTT broker {settings.mqtt_host}:{settings.mqtt_port} {reason_code}")

    def on_message(self, client, userdata, msg):
        topic = msg.topic
        message = msg.payload.decode('utf-8')
        logging.info(f"Received message from [{topic}] - {message}")
        try:
            asyncio.run_coroutine_threadsafe(process_message(topic, message), self.loop)
        except Exception as e:
            logging.error(f"Error handling message: {e}")

    async def connect(self):
        logging.info("Connecting to MQTT broker")
        try:
            self.client.connect(settings.mqtt_host, port=settings.mqtt_port)
            self.client.loop_start()
        except Exception as e:
            logging.error(f"Error connecting to MQTT broker: {e}")
            raise

    async def disconnect(self):
        logging.info("Disconnecting from MQTT broker")
        try:
            self.client.loop_stop()
            self.client.disconnect()
            logging.info("Disconnected from MQTT broker")
        except Exception as e:
            logging.error(f"Error disconnecting from MQTT broker: {e}")


async def main():
    try:
        logging.info("Initializing Database")
        await init_beanie(database=mongo_client.get_default_database(), document_models=[models.SensorData])
        logging.info("Database initialized successfully.")

        loop = asyncio.get_running_loop()
        mqtt_client = MQTTClient(loop)
        await mqtt_client.connect()

        stop_event = asyncio.Event()

        async def wait_for_stop():
            await stop_event.wait()
            await mqtt_client.disconnect()
            logging.info("MQTT client stopped.")

        stop_task = loop.create_task(wait_for_stop())

        try:
            await stop_task
        except asyncio.CancelledError:
            pass

    except Exception as e:
        logging.error(f"Error in main: {e}")


if __name__ == "__main__":
    try:
        asyncio.run(main())
    except KeyboardInterrupt:
        logging.info("Exiting...")
