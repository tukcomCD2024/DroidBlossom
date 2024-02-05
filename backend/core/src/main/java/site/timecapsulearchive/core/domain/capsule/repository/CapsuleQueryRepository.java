package site.timecapsulearchive.core.domain.capsule.repository;

import static com.querydsl.core.group.GroupBy.groupBy;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;
import static site.timecapsulearchive.core.domain.capsule.entity.QCapsule.capsule;
import static site.timecapsulearchive.core.domain.capsule.entity.QImage.image;
import static site.timecapsulearchive.core.domain.capsule.entity.QVideo.video;

import com.querydsl.core.ResultTransformer;
import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Polygon;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;
import site.timecapsulearchive.core.domain.capsule.dto.CapsuleSummaryDto;
import site.timecapsulearchive.core.domain.capsule.dto.mapper.CapsuleMapper;
import site.timecapsulearchive.core.domain.capsule.dto.secret_c.SecretCapsuleDetail;
import site.timecapsulearchive.core.domain.capsule.dto.secret_c.SecretCapsuleDetailDto;
import site.timecapsulearchive.core.domain.capsule.dto.secret_c.SecretCapsuleSummaryDto;
import site.timecapsulearchive.core.domain.capsule.entity.CapsuleType;
import site.timecapsulearchive.core.domain.capsule.entity.QImage;
import site.timecapsulearchive.core.domain.capsule.entity.QVideo;

@Repository
@RequiredArgsConstructor
public class CapsuleQueryRepository {

    private final EntityManager entityManager;
    private final JPAQueryFactory jpaQueryFactory;
    private final CapsuleMapper capsuleMapper;

    private static List<Long> getCapsuleIds(List<SecretCapsuleDetail> secretCapsuleList) {
        return secretCapsuleList.stream()
            .map(SecretCapsuleDetail::capsuleId)
            .toList();
    }

    /**
     * 캡슐 타입에 따라 현재 위치에서 범위 내의 캡슐을 조회한다.
     *
     * @param memberId    범위 내의 캡슙을 조회할 멤버 id
     * @param mbr         캡슈을 조회할 범위
     * @param capsuleType 조회할 캡슐의 타입
     * @return 범위 내에 조회된 캡슐들의 요약 정보들을 반환한다.
     */
    public List<CapsuleSummaryDto> findCapsuleByCurrentLocationAndCapsuleType(
        Long memberId,
        Polygon mbr,
        CapsuleType capsuleType
    ) {
        String queryString = """
            select new site.timecapsulearchive.core.domain.capsule.dto.CapsuleSummaryDto(
                c.id,
                c.point,
                m.nickname,
                cs.imageUrl,
                c.title,
                c.dueDate,
                c.isOpened,
                c.type
            )
            from Capsule c
            join c.member m
            join c.capsuleSkin cs
            where ST_Contains(:mbr, c.point) and m.id=:memberId
            """;
        if (capsuleType != CapsuleType.ALL) {
            queryString += " and c.type = :capsuleType";
        }

        TypedQuery<CapsuleSummaryDto> query = entityManager.createQuery(queryString,
            CapsuleSummaryDto.class);
        query.setParameter("mbr", mbr);
        query.setParameter("memberId", memberId);

        if (capsuleType != CapsuleType.ALL) {
            query.setParameter("capsuleType", capsuleType);
        }

        return query.getResultList();
    }

    public Optional<SecretCapsuleSummaryDto> findSecretCapsuleSummaryByMemberIdAndCapsuleId(
        Long memberId,
        Long capsuleId
    ) {
        return Optional.ofNullable(
            jpaQueryFactory
                .select(
                    Projections.constructor(
                        SecretCapsuleSummaryDto.class,
                        capsule.member.nickname,
                        capsule.member.profileUrl,
                        capsule.capsuleSkin.imageUrl,
                        capsule.title,
                        capsule.dueDate,
                        capsule.address.fullRoadAddressName,
                        capsule.address.roadName,
                        capsule.isOpened,
                        capsule.createdAt
                    )
                )
                .from(capsule)
                .where(capsule.id.eq(capsuleId).and(capsule.member.id.eq(memberId))
                    .and(capsule.type.eq(CapsuleType.SECRET)))
                .fetchOne()
        );
    }

    public Optional<SecretCapsuleDetailDto> findSecretCapsuleDetailByMemberIdAndCapsuleId(
        Long memberId,
        Long capsuleId
    ) {
        return jpaQueryFactory
            .from(capsule)
            .leftJoin(image).on(capsule.id.eq(image.capsule.id))
            .leftJoin(video).on(capsule.id.eq(video.capsule.id))
            .where(capsule.id.eq(capsuleId).and(capsule.member.id.eq(memberId))
                .and(capsule.type.eq(CapsuleType.SECRET)))
            .transform(
                findSecretCapsuleDetailDto()
            ).stream()
            .findFirst();
    }

    private ResultTransformer<List<SecretCapsuleDetailDto>> findSecretCapsuleDetailDto() {
        return groupBy(capsule.id).list(
            Projections.constructor(
                SecretCapsuleDetailDto.class,
                capsule.id,
                capsule.capsuleSkin.imageUrl,
                capsule.dueDate,
                capsule.member.nickname,
                capsule.createdAt,
                capsule.address.fullRoadAddressName,
                capsule.title,
                capsule.content,
                GroupBy.list(
                    Projections.constructor(String.class, image.imageUrl).skipNulls()),
                GroupBy.list(
                    Projections.constructor(String.class, video.videoUrl).skipNulls()),
                capsule.isOpened
            )
        );
    }

    public Slice<SecretCapsuleDetailDto> findSecretCapsuleSliceByMemberId(
        Long memberId,
        int size,
        ZonedDateTime createdAt
    ) {
        List<SecretCapsuleDetail> secretCapsuleList = jpaQueryFactory
            .select(
                Projections.constructor(
                    SecretCapsuleDetail.class,
                    capsule.id,
                    capsule.capsuleSkin.imageUrl,
                    capsule.dueDate,
                    capsule.member.nickname,
                    capsule.createdAt,
                    capsule.address.fullRoadAddressName,
                    capsule.title,
                    capsule.content,
                    capsule.isOpened
                )
            )
            .from(capsule)
            .where(
                capsule.member.id.eq(memberId),
                capsule.createdAt.lt(createdAt),
                capsule.type.eq(CapsuleType.SECRET)
            )
            .orderBy(capsule.id.desc())
            .limit(size + 1)
            .fetch();

        boolean hasNext = canMoreRead(size, secretCapsuleList.size());
        if (hasNext) {
            secretCapsuleList.remove(size);
        }

        List<Long> capsuleIds = getCapsuleIds(secretCapsuleList);

        Map<Long, List<String>> imageUrls = findImageUrlsByCapsuleId(capsuleIds);

        Map<Long, List<String>> videoUrls = findVideoUrlsByCapsuleId(capsuleIds);

        List<SecretCapsuleDetailDto> result = secretCapsuleList.stream()
            .map(dto -> capsuleMapper.secretCapsuleDetailToDto(dto, imageUrls, videoUrls))
            .toList();

        return new SliceImpl<>(result, Pageable.ofSize(size), hasNext);
    }

    private boolean canMoreRead(int size, int capsuleSize) {
        return capsuleSize > size;
    }

    private Map<Long, List<String>> findVideoUrlsByCapsuleId(List<Long> capsuleIds) {
        return jpaQueryFactory
            .select(video.capsule.id, video.videoUrl)
            .from(video)
            .where(video.capsule.id.in(capsuleIds))
            .fetch()
            .stream()
            .collect(
                groupingBy(
                    video -> video.get(QVideo.video.capsule.id),
                    mapping(video -> video.get(QVideo.video.videoUrl), toList())
                )
            );
    }

    private Map<Long, List<String>> findImageUrlsByCapsuleId(List<Long> capsuleIds) {
        return jpaQueryFactory
            .select(image.capsule.id, image.imageUrl)
            .from(image)
            .where(image.capsule.id.in(capsuleIds))
            .fetch()
            .stream()
            .collect(
                groupingBy(
                    image -> image.get(QImage.image.capsule.id),
                    mapping(image -> image.get(QImage.image.imageUrl), toList())
                )
            );
    }
}