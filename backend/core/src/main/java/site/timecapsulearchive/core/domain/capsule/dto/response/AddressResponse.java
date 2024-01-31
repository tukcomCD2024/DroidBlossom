package site.timecapsulearchive.core.domain.capsule.dto.response;

public record AddressResponse(
    String address
) {

    public static AddressResponse from(String address) {
        return new AddressResponse(address);
    }
}
