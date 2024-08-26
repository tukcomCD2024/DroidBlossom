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
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import site.timecapsulearchive.core.global.entity.BaseEntity;
import site.timecapsulearchive.core.global.util.NullCheck;

@Entity
@Table(name = "member")
@Getter
@SQLDelete(sql = "UPDATE member SET deleted_at = now() WHERE member_id = ?")
@Where(clause = "deleted_at is null")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "phone")
    private byte[] phone;

    @Column(name = "phone_hash", unique = true)
    private byte[] phoneHash;

    @Column(name = "profile_url", nullable = false)
    private String profileUrl;

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Column(name = "social_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private SocialType socialType;

    @Column(name = "notification_enabled", nullable = false)
    private Boolean notificationEnabled;

    @Column(name = "email", nullable = false)
    private byte[] email;

    @Column(name = "email_hash", nullable = false)
    private byte[] emailHash;

    @Column(name = "fcm_token")
    private String fcmToken;

    @Column(name = "is_verified", nullable = false)
    private Boolean isVerified;

    @Column(name = "auth_id", nullable = false, unique = true)
    private String authId;

    @Column(name = "password")
    private String password;

    @Column(name = "tag", nullable = false, unique = true)
    private String tag;

    @Column(name = "declaration_count")
    private Long declarationCount = 0L;

    @Column(name = "tag_search_available", nullable = false)
    private Boolean tagSearchAvailable = Boolean.TRUE;

    @Column(name = "phone_search_available", nullable = false)
    private Boolean phoneSearchAvailable = Boolean.FALSE;

    @Builder
    private Member(String profileUrl, String nickname, SocialType socialType,
        String authId, String password, String tag, byte[] phone, byte[] phoneHash,
        byte[] email, byte[] emailHash
    ) {
        this.profileUrl = NullCheck.validate(profileUrl, "Entity: profile");
        this.nickname = NullCheck.validate(nickname, "Entity: nickname");
        this.socialType = NullCheck.validate(socialType, "Entity: socialType");
        this.tag = NullCheck.validate(tag, "Entity: tag");
        this.authId = NullCheck.validate(authId, "Entity: authId");
        this.isVerified = true;
        this.notificationEnabled = false;
        this.password = password;
        this.phone = phone;
        this.phoneHash = phoneHash;
        this.email = email;
        this.emailHash = emailHash;
    }

    public void updateData(String nickname, String tag) {
        this.nickname = nickname;
        this.tag = tag;
    }

    public void upDeclarationCount() {
        declarationCount++;
    }
}
