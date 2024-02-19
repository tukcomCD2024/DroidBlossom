from datetime import datetime

from application.model.base import Base
from sqlalchemy import String, Text, Integer, DateTime
from sqlalchemy.orm import mapped_column, Mapped


class FailedTask(Base):
    __tablename__ = 'failed_task'

    id: Mapped[int] = mapped_column(primary_key=True, autoincrement=True)
    name: Mapped[str] = mapped_column(String())
    full_name: Mapped[str] = mapped_column(String())
    args: Mapped[str] = mapped_column(Text())
    kwargs: Mapped[str] = mapped_column(Text())
    exception_class: Mapped[str] = mapped_column(Text())
    exception_msg: Mapped[str] = mapped_column(Text())
    traceback: Mapped[str] = mapped_column(Text())
    celery_task_id: Mapped[str] = mapped_column(String())
    failures: Mapped[int] = mapped_column(Integer())

    created_at: Mapped[datetime] = mapped_column(DateTime)
    updated_at: Mapped[datetime] = mapped_column(DateTime)
