package site.timecapsulearchive.core.domain.capsule;

public record CoordinateRangeRequestDto(
    double latitude,
    double longitude,
    double distance
) {

    public static CoordinateRangeRequestDto from(
        double latitude,
        double longitude,
        double distance
    ) {
        return new CoordinateRangeRequestDto(latitude, longitude, distance);
    }
}
