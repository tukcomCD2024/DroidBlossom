package site.timecapsulearchive.notification.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import site.timecapsulearchive.notification.global.entity.BaseEntity;

@Entity
@Table(name = "notification")
@Getter
@SQLDelete(sql = "UPDATE notification SET deleted_at = now() WHERE notification_id = ?")
@Where(clause = "deleted_at is null")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification extends BaseEntity {

    @Id
    @Column(name = "notification_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "text", nullable = false)
    private String text;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private NotificationStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notification_category_id", nullable = false)
    private NotificationCategory notificationCategory;

    @Builder
    private Notification(String title, String text, String imageUrl, Long memberId,
        NotificationStatus status, NotificationCategory notificationCategory) {
        this.title = title;
        this.text = text;
        this.imageUrl = imageUrl;
        this.memberId = memberId;
        this.status = status;
        this.notificationCategory = notificationCategory;
    }
}
