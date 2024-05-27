package site.timecapsulearchive.core.infra.s3.manager;

public final class S3UrlGenerator {

    private S3UrlGenerator() {
    }

    public static String generateFileName(
        final Long memberId,
        final String directory,
        final String fileName
    ) {
        return directory + "/" + memberId + "/" + fileName;
    }
}
