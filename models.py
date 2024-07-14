from typing import Dict, Any

from beanie import Document, Indexed
from pydantic import Field


class SensorData(Document):
    sensor_id: Indexed(str)
    type: str
    timestamp: int
    data: Dict[str, Any] = Field(default_factory=dict)

    class Settings:
        name = 'sensor_data'
