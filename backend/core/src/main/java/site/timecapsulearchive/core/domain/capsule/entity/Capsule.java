package site.timecapsulearchive.core.domain.capsule.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.ZonedDateTime;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.timecapsulearchive.core.domain.capsuleskin.entity.CapsuleSkin;
import site.timecapsulearchive.core.domain.group.entity.Group;
import site.timecapsulearchive.core.domain.member.entity.Member;
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

    @Column(name = "longitude", nullable = false)
    private Float longitude;

    @Column(name = "latitude", nullable = false)
    private Float latitude;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "is_opened", nullable = false)
    private Boolean isOpened;

    @Embedded
    private Address address;

    @OneToMany(mappedBy = "capsule", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> images;

    @OneToMany(mappedBy = "capsule", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Video> videos;

    @OneToMany(mappedBy = "capsule", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GroupCapsuleOpen> groupCapsuleOpens;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "capsule_skin_id", nullable = false)
    private CapsuleSkin capsuleSkin;
}
