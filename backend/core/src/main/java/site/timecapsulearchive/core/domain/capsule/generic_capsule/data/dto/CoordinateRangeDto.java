package site.timecapsulearchive.core.domain.capsule.generic_capsule.data.dto;

public record CoordinateRangeDto(
    double latitude,
    double longitude,
    double distance
) {

    public static CoordinateRangeDto from(
        final double latitude,
        final double longitude,
        final double distance
    ) {
        return new CoordinateRangeDto(latitude, longitude, distance);
    }
}
