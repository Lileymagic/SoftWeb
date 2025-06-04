package com.example.softengineerwebpr.domain.user.controller; //

import com.example.softengineerwebpr.common.dto.ApiResponse; //
import com.example.softengineerwebpr.common.util.AuthenticationUtil; //
import com.example.softengineerwebpr.domain.user.dto.FriendRequestDto;
import com.example.softengineerwebpr.domain.user.dto.FriendRequestResponseDto;
import com.example.softengineerwebpr.domain.user.dto.UserSearchResponseDto;
import com.example.softengineerwebpr.domain.user.entity.User; //
import com.example.softengineerwebpr.domain.user.service.FriendService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/friends") // 친구 관련 API 기본 경로
@RequiredArgsConstructor
public class FriendController {

    private final FriendService friendService;
    private final AuthenticationUtil authenticationUtil; //

    /**
     * 사용자 검색 (닉네임 기반)
     * @param query 검색할 닉네임
     * @param authentication 현재 인증 정보
     * @return 검색된 사용자 목록
     */
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<UserSearchResponseDto>>> searchUsers(
            @RequestParam String query, // searchType 파라미터 제거됨
            Authentication authentication) {
        User currentUser = authenticationUtil.getCurrentUser(authentication); //
        log.info("API 호출: 사용자 닉네임 검색 - query: {}, byUser: {}", query, currentUser.getNickname());
        List<UserSearchResponseDto> users = friendService.searchUsersByNickname(query, currentUser); // 수정된 서비스 메소드 호출
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "사용자 검색 성공", users));
    }

    /**
     * 친구 요청 보내기
     * @param requestDto 수신자 ID 포함
     * @param authentication 현재 인증 정보
     * @return 성공 응답
     */
    @PostMapping("/request")
    public ResponseEntity<ApiResponse<Void>> sendFriendRequest(
            @Valid @RequestBody FriendRequestDto requestDto,
            Authentication authentication) {
        User requester = authenticationUtil.getCurrentUser(authentication); //
        log.info("API 호출: 친구 요청 - toUserId: {}, fromUser: {}", requestDto.getRecipientUserId(), requester.getNickname());
        friendService.sendFriendRequest(requestDto.getRecipientUserId(), requester);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(HttpStatus.CREATED.value(), "친구 요청을 성공적으로 보냈습니다."));
    }

    /**
     * 친구 요청 수락
     * @param requesterUserId 요청을 보냈던 사용자의 ID
     * @param authentication 현재 인증 정보 (요청을 수락하는 사용자)
     * @return 성공 응답
     */
    @PatchMapping("/accept/{requesterUserId}")
    public ResponseEntity<ApiResponse<Void>> acceptFriendRequest(
            @PathVariable Long requesterUserId,
            Authentication authentication) {
        User accepter = authenticationUtil.getCurrentUser(authentication); //
        log.info("API 호출: 친구 요청 수락 - requesterId: {}, accepterUser: {}", requesterUserId, accepter.getNickname());
        friendService.acceptFriendRequest(requesterUserId, accepter);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "친구 요청을 수락했습니다."));
    }

    /**
     * 친구 요청 거절
     * @param requesterUserId 요청을 보냈던 사용자의 ID
     * @param authentication 현재 인증 정보 (요청을 거절하는 사용자)
     * @return 성공 응답
     */
    @PatchMapping("/reject/{requesterUserId}")
    public ResponseEntity<ApiResponse<Void>> rejectFriendRequest(
            @PathVariable Long requesterUserId,
            Authentication authentication) {
        User rejecter = authenticationUtil.getCurrentUser(authentication); //
        log.info("API 호출: 친구 요청 거절 - requesterId: {}, rejecterUser: {}", requesterUserId, rejecter.getNickname());
        friendService.rejectFriendRequest(requesterUserId, rejecter);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "친구 요청을 거절했습니다."));
    }

    /**
     * 친구 삭제
     * @param friendUserId 삭제할 친구의 사용자 ID
     * @param authentication 현재 인증 정보
     * @return 성공 응답
     */
    @DeleteMapping("/{friendUserId}")
    public ResponseEntity<ApiResponse<Void>> removeFriend(
            @PathVariable Long friendUserId,
            Authentication authentication) {
        User currentUser = authenticationUtil.getCurrentUser(authentication); //
        log.info("API 호출: 친구 삭제 - friendUserId: {}, byUser: {}", friendUserId, currentUser.getNickname());
        friendService.removeFriend(friendUserId, currentUser);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "친구를 삭제했습니다."));
    }

    /**
     * 내 친구 목록 조회
     * @param authentication 현재 인증 정보
     * @return 친구 목록
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<UserSearchResponseDto>>> getMyFriends(Authentication authentication) {
        User currentUser = authenticationUtil.getCurrentUser(authentication); //
        log.info("API 호출: 내 친구 목록 조회 - byUser: {}", currentUser.getNickname());
        List<UserSearchResponseDto> friends = friendService.getFriendList(currentUser);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "친구 목록 조회 성공", friends));
    }

    /**
     * 내가 받은 친구 요청 목록 조회
     * @param authentication 현재 인증 정보
     * @return 받은 친구 요청 목록
     */
    @GetMapping("/requests/received")
    public ResponseEntity<ApiResponse<List<FriendRequestResponseDto>>> getReceivedFriendRequests(Authentication authentication) {
        User currentUser = authenticationUtil.getCurrentUser(authentication); //
        log.info("API 호출: 받은 친구 요청 목록 조회 - forUser: {}", currentUser.getNickname());
        List<FriendRequestResponseDto> requests = friendService.getReceivedFriendRequests(currentUser);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "받은 친구 요청 목록 조회 성공", requests));
    }

    /**
     * 내가 보낸 친구 요청 목록 조회
     * @param authentication 현재 인증 정보
     * @return 보낸 친구 요청 목록
     */
    @GetMapping("/requests/sent")
    public ResponseEntity<ApiResponse<List<FriendRequestResponseDto>>> getSentFriendRequests(Authentication authentication) {
        User currentUser = authenticationUtil.getCurrentUser(authentication); //
        log.info("API 호출: 보낸 친구 요청 목록 조회 - byUser: {}", currentUser.getNickname());
        List<FriendRequestResponseDto> requests = friendService.getSentFriendRequests(currentUser);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "보낸 친구 요청 목록 조회 성공", requests));
    }
}