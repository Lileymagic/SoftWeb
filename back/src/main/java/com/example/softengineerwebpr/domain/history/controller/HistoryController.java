package com.example.softengineerwebpr.domain.history.controller; // domain.history.controller 패키지 생성

import com.example.softengineerwebpr.common.dto.ApiResponse;
import com.example.softengineerwebpr.common.util.AuthenticationUtil;
import com.example.softengineerwebpr.domain.history.dto.HistoryResponseDto;
import com.example.softengineerwebpr.domain.history.service.HistoryService;
import com.example.softengineerwebpr.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class HistoryController {

    private final HistoryService historyService;
    private final AuthenticationUtil authenticationUtil;

    /**
     * 특정 프로젝트의 활동 히스토리 목록을 조회합니다.
     */
    @GetMapping("/projects/{projectId}/history")
    public ResponseEntity<ApiResponse<List<HistoryResponseDto>>> getProjectHistory(
            @PathVariable Long projectId,
            Authentication authentication) {
        User currentUser = authenticationUtil.getCurrentUser(authentication);
        List<HistoryResponseDto> histories = historyService.getHistoryForProject(projectId, currentUser);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "프로젝트 히스토리 조회 성공", histories));
    }
}