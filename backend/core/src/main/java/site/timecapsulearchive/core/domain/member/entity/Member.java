package site.timecapsulearchive.core.domain.member.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.timecapsulearchive.core.domain.capsule.entity.Capsule;
import site.timecapsulearchive.core.domain.friend.entity.FriendInvite;
import site.timecapsulearchive.core.domain.friend.entity.MemberFriend;
import site.timecapsulearchive.core.domain.group.entity.GroupInvite;
import site.timecapsulearchive.core.domain.group.entity.MemberGroup;
import site.timecapsulearchive.core.domain.history.entity.History;
import site.timecapsulearchive.core.global.entity.BaseEntity;
import site.timecapsulearchive.core.global.util.NullCheck;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "MEMBER")
public class Member extends BaseEntity {

    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "phone")
    private byte[] phone;

    @Column(name = "phone_hash", unique = true)
    private byte[] phone_hash;

    @Column(name = "profile_url", nullable = false)
    private String profileUrl;

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Column(name = "social_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private SocialType socialType;

    @Column(name = "notification_enabled", nullable = false)
    private Boolean notificationEnabled;

    @Email
    @Column(name = "email", nullable = false)
    private String email;

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

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Capsule> capsules;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MemberGroup> groups;

    @OneToMany(mappedBy = "friend", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MemberFriend> friends;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FriendInvite> friendsRequests;

    @OneToMany(mappedBy = "friend", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FriendInvite> notifications;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<History> histories;

    @Builder
    private Member(String profileUrl, String nickname, SocialType socialType, String email,
        String authId, String password, String tag, byte[] phone, byte[] phone_hash) {
        this.profileUrl = NullCheck.validate(profileUrl, "Entity: profile");
        this.nickname = NullCheck.validate(nickname, "Entity: nickname");
        this.socialType = NullCheck.validate(socialType, "Entity: socialType");
        this.email = NullCheck.validate(email, "Entity: email");
        this.tag = NullCheck.validate(tag, "Entity: tag");
        this.authId = NullCheck.validate(authId, "Entity: authId");
        this.isVerified = true;
        this.notificationEnabled = false;
        this.password = password;
        this.phone = phone;
        this.phone_hash = phone_hash;
    }

}
