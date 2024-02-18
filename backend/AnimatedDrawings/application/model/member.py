from datetime import datetime
from typing import Optional

from application.model.base import Base
from sqlalchemy import func
from sqlalchemy.orm import mapped_column, Mapped


class Member(Base):
    __tablename__ = 'member'

    member_id: Mapped[int] = mapped_column(autoincrement=True, primary_key=True)
    is_verified: Mapped[bool] = mapped_column()
    notification_enabled: Mapped[bool] = mapped_column()
    phone: Mapped[str] = mapped_column()
    nickname: Mapped[str] = mapped_column()
    social_type: Mapped[str] = mapped_column()
    email: Mapped[str] = mapped_column()
    fcm_token: Mapped[Optional[str]] = mapped_column()
    profile_url: Mapped[Optional[str]] = mapped_column()
    auth_id: Mapped[str] = mapped_column()
    phone_hash: Mapped[str] = mapped_column()

    created_at: Mapped[datetime] = mapped_column(default=func.now())
    updated_at: Mapped[datetime] = mapped_column(default=func.now(),
                                                 onupdate=func.now())
