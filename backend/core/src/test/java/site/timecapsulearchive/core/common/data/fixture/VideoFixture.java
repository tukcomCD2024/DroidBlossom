package site.timecapsulearchive.core.common.data.fixture;

import java.util.List;
import java.util.stream.IntStream;
import site.timecapsulearchive.core.domain.capsule.entity.Capsule;
import site.timecapsulearchive.core.domain.capsule.entity.Video;
import site.timecapsulearchive.core.domain.member.entity.Member;

public class VideoFixture {

    public static List<Video> testVideos(Member member, Capsule capsule, int count) {
        return IntStream.range(0, count)
            .mapToObj(
                dataPrefix -> Video.builder()
                    .videoUrl(dataPrefix + "testVideoURl")
                    .member(member)
                    .capsule(capsule)
                    .build()
            )
            .toList();
    }

}
