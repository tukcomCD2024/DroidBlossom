package site.timecapsulearchive.core.common.data;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import site.timecapsulearchive.core.domain.friend.entity.FriendInvite;
import site.timecapsulearchive.core.domain.friend.entity.FriendStatus;
import site.timecapsulearchive.core.domain.friend.entity.MemberFriend;
import site.timecapsulearchive.core.domain.member.entity.Member;
import site.timecapsulearchive.core.domain.member.entity.SocialType;
import site.timecapsulearchive.core.global.security.encryption.HashEncryptionManager;
import site.timecapsulearchive.core.global.security.encryption.HashProperties;

public class MemberTestDataRepository {

    private final HashEncryptionManager hash = new HashEncryptionManager(
        new HashProperties("test"));

    public Member insertAndGetMember(EntityManager entityManager, int dataPrefix) {
        Member member = Member.builder()
            .socialType(SocialType.GOOGLE)
            .nickname(dataPrefix + "testNickname")
            .email(dataPrefix + "test@google.com")
            .authId(dataPrefix + "test")
            .profileUrl(dataPrefix + "test.com")
            .tag(dataPrefix + "testTag")
            .build();

        byte[] number = getPhoneBytes(dataPrefix);
        member.updatePhoneHash(hash.encrypt(number));

        entityManager.persist(member);

        return member;
    }

    public byte[] getPhoneBytes(int dataPrefix) {
        return ("0" + (1000000000 + dataPrefix)).getBytes(StandardCharsets.UTF_8);
    }

    public void insertNotificationCategory(EntityManager entityManager) {
        Query notificationCategoryInsert = entityManager.createNativeQuery(
            "INSERT INTO notification_category(notification_category_id, category_name, category_description, created_at, updated_at) VALUES (?, ?, ?, ?, ?)");

        ZonedDateTime now = ZonedDateTime.now();

        notificationCategoryInsert.setParameter(1, 1L);
        notificationCategoryInsert.setParameter(2, "test_category");
        notificationCategoryInsert.setParameter(3, "test_category");
        notificationCategoryInsert.setParameter(4, now);
        notificationCategoryInsert.setParameter(5, now);

        notificationCategoryInsert.executeUpdate();
    }

    public void insertNotification(EntityManager entityManager, Long memberId, int size) {
        for (int count = 0; count < size; count++) {
            Query notificationInsert = entityManager.createNativeQuery(
                "INSERT INTO notification(title, text, image_url, member_id, notification_category_id,created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?)");

            String imageUrl = null;
            if (count % 2 == 0) {
                imageUrl = "image";
            }

            ZonedDateTime now = ZonedDateTime.now();
            notificationInsert.setParameter(1, "title");
            notificationInsert.setParameter(2, "text");
            notificationInsert.setParameter(3, imageUrl);
            notificationInsert.setParameter(4, memberId);
            notificationInsert.setParameter(5, 1L);
            notificationInsert.setParameter(6, now);
            notificationInsert.setParameter(7, now);

            notificationInsert.executeUpdate();
        }
    }

    public void insertMemberFriend(EntityManager entityManager, Member owner, Member friend) {
        MemberFriend memberFriend = MemberFriend.builder()
            .friend(friend)
            .owner(owner)
            .build();
        entityManager.persist(memberFriend);
    }

    public void insertFriendInvite(EntityManager entityManager, Member owner, Member friend) {
        FriendInvite friendInvite = FriendInvite.builder()
            .friend(friend)
            .owner(owner)
            .friendStatus(FriendStatus.PENDING)
            .build();
        entityManager.persist(friendInvite);
    }
}
