package site.timecapsulearchive.core.domain.friend.entity;

import jakarta.persistence.CascadeType;
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
import site.timecapsulearchive.core.domain.member.entity.Member;
import site.timecapsulearchive.core.global.entity.BaseEntity;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "FRIEND_INVITE")
public class FriendInvite extends BaseEntity {

    @Id
    @Column(name = "friend_invite_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "owner_id", nullable = false)
    private Member owner;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
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
