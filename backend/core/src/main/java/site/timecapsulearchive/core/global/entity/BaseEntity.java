package site.timecapsulearchive.core.global.entity;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import java.time.ZonedDateTime;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@EntityListeners(value = {AuditingEntityListener.class})
@MappedSuperclass
public abstract class BaseEntity {

    @CreatedDate
    private ZonedDateTime createdAt;

    @LastModifiedDate
    private ZonedDateTime updatedAt;
}
