package site.timecapsulearchive.core.domain.history.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import site.timecapsulearchive.core.domain.history.data.reqeust.HistoryCreateRequest;
import site.timecapsulearchive.core.domain.history.data.response.HistoriesPageResponse;
import site.timecapsulearchive.core.domain.history.data.response.HistoryDetailResponse;
import site.timecapsulearchive.core.domain.history.data.response.HistorySummaryResponse;

public interface HistoryApi {

    @Operation(
        summary = "회원 히스토리 생성",
        description = "캡슐 이미지를 기반으로 회원의 히스토리를 생성한다.",
        security = {@SecurityRequirement(name = "user_token")},
        tags = {"history"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "202",
            description = "처리 시작"
        )
    })
    @PostMapping(
        value = "/histories",
        consumes = {"multipart/form-data"}
    )
    ResponseEntity<HistorySummaryResponse> createHistory(
        @ModelAttribute HistoryCreateRequest request);

    @Operation(
        summary = "회원 히스토리 목록 조회",
        description = "회원의 히스토리 목록을 조회한다.",
        security = {@SecurityRequirement(name = "user_token")},
        tags = {"history"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "처리 완료"
        )
    })
    @GetMapping(
        value = "/histories",
        produces = {"application/json"}
    )
    ResponseEntity<HistoriesPageResponse> findHistories(
        @Parameter(in = ParameterIn.QUERY, description = "페이지 크기", required = true, schema = @Schema())
        @NotNull @Valid @RequestParam(value = "size") Long size,

        @Parameter(in = ParameterIn.QUERY, description = "마지막 히스토리 아이디", required = true, schema = @Schema())
        @NotNull @Valid @RequestParam(value = "history_id") Long historyId
    );

    @Operation(
        summary = "회원 히스토리 상세 조회",
        description = "회원의 히스토리의 컨텐츠를 조회한다.",
        security = {@SecurityRequirement(name = "user_token")},
        tags = {"history"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "처리 완료"
        )
    })
    @GetMapping(
        value = "/histories/{history_id}",
        produces = {"application/json"}
    )
    ResponseEntity<HistoryDetailResponse> findHistoryById(
        @Parameter(in = ParameterIn.PATH, description = "회원 히스토리 아이디", required = true, schema = @Schema())
        @PathVariable("history_id") Long historyId
    );

}
