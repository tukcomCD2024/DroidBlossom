package site.timecapsulearchive.core.global.api.health;

public record HealthResponse(
    String message
) {

    public static HealthResponse ok() {
        return new HealthResponse("ok");
    }
}
