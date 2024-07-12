from typing import Dict, Any

from beanie import Document
from pydantic import Field


class SensorData(Document):
    sensorId: str = Field(alias='sensor_id')
    type: str
    timestamp: int
    data: Dict[str, Any] = Field(default_factory=dict)

    class Settings:
        name = 'sensor_data'
