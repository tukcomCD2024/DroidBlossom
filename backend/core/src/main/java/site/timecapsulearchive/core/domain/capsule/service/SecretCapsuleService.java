package site.timecapsulearchive.core.domain.capsule.service;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import site.timecapsulearchive.core.domain.capsule.dto.MediaSaveDto;
import site.timecapsulearchive.core.domain.capsule.dto.mapper.CapsuleMapper;
import site.timecapsulearchive.core.domain.capsule.dto.response.MyCapsulePageResponse;
import site.timecapsulearchive.core.domain.capsule.dto.secret_c.MySecreteCapsuleDto;
import site.timecapsulearchive.core.domain.capsule.dto.secret_c.SecretCapsuleCreateRequestDto;
import site.timecapsulearchive.core.domain.capsule.dto.secret_c.SecretCapsuleDetailDto;
import site.timecapsulearchive.core.domain.capsule.dto.secret_c.SecretCapsuleSummaryDto;
import site.timecapsulearchive.core.domain.capsule.dto.secret_c.response.SecretCapsuleDetailResponse;
import site.timecapsulearchive.core.domain.capsule.entity.Capsule;
import site.timecapsulearchive.core.domain.capsule.exception.CapsuleNotFondException;
import site.timecapsulearchive.core.domain.capsule.repository.CapsuleQueryRepository;
import site.timecapsulearchive.core.domain.capsule.repository.CapsuleRepository;
import site.timecapsulearchive.core.domain.capsuleskin.entity.CapsuleSkin;
import site.timecapsulearchive.core.domain.capsuleskin.exception.CapsuleSkinNotFoundException;
import site.timecapsulearchive.core.domain.capsuleskin.repository.CapsuleSkinRepository;
import site.timecapsulearchive.core.domain.member.entity.Member;
import site.timecapsulearchive.core.domain.member.service.MemberService;
import site.timecapsulearchive.core.global.geography.GeoTransformer;
import site.timecapsulearchive.core.infra.s3.dto.S3PreSignedUrlDto;
import site.timecapsulearchive.core.infra.s3.dto.request.S3PreSignedUrlRequestDto;
import site.timecapsulearchive.core.infra.s3.service.S3PreSignedUrlManager;

@Service
@RequiredArgsConstructor
public class SecretCapsuleService {

    private final CapsuleQueryRepository capsuleQueryRepository;
    private final CapsuleRepository capsuleRepository;
    private final CapsuleSkinRepository capsuleSkinRepository;
    private final MemberService memberService;
    private final ImageService imageService;
    private final VideoService videoService;
    private final GeoTransformer geoTransformer;
    private final CapsuleMapper capsuleMapper;
    private final S3PreSignedUrlManager s3PreSignedUrlManager;

    /**
     * 멤버 아이디와 캡슐 생성 포맷을 받아서 캡슐을 생성한다.
     *
     * @param memberId 캡슐을 생성할 멤버 아이디
     * @param dto      캡슐 생성 요청 포맷
     */
    public void createCapsule(Long memberId, SecretCapsuleCreateRequestDto dto) {
        Member findMember = memberService.findMemberByMemberId(memberId);

        CapsuleSkin capsuleSkin = capsuleSkinRepository
            .findById(dto.capsuleSkinId())
            .orElseThrow(CapsuleSkinNotFoundException::new);

        Point point = geoTransformer.changePoint4326To3857(dto.latitude(), dto.longitude());

        Capsule capsule = capsuleMapper.requestDtoToEntity(
            dto, point,
            findMember,
            capsuleSkin
        );

        if (isNotTimeCapsule(capsule)) {
            capsule.open();
        }

        capsuleRepository.save(capsule);

        saveImage(dto, capsule, findMember);
        saveVideo(dto, capsule, findMember);
    }

    private boolean isNotTimeCapsule(Capsule capsule) {
        return capsule.getDueDate() == null;
    }

    private void saveImage(SecretCapsuleCreateRequestDto dto, Capsule capsule, Member findMember) {
        if (isImagesNotEmpty(dto)) {
            imageService.saveImage(MediaSaveDto.of(
                capsule,
                findMember,
                dto.directory(),
                dto.imageNames()
            ));
        }
    }

    private boolean isImagesNotEmpty(SecretCapsuleCreateRequestDto dto) {
        return dto.imageNames() != null && !dto.imageNames().isEmpty();
    }

    private void saveVideo(SecretCapsuleCreateRequestDto dto, Capsule capsule, Member findMember) {
        if (isVideosNotEmpty(dto)) {
            videoService.saveVideo(MediaSaveDto.of(
                capsule,
                findMember,
                dto.directory(),
                dto.videoNames()
            ));
        }
    }

    private boolean isVideosNotEmpty(SecretCapsuleCreateRequestDto dto) {
        return dto.videoNames() != null && !dto.videoNames().isEmpty();
    }

    /**
     * 멤버 아이디와 마지막 캡슐 생성 날짜를 받아서 내 페이지 비밀 캡슐을 조회한다.
     *
     * @param memberId  캡슐을 생성할 멤버 아이디
     * @param size      페이지 사이즈
     * @param createdAt 마지막 캡슐 생성 날짜
     * @return 내 페이지에서 비밀 캡슐을 조회한다.
     */
    public MyCapsulePageResponse findSecretCapsuleListByMemberId(
        Long memberId,
        int size,
        ZonedDateTime createdAt
    ) {
        Slice<MySecreteCapsuleDto> slice = capsuleQueryRepository
            .findSecretCapsuleSliceByMemberIdAndCreatedAt(memberId, size, createdAt);

        return capsuleMapper.capsuleDetailSliceToResponse(slice.getContent(), slice.hasNext());
    }

    /**
     * 멤버 아이디와 캡슐 아이디를 받아서 캡슐 요약 정보를 반환한다.
     *
     * @param memberId  멤버 아이디
     * @param capsuleId 캡슐 아이디
     * @return 캡슐 요약 정보
     */
    public SecretCapsuleSummaryDto findSecretCapsuleSummaryById(
        Long memberId,
        Long capsuleId
    ) {
        return capsuleQueryRepository.findSecretCapsuleSummaryByMemberIdAndCapsuleId(memberId,
                capsuleId)
            .orElseThrow(CapsuleNotFondException::new);
    }

    /**
     * 멤버 아이디와 캡슐 아이디를 받아서 캡슐 상세 정보를 반환한다.
     *
     * @param memberId  멤버 아이디
     * @param capsuleId 캡슐 아이디
     * @return 캡슐 상세 정보
     */
    public SecretCapsuleDetailResponse findSecretCapsuleDetailById(
        Long memberId,
        Long capsuleId
    ) {
        SecretCapsuleDetailDto dto = capsuleQueryRepository.findSecretCapsuleDetailByMemberIdAndCapsuleId(
                memberId,
                capsuleId)
            .orElseThrow(CapsuleNotFondException::new);

        if (capsuleNotOpened(dto)) {
            return capsuleMapper.notOpenedSecretCapsuleDetailDtoToResponse(dto);
        }

        S3PreSignedUrlDto s3UrlsForGet = s3PreSignedUrlManager.getS3PreSignedUrlsForGet(
            S3PreSignedUrlRequestDto.forGet(dto.images(), dto.videos())
        );

        return capsuleMapper.secretCapsuleDetailDtoToResponse(
            dto,
            s3UrlsForGet.preSignedImageUrls(),
            s3UrlsForGet.preSignedVideoUrls()
        );
    }

    private boolean capsuleNotOpened(SecretCapsuleDetailDto dto) {
        if (dto.dueDate() == null) {
            return false;
        }

        return !dto.isOpened() || dto.dueDate().isBefore(ZonedDateTime.now(ZoneOffset.UTC));
    }
}
