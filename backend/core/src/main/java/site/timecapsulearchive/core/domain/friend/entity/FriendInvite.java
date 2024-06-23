package site.timecapsulearchive.core.domain.friend.entity;

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
import site.timecapsulearchive.core.domain.member.entity.Member;
import site.timecapsulearchive.core.global.entity.BaseEntity;

@Entity
@Table(name = "friend_invite")
@Getter
@SQLDelete(sql = "UPDATE `friend_invite` SET is_deleted = true WHERE friend_invite_id = ?")
@Where(clause = "is_deleted = false")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FriendInvite extends BaseEntity {

    @Id
    @Column(name = "friend_invite_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "is_deleted")
    private boolean is_deleted = Boolean.FALSE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private Member owner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "friend_id", nullable = false)
    private Member friend;

    private FriendInvite(Member owner, Member friend) {
        this.owner = owner;
        this.friend = friend;
    }

    public static FriendInvite createOf(Member owner, Member friend) {
        return new FriendInvite(owner, friend);
    }

    public MemberFriend friendRelation() {
        return MemberFriend.builder()
            .owner(owner)
            .friend(friend)
            .build();
    }

    public MemberFriend ownerRelation() {
        return MemberFriend.builder()
            .owner(friend)
            .friend(owner)
            .build();
    }
}
