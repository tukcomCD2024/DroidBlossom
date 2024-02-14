package site.timecapsulearchive.core.domain.capsuleskin.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
    private String motionName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Builder
    private CapsuleSkin(String skinName, String imageUrl, String motionName, Member member) {
        this.skinName = skinName;
        this.imageUrl = imageUrl;
        this.motionName = motionName;
        this.member = member;
    }
}
