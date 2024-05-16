package site.timecapsulearchive.core.domain.group.api.command;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import site.timecapsulearchive.core.domain.group.data.reqeust.GroupCreateRequest;
import site.timecapsulearchive.core.domain.group.data.reqeust.GroupUpdateRequest;
import site.timecapsulearchive.core.global.common.response.ApiSpec;
import site.timecapsulearchive.core.global.error.ErrorResponse;

public interface GroupCommandApi {

    @Operation(
        summary = "그룹 삭제",
        description = """
            그룹 삭제를 요청한 사용자가 해당 그룹의 그룹장인 경우 그룹을 삭제한다.<br>
            <b><u>주의</u></b>
            <ul>
                <li>그룹장이 아닌 경우 그룹을 삭제할 수 없다.</li>
                <li>그룹에 포함된 멤버가 그룹장을 제외하고 존재하면 그룹을 삭제할 수 없다.</li>
                <li>그룹에 그룹 캡슐이 남아있는 경우 삭제할 수 없다.</li>
            </ul>
            """,
        security = {@SecurityRequirement(name = "user_token")},
        tags = {"group"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "202",
            description = "처리 시작"
        ),
        @ApiResponse(
            responseCode = "400",
            description = """
                다음의 경우 예외가 발생한다.
                <ul>
                <li>삭제를 요청한 그룹에서 요청한 사용자가 그룹장이 아닌 경우</li>
                <li>삭제를 요청한 그룹에 그룹장을 제외한 그룹원이 존재하는 경우</li>
                <li>삭제를 요청한 그룹에 그룹 캡슐이 존재하는 경우</li>
                </ul>
                """,
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "그룹이 존재하지 않으면 발생한다.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        ),
    })
    ResponseEntity<ApiSpec<String>> deleteGroupById(
        Long memberId,

        @Parameter(in = ParameterIn.PATH, description = "삭제할 그룹 아이디", required = true)
        Long groupId
    );

    @Operation(
        summary = "그룹 생성",
        description = "친구로부터 그룹을 생성한다.",
        security = {@SecurityRequirement(name = "user_token")},
        tags = {"group"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "처리완료"
        ),
        @ApiResponse(
            responseCode = "500",
            description = "외부 API 요청 실패"
        )
    })
    ResponseEntity<ApiSpec<String>> createGroup(
        Long memberId,
        GroupCreateRequest request
    );

    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "처리 완료"
        ),@ApiResponse(
            responseCode = "400",
            description = "잘못된 타입이나 값을 입력하는 경우 발생한다.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "403",
            description = "그룹장이 아니여서 그룹 수정에 대한 권한이 없는 경우 발생한다.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "권한을 확인할 수 있는 그룹원이 없는 경우, 수정하려는 그룹이 없는 경우 발생한다.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    ResponseEntity<ApiSpec<String>> updateGroupById(
        Long memberId,

        @Parameter(in = ParameterIn.PATH, description = "수정할 그룹 아이디", required = true, schema = @Schema())
        Long groupId,

        GroupUpdateRequest request
    );
}
