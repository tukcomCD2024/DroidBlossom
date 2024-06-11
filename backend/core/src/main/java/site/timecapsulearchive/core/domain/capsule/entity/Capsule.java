package site.timecapsulearchive.core.domain.capsule.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;
import site.timecapsulearchive.core.domain.capsule.exception.GroupCapsuleOpenNotFoundException;
import site.timecapsulearchive.core.domain.capsuleskin.entity.CapsuleSkin;
import site.timecapsulearchive.core.domain.group.entity.Group;
import site.timecapsulearchive.core.domain.member.entity.Member;
import site.timecapsulearchive.core.global.common.supplier.ZonedDateTimeSupplier;
import site.timecapsulearchive.core.global.entity.BaseEntity;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "CAPSULE")
public class Capsule extends BaseEntity {

    @Id
    @Column(name = "capsule_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "due_date")
    private ZonedDateTime dueDate;

    @Column(name = "point", columnDefinition = "SRID 3857")
    private Point point;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private CapsuleType type;

    @Column(name = "is_opened", nullable = false)
    private Boolean isOpened;

    @Embedded
    private Address address;

    @OneToMany(mappedBy = "capsule", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Image> images = new ArrayList<>();

    @OneToMany(mappedBy = "capsule", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Video> videos = new ArrayList<>();

    @OneToMany(mappedBy = "capsule", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<GroupCapsuleOpen> groupCapsuleOpens = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private Group group;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "capsule_skin_id", nullable = false)
    private CapsuleSkin capsuleSkin;

    @Builder
    private Capsule(
        ZonedDateTime dueDate,
        String title,
        String content,
        CapsuleType type,
        Address address,
        Point point,
        Member member,
        CapsuleSkin capsuleSkin,
        Group group
    ) {
        this.dueDate = dueDate;
        this.title = title;
        this.content = content;
        this.point = point;
        this.type = type;
        this.isOpened = Boolean.FALSE;
        this.address = address;
        this.group = group;
        this.member = member;
        this.capsuleSkin = capsuleSkin;
    }

    public boolean isNotCapsuleOpened() {
        if (dueDate == null) {
            return false;
        }

        return dueDate.isAfter(ZonedDateTimeSupplier.utc().get());
    }

    public void open() {
        this.isOpened = Boolean.TRUE;
    }

    public boolean isTimeCapsule() {
        return dueDate != null;
    }

    public boolean canOpen() {
        return dueDate == null || dueDate.isBefore(ZonedDateTimeSupplier.utc().get());
    }

    public boolean isAllGroupMemberOpened(Long memberId, Long capsuleId) {
        if (groupCapsuleOpens.isEmpty()) {
            throw new GroupCapsuleOpenNotFoundException();
        }

        return groupCapsuleOpens.stream()
            .allMatch(groupCapsuleOpen -> {
                if (groupCapsuleOpen.matched(capsuleId, memberId)) {
                    groupCapsuleOpen.open();
                }

                return groupCapsuleOpen.getIsOpened();
            });
    }
}