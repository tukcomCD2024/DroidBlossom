package site.timecapsulearchive.core.domain.capsule.entity;

public enum CapsuleType {
    SECRET,
    PUBLIC,
    GROUP,
    TREASURE,
    ALL;

    public boolean isGroupCapsule() {
        return this.equals(CapsuleType.GROUP);
    }

}
