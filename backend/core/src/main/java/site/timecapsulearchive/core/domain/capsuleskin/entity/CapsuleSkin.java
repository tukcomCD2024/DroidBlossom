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
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import site.timecapsulearchive.core.domain.member.entity.Member;
import site.timecapsulearchive.core.global.entity.BaseEntity;

@Entity
@Table(name = "capsule_skin")
@Getter
@SQLDelete(sql = "UPDATE `capsule_skin` SET deleted_at = now() WHERE caspuel_skin_id = ?")
@Where(clause = "deleted_at is null")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    private CapsuleSkin(String skinName, String imageUrl, Member member) {
        this.skinName = skinName;
        this.imageUrl = imageUrl;
        this.member = member;
    }

    public static CapsuleSkin captureTreasureCapsuleSkin(String imageUrl, Member member) {
        return new CapsuleSkin("보물 캡슐에서 얻은 스킨", imageUrl, member);
    }
}
