package site.timecapsulearchive.core.infra.s3.data.dto;

public enum S3Directory {
    CAPSULE("capsuleContents"),
    CAPSULE_SKIN("capsuleSkin"),
    GROUP("group");

    private final String value;

    S3Directory(String value) {
        this.value = value;
    }

    public String generateFullPath(final Long memberId, final String fileName) {
        return value + "/" + memberId + "/" + fileName;
    }
}
