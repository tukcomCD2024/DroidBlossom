package site.timecapsulearchive.core.domain.capsule.entity;

public enum CapsuleType {
    SECRET,
    PUBLIC,
    GROUP,
    TREASURE,
    ALL;

    public boolean isPublicOrGroup() {
        return this == PUBLIC || this == GROUP;
    }
}
