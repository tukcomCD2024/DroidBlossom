package site.timecapsulearchive.core.domain.member_group.entity;

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
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import site.timecapsulearchive.core.domain.group.entity.Group;
import site.timecapsulearchive.core.domain.member.entity.Member;
import site.timecapsulearchive.core.domain.member_group.exception.GroupQuitException;
import site.timecapsulearchive.core.global.entity.BaseEntity;

@Entity
@Getter
@Table(name = "member_group")
@SQLDelete(sql = "UPDATE member_group SET is_deleted = true WHERE member_group_id = ?")
@Where(clause = "is_deleted = false")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberGroup extends BaseEntity {

    @Id
    @Column(name = "member_group_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "is_owner", nullable = false)
    private Boolean isOwner;

    @Column(name = "is_deleted")
    private boolean is_deleted = Boolean.FALSE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    private MemberGroup(Boolean isOwner, Member member, Group group) {
        this.isOwner = isOwner;
        this.member = member;
        this.group = group;
    }

    public static MemberGroup createGroupOwner(Member member, Group group) {
        return new MemberGroup(true, member, group);
    }

    public static MemberGroup createGroupMember(Member member, Group group) {
        return new MemberGroup(false, member, group);
    }

    public void checkGroupMemberOwner() {
        if (this.isOwner) {
            throw new GroupQuitException();
        }
    }
}
