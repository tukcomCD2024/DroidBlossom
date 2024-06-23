package site.timecapsulearchive.core.domain.member.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import site.timecapsulearchive.core.global.entity.BaseEntity;

@Entity
@Table(name = "notification_category")
@Getter
@SQLDelete(sql = "UPDATE notification_category SET is_deleted = true WHERE notification_category_id = ?")
@Where(clause = "is_deleted = false")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NotificationCategory extends BaseEntity {

    @Id
    @Column(name = "notification_category_id", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "category_name", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private CategoryName categoryName;

    @Column(name = "category_description", nullable = false)
    private String categoryDescription;

    @Column(name = "is_deleted")
    private boolean is_deleted = Boolean.FALSE;
}

