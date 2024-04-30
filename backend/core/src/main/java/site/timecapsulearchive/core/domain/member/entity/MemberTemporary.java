package site.timecapsulearchive.core.domain.member.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.timecapsulearchive.core.global.entity.BaseEntity;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "MEMBER_TEMPORARY")
public class MemberTemporary extends BaseEntity {

    @Id
    @Column(name = "member_temporary_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "profile_url", nullable = false)
    private String profileUrl;

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Column(name = "social_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private SocialType socialType;

    @Email
    @Column(name = "email", nullable = false)
    private String email;
    @Column(name = "is_verified", nullable = false)
    private Boolean isVerified;

    @Column(name = "auth_id", nullable = false, unique = true)
    private String authId;

    @Column(name = "tag", nullable = false, unique = true)
    private String tag;

    @Builder
    public MemberTemporary(String profileUrl, String nickname, SocialType socialType, String email,
        String authId, String tag) {
        this.profileUrl = Objects.requireNonNull(profileUrl);
        this.nickname = Objects.requireNonNull(nickname);
        this.socialType = Objects.requireNonNull(socialType);
        this.email = Objects.requireNonNull(email);
        this.isVerified = false;
        this.authId = Objects.requireNonNull(authId);
        this.tag = Objects.requireNonNull(tag);
    }


}
