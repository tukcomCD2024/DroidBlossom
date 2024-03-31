package site.timecapsulearchive.core.common.data.fixture;

import java.util.List;
import java.util.stream.IntStream;
import site.timecapsulearchive.core.domain.capsule.entity.Capsule;
import site.timecapsulearchive.core.domain.capsule.entity.Image;
import site.timecapsulearchive.core.domain.member.entity.Member;

public class ImageFixture {

    public static List<Image> testImages(Member member, Capsule capsule) {
        return IntStream.range(0, 5)
            .mapToObj(
                dataPrefix -> Image.builder()
                    .imageUrl(dataPrefix + "testImageURl")
                    .member(member)
                    .capsule(capsule)
                    .build()
            )
            .toList();
    }

}
