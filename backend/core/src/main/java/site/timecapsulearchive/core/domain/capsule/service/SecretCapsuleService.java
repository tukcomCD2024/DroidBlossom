package site.timecapsulearchive.core.domain.capsule.service;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.timecapsulearchive.core.domain.capsule.entity.Capsule;
import site.timecapsulearchive.core.domain.capsule.exception.CapsuleNotFondException;
import site.timecapsulearchive.core.domain.capsule.mapper.CapsuleMapper;
import site.timecapsulearchive.core.domain.capsule.mapper.ImageMapper;
import site.timecapsulearchive.core.domain.capsule.mapper.VideoMapper;
import site.timecapsulearchive.core.domain.capsule.repository.CapsuleQueryRepository;
import site.timecapsulearchive.core.domain.capsule.repository.CapsuleRepository;
import site.timecapsulearchive.core.domain.capsule.repository.ImageQueryRepository;
import site.timecapsulearchive.core.domain.capsule.repository.VideoQueryRepository;
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
import site.timecapsulearchive.core.domain.member.exception.MemberNotFoundException;
import site.timecapsulearchive.core.domain.member.repository.MemberRepository;
import site.timecapsulearchive.core.infra.s3.data.dto.S3PreSignedUrlDto;
import site.timecapsulearchive.core.infra.s3.data.request.S3PreSignedUrlRequestDto;
import site.timecapsulearchive.core.infra.s3.manager.S3PreSignedUrlManager;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SecretCapsuleService {

    private static final String DELIMITER = ",";

    private final CapsuleQueryRepository capsuleQueryRepository;
    private final CapsuleRepository capsuleRepository;
    private final CapsuleSkinRepository capsuleSkinRepository;
    private final MemberRepository memberRepository;
    private final ImageQueryRepository imageQueryRepository;
    private final VideoQueryRepository videoQueryRepository;

    private final S3PreSignedUrlManager s3PreSignedUrlManager;

    private final CapsuleMapper capsuleMapper;
    private final ImageMapper imageMapper;
    private final VideoMapper videoMapper;

    /**
     * 멤버 아이디와 마지막 캡슐 생성 날짜를 받아서 내 페이지 비밀 캡슐을 조회한다.
     *
     * @param memberId  캡슐을 생성할 멤버 아이디
     * @param size      페이지 사이즈
     * @param createdAt 마지막 캡슐 생성 날짜
     * @return 내 페이지에서 비밀 캡슐을 조회한다.
     */
    public MySecretCapsuleSliceResponse findSecretCapsuleSliceByMemberId(
        final Long memberId,
        final int size,
        final ZonedDateTime createdAt
    ) {
        final Slice<MySecreteCapsuleDto> slice = capsuleQueryRepository
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
        final Long memberId,
        final Long capsuleId
    ) {
        return capsuleQueryRepository.findSecretCapsuleSummaryDtosByMemberIdAndCapsuleId(
                memberId, capsuleId)
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
        final Long memberId,
        final Long capsuleId
    ) {
        final SecretCapsuleDetailDto dto = capsuleQueryRepository.findSecretCapsuleDetailDtosByMemberIdAndCapsuleId(
                memberId, capsuleId)
            .orElseThrow(CapsuleNotFondException::new);

        if (capsuleNotOpened(dto)) {
            return capsuleMapper.notOpenedSecretCapsuleDetailDtoToResponse(dto);
        }

        final S3PreSignedUrlDto s3UrlsForGet = s3PreSignedUrlManager.getS3PreSignedUrlsForGet(
            S3PreSignedUrlRequestDto.forGet(
                splitFileNames(dto.images()),
                splitFileNames(dto.videos())
            )
        );

        return capsuleMapper.secretCapsuleDetailDtoToResponse(
            dto,
            s3UrlsForGet.preSignedImageUrls(),
            s3UrlsForGet.preSignedVideoUrls()
        );
    }

    private List<String> splitFileNames(String fileNames) {
        if (fileNames == null) {
            return Collections.emptyList();
        }

        return List.of(fileNames.split(DELIMITER));
    }

    private boolean capsuleNotOpened(final SecretCapsuleDetailDto dto) {
        if (dto.dueDate() == null) {
            return false;
        }

        return !dto.isOpened() || dto.dueDate().isAfter(ZonedDateTime.now(ZoneOffset.UTC));
    }

    /**
     * 멤버 아이디와 캡슐 생성 포맷을 받아서 캡슐을 생성한다.
     *
     * @param memberId 캡슐을 생성할 멤버 아이디
     * @param dto      캡슐 생성 요청 포맷
     */
    @Transactional
    public void saveCapsule(final Long memberId, final SecretCapsuleCreateRequestDto dto) {
        final Member foundMember = memberRepository.findMemberById(memberId)
            .orElseThrow(MemberNotFoundException::new);

        final CapsuleSkin foundCapsuleSkin = capsuleSkinRepository
            .findCapsuleSkinById(dto.capsuleSkinId())
            .orElseThrow(CapsuleSkinNotFoundException::new);

        final Capsule capsule = capsuleMapper.requestDtoToEntity(dto, foundMember,
            foundCapsuleSkin);

        capsuleRepository.save(capsule);

        if (isNotEmpty(dto.imageNames())) {
            imageQueryRepository.bulkSave(
                imageMapper.toEntity(capsule, foundMember, dto.directory(), dto.imageNames())
            );
        }

        if (isNotEmpty(dto.videoNames())) {
            videoQueryRepository.bulkSave(
                videoMapper.toEntity(capsule, foundMember, dto.directory(), dto.videoNames())
            );
        }
    }

    private boolean isNotEmpty(List<String> fileNames) {
        return fileNames != null && !fileNames.isEmpty();
    }
}

