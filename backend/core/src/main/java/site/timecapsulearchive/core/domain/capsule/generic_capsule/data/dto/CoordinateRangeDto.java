package site.timecapsulearchive.core.domain.capsule.generic_capsule.data.dto;

public record CoordinateRangeDto(
    double latitude,
    double longitude,
    double distance
) {

    public static CoordinateRangeDto from(
        double latitude,
        double longitude,
        double distance
    ) {
        return new CoordinateRangeDto(latitude, longitude, distance);
    }
}
