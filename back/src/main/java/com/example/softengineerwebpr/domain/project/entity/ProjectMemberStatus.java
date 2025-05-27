package com.example.softengineerwebpr.domain.project.entity; // 또는 common.entity 등 적절한 위치

public enum ProjectMemberStatus {
    PENDING,  // 초대 수락 대기 중
    ACCEPTED, // 초대 수락 (정식 멤버)
    REJECTED  // 초대 거절 (선택적: 이 경우 레코드를 삭제할 수도 있음)
}