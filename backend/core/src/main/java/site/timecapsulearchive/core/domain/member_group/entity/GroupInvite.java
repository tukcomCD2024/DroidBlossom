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
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import site.timecapsulearchive.core.domain.group.entity.Group;
import site.timecapsulearchive.core.domain.member.entity.Member;
import site.timecapsulearchive.core.global.entity.BaseEntity;

@Entity
@Table(name = "group_invite")
@Getter
@SQLDelete(sql = "UPDATE `group_invite` SET is_deleted = true WHERE group_invite_id = ?")
@Where(clause = "is_deleted = false")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GroupInvite extends BaseEntity {

    @Id
    @Column(name = "group_invite_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "is_deleted")
    private boolean is_deleted = Boolean.FALSE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_owner_id", nullable = false)
    private Member groupOwner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_member_id", nullable = false)
    private Member groupMember;

    private GroupInvite(Group group, Member groupOwner, Member groupMember) {
        this.group = group;
        this.groupOwner = groupOwner;
        this.groupMember = groupMember;
    }

    public static GroupInvite createOf(Group group, Member groupOwner, Member groupMember) {
        return new GroupInvite(group, groupOwner, groupMember);
    }
}
