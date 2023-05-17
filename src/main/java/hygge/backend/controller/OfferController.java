package hygge.backend.controller;

import hygge.backend.dto.response.ErrorResponse;
import hygge.backend.dto.response.offer.GetOffersResponse;
import hygge.backend.service.OfferService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@Tag(name = "팀 합류 제안", description = "팀 합류 제안 관련 API 입니다.")
@RestController
@RequestMapping("/api/v1/offer")
@RequiredArgsConstructor
public class OfferController {
    private final OfferService offerService;

    // 구독한 과목에 대한 합류 제안 조회
    @Operation(summary = "구독한 과목에 대한 합류 제안 조회 조회 메서드", description = "구독한 과목에 대한 합류 제안 조회 메서드입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "구독한 과목에 대한 합류 제안 조회 성공",
                    content = @Content(schema = @Schema(implementation = GetOffersResponse.class))),
            @ApiResponse(responseCode = "400", description = "구독한 과목에 대한 합류 제안 조회 실패",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))

    })
    @GetMapping
    public ResponseEntity<GetOffersResponse> getOffers(Principal principal, @RequestParam Long subjectId) {
        Long memberId = Long.parseLong(principal.getName());
        GetOffersResponse response = offerService.getOffers(memberId, subjectId);
        return ResponseEntity.ok(response);
    }

}
