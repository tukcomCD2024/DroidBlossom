package site.timecapsulearchive.core.domain.capsule.entity;

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
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.locationtech.jts.geom.Point;
import site.timecapsulearchive.core.domain.capsule.exception.GroupCapsuleOpenNotFoundException;
import site.timecapsulearchive.core.domain.capsule.exception.NoCapsuleAuthorityException;
import site.timecapsulearchive.core.domain.capsuleskin.entity.CapsuleSkin;
import site.timecapsulearchive.core.domain.group.entity.Group;
import site.timecapsulearchive.core.domain.member.entity.Member;
import site.timecapsulearchive.core.global.common.supplier.ZonedDateTimeSupplier;
import site.timecapsulearchive.core.global.entity.BaseEntity;

@Entity
@Table(name = "capsule")
@Getter
@SQLDelete(sql = "UPDATE `capsule` SET deleted_at = now() WHERE capsule_id = ?")
@Where(clause = "deleted_at is null")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Capsule extends BaseEntity {

    @OneToMany(mappedBy = "capsule")
    private final List<Image> images = new ArrayList<>();
    @OneToMany(mappedBy = "capsule")
    private final List<Video> videos = new ArrayList<>();
    @OneToMany(mappedBy = "capsule")
    private final List<GroupCapsuleOpen> groupCapsuleOpens = new ArrayList<>();
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
    @Column(name = "declaration_count")
    private Long declarationCount = 0L;
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

    public boolean isNotTimeCapsule() {
        return dueDate == null;
    }

    public boolean isAllGroupMemberOpened(Long memberId, Long capsuleId) {
        if (groupCapsuleOpens.isEmpty()) {
            throw new GroupCapsuleOpenNotFoundException();
        }

        boolean isCapsuleOpened = true;
        for (GroupCapsuleOpen groupCapsuleOpen : groupCapsuleOpens) {
            if (groupCapsuleOpen.matched(capsuleId, memberId)) {
                groupCapsuleOpen.open();
            }

            isCapsuleOpened = isCapsuleOpened && groupCapsuleOpen.getIsOpened();
        }

        return isCapsuleOpened;
    }


    public void upDeclarationCount() {
        if (type.isPublicOrGroup()) {
            declarationCount++;

            return;
        }
        throw new NoCapsuleAuthorityException();
    }
}