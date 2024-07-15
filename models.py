from enum import StrEnum
from typing import Dict, Any

from beanie import Document, Indexed
from pydantic import Field


class DataType(StrEnum):
    THP = 'thp'
    ILLUMINANCE = 'illuminance'


class SensorData(Document):
    sensor_id: Indexed(str)
    type: DataType
    data: Dict[str, Any] = Field(default_factory=dict)
    timestamp: int

    class Settings:
        name = 'sensor_data'
