package site.timecapsulearchive.core.domain.capsule.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.timecapsulearchive.core.domain.member.entity.Member;
import site.timecapsulearchive.core.global.entity.BaseEntity;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "GROUP_CAPSULE_OPEN")
public class GroupCapsuleOpen extends BaseEntity {

    @Id
    @Column(name = "group_capsule_open_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "is_opened", nullable = false)
    private Boolean isOpened;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "capsule_id", nullable = false)
    private Capsule capsule;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Builder
    private GroupCapsuleOpen(Boolean isOpened, Capsule capsule, Member member) {
        this.isOpened = Objects.requireNonNull(isOpened);
        this.capsule = Objects.requireNonNull(capsule);
        this.member = Objects.requireNonNull(member);
    }

    public static GroupCapsuleOpen createOf(Member member, Capsule capsule, Boolean isOpened) {
        return new GroupCapsuleOpen(isOpened, capsule, member);
    }

    public void open() {
        this.isOpened = Boolean.TRUE;
    }
}
