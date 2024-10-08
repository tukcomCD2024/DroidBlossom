package site.timecapsulearchive.core.common.fixture.domain;

import java.util.List;
import java.util.stream.IntStream;
import site.timecapsulearchive.core.domain.capsule.entity.Capsule;
import site.timecapsulearchive.core.domain.capsule.entity.Image;
import site.timecapsulearchive.core.domain.member.entity.Member;

public class ImageFixture {

    public static List<Image> testImages(Member member, Capsule capsule, int count) {
        return IntStream.range(0, count)
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
