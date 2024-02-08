package site.timecapsulearchive.core.domain.capsule.service;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import site.timecapsulearchive.core.domain.capsule.entity.Capsule;
import site.timecapsulearchive.core.domain.capsule.exception.CapsuleNotFondException;
import site.timecapsulearchive.core.domain.capsule.generic_capsule.data.dto.MediaSaveDto;
import site.timecapsulearchive.core.domain.capsule.mapper.CapsuleMapper;
import site.timecapsulearchive.core.domain.capsule.repository.CapsuleQueryRepository;
import site.timecapsulearchive.core.domain.capsule.repository.CapsuleRepository;
import site.timecapsulearchive.core.domain.capsule.secret_capsule.data.dto.MySecreteCapsuleDto;
import site.timecapsulearchive.core.domain.capsule.secret_capsule.data.dto.SecretCapsuleCreateRequestDto;
import site.timecapsulearchive.core.domain.capsule.secret_capsule.data.dto.SecretCapsuleDetailDto;
import site.timecapsulearchive.core.domain.capsule.secret_capsule.data.dto.SecretCapsuleSummaryDto;
import site.timecapsulearchive.core.domain.capsule.secret_capsule.data.response.MySecretCapsuleSliceResponse;
import site.timecapsulearchive.core.domain.capsule.secret_capsule.data.response.SecretCapsuleDetailResponse;
import site.timecapsulearchive.core.domain.capsuleskin.entity.CapsuleSkin;
import site.timecapsulearchive.core.domain.capsuleskin.exception.CapsuleSkinNotFoundException;
import site.timecapsulearchive.core.domain.capsuleskin.repository.CapsuleSkinRepository;
import site.timecapsulearchive.core.domain.member.entity.Member;
import site.timecapsulearchive.core.domain.member.service.MemberService;
import site.timecapsulearchive.core.global.geography.GeoTransformManager;
import site.timecapsulearchive.core.infra.s3.data.dto.S3PreSignedUrlDto;
import site.timecapsulearchive.core.infra.s3.data.request.S3PreSignedUrlRequestDto;
import site.timecapsulearchive.core.infra.s3.manager.S3PreSignedUrlManager;

@Service
@RequiredArgsConstructor
public class SecretCapsuleService {

    private final CapsuleQueryRepository capsuleQueryRepository;
    private final CapsuleRepository capsuleRepository;
    private final CapsuleSkinRepository capsuleSkinRepository;
    private final MemberService memberService;
    private final ImageService imageService;
    private final VideoService videoService;
    private final GeoTransformManager geoTransformManager;
    private final CapsuleMapper capsuleMapper;
    private final S3PreSignedUrlManager s3PreSignedUrlManager;

    /**
     * 멤버 아이디와 캡슐 생성 포맷을 받아서 캡슐을 생성한다.
     *
     * @param memberId 캡슐을 생성할 멤버 아이디
     * @param dto      캡슐 생성 요청 포맷
     */
    public void saveCapsule(Long memberId, SecretCapsuleCreateRequestDto dto) {
        Member findMember = memberService.findMemberByMemberId(memberId);

        CapsuleSkin capsuleSkin = capsuleSkinRepository
            .findById(dto.capsuleSkinId())
            .orElseThrow(CapsuleSkinNotFoundException::new);

        Point point = geoTransformManager.changePoint4326To3857(dto.latitude(), dto.longitude());

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
    public MySecretCapsuleSliceResponse findSecretCapsuleSliceByMemberId(
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
        return capsuleQueryRepository.findSecretCapsuleSummaryDtosByMemberIdAndCapsuleId(memberId,
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
        SecretCapsuleDetailDto dto = capsuleQueryRepository.findSecretCapsuleDetailDtosByMemberIdAndCapsuleId(
                memberId,
                capsuleId)
            .orElseThrow(CapsuleNotFondException::new);

        if (capsuleNotOpened(dto)) {
            return capsuleMapper.notOpenedSecretCapsuleDetailDtoToResponse(dto);
        }

        S3PreSignedUrlDto s3UrlsForGet = s3PreSignedUrlManager.getS3PreSignedUrlsForGet(
            S3PreSignedUrlRequestDto.forGet(
                List.of(dto.images().split(",")),
                List.of(dto.videos().split(","))
            )
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

        return !dto.isOpened() || dto.dueDate().isAfter(ZonedDateTime.now(ZoneOffset.UTC));
    }
}
