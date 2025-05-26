package com.example.softengineerwebpr.common.entity;

public enum Permission {
    // 1. 프로젝트에 유저 초대, 제외
    MANAGE_PROJECT_MEMBERS(1),      // 2^0 (기존 P_INVITE_REMOVE_USER 에 해당)
    // 2. 업무 생성, 마감기한 설정, 삭제
    MANAGE_TASKS(2),               // 2^1
    // 3. 그룹 생성, 초대
    MANAGE_GROUPS(4),              // 2^2
    // 4. 프로젝트 삭제
    DELETE_PROJECT(8),             // 2^3
    // 5. 권한 위임 및 양도 (예: 다른 사용자를 프로젝트 관리자로 지정)
    DELEGATE_PROJECT_PERMISSIONS(16), // 2^4
    // 6. 게시글, 댓글 삭제
    DELETE_POSTS_COMMENTS(32);     // 2^5

    public final int bit;

    Permission(int bit) {
        this.bit = bit;
    }

    public int getBit() {
        return bit;
    }
}