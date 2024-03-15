from datetime import datetime
from typing import Optional

from sqlalchemy import String, ForeignKey, func
from sqlalchemy.orm import mapped_column, Mapped, relationship

from application.model.base import Base
from application.model.member import Member


class CapsuleSkin(Base):
    __tablename__ = 'capsule_skin'

    capsule_skin_id: Mapped[int] = mapped_column(autoincrement=True,
                                                 primary_key=True)
    skin_name: Mapped[str] = mapped_column(String())
    image_url: Mapped[str] = mapped_column(String())
    motion_name: Mapped[Optional[str]] = mapped_column(String())
    retarget: Mapped[Optional[str]] = mapped_column(String())

    member_id: Mapped[int] = mapped_column(ForeignKey('member.member_id'))

    created_at: Mapped[datetime] = mapped_column(default=func.now())
    updated_at: Mapped[datetime] = mapped_column(default=func.now(),
                                                 onupdate=func.now())

    member: Mapped["Member"] = relationship(foreign_keys=member_id)

    def __repr__(self) -> str:
        return f"CapsuleSkin(id={self.id!r}, name={self.skin_name!r}, image_url={self.image_url!r}, motion_name={self.motion_name!r}, retarget={self.retarget!r}), member_id={self.member_id!r}"
