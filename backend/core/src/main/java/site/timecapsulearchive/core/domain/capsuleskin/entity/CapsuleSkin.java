package site.timecapsulearchive.core.domain.capsuleskin.entity;

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
import site.timecapsulearchive.core.domain.member.entity.Member;
import site.timecapsulearchive.core.global.entity.BaseEntity;
import site.timecapsulearchive.core.global.error.exception.EntityCreateRestrictionException;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "CAPSULE_SKIN")
public class CapsuleSkin extends BaseEntity {

    @Id
    @Column(name = "capsule_skin_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "skin_name", nullable = false)
    private String skinName;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    @Column(name = "motion_name")
    @Enumerated(EnumType.STRING)
    private Motion motionName;

    @Column(name = "retarget")
    @Enumerated(EnumType.STRING)
    private Retarget retarget;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Builder
    private CapsuleSkin(String skinName, String imageUrl, Motion motionName, Member member,
        Retarget retarget) {
        this.skinName = skinName;
        this.imageUrl = imageUrl;
        this.motionName = motionName;
        this.member = member;
        this.retarget = retarget;
    }

    /**
     * <code>CapsuleSkin</code>을 생성하는 정적 팩토리 메소드이다.
     *
     * @param skinName 스킨 이름
     * @param fullPath 완전한 캡슐 스킨 이미지 경로
     * @param member   캡슐 스킨을 생성한 멤버
     * @return <code>CapsuleSkin</code>
     */
    public static CapsuleSkin createOf(String skinName, String fullPath, Member member) {
        if (fullPath.isBlank() || skinName.isBlank() || member == null) {
            throw new EntityCreateRestrictionException(CapsuleSkin.class.getSimpleName(), skinName,
                fullPath, member);
        }

        return new CapsuleSkin(skinName, fullPath, null, member, null);
    }
}
