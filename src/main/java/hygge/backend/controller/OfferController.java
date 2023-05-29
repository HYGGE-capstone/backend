package hygge.backend.controller;

import hygge.backend.dto.offer.OfferResultDto;
import hygge.backend.dto.request.offer.OfferRequest;
import hygge.backend.dto.request.offer.OfferResultRequestDto;
import hygge.backend.dto.response.ErrorResponse;
import hygge.backend.dto.response.offer.GetOffersResponse;
import hygge.backend.dto.response.offer.OfferResponse;
import hygge.backend.service.OfferService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Tag(name = "팀 합류 제안", description = "팀 합류 제안 관련 API 입니다.")
@RestController
@RequestMapping("/api/v1/offer")
@RequiredArgsConstructor
public class OfferController {
    private final OfferService offerService;

    // 구독한 과목에 대한 합류 제안 조회
    @Operation(summary = "구독한 과목에 대한 합류 제안 조회 메서드", description = "구독한 과목에 대한 합류 제안 조회 메서드입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "구독한 과목에 대한 합류 제안 조회 성공",
                    content = @Content(schema = @Schema(implementation = GetOffersResponse.class))),
            @ApiResponse(responseCode = "400", description = "구독한 과목에 대한 합류 제안 조회 실패",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))

    })
    @GetMapping
    public GetOffersResponse getOffers(Principal principal, @RequestParam Long subjectId) {
        Long memberId = Long.parseLong(principal.getName());
        return offerService.getOffers(memberId, subjectId);
    }

    // 구독자에게 팀이 제안 POST
    @Operation(summary = "팀 합류 제안 메서드", description = "팀 합류 제안 메서드입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "팀 합류 제안 성공",
                    content = @Content(schema = @Schema(implementation = OfferResponse.class))),
            @ApiResponse(responseCode = "400", description = "팀 합류 제안 실패",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))

    })
    @PostMapping
    public ResponseEntity<OfferResponse> offer(Principal principal, @RequestBody OfferRequest request) {
        Long memberId = Long.parseLong(principal.getName());
        OfferResponse response = offerService.offer(memberId, request);
        return ResponseEntity.ok(response);
    }

    // 팀 합류 제안 수락
    @Operation(summary = "팀 합류 제안 수락 메서드", description = "팀 합류 제안 수락 메서드입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "팀 합류 제안 수락 성공",
                    content = @Content(schema = @Schema(implementation = OfferResponse.class))),
            @ApiResponse(responseCode = "400", description = "팀 합류 제안 수락 실패",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))

    })
    @PostMapping("/accept")
    public ResponseEntity<OfferResultDto> offerAccept(Principal principal, @RequestBody OfferResultRequestDto request){
        Long memberId = Long.parseLong(principal.getName());
        OfferResultDto response = offerService.offerAccept(memberId, request);
        return ResponseEntity.ok(response);
    }

    // 팀 합류 제안 거절
    @Operation(summary = "팀 합류 제안 거절 메서드", description = "팀 합류 제안 거절 메서드입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "팀 합류 제안 거절 성공",
                    content = @Content(schema = @Schema(implementation = OfferResponse.class))),
            @ApiResponse(responseCode = "400", description = "팀 합류 제안 거절 실패",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))

    })
    @PostMapping("/reject")
    public ResponseEntity<OfferResultDto> offerReject(Principal principal, @RequestBody OfferResultRequestDto request){
        Long memberId = Long.parseLong(principal.getName());
        OfferResultDto response = offerService.offerReject(memberId, request);
        return ResponseEntity.ok(response);
    }

}
