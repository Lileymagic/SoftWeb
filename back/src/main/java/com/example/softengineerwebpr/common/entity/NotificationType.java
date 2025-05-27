package com.example.softengineerwebpr.common.entity;

public enum NotificationType {
    // 프로젝트 관련
    PROJECT_INVITED("프로젝트 초대"), // 프로젝트에 초대받았을 때
    PROJECT_INVITE_ACCEPTED("초대 수락"), // 보낸 프로젝트 초대가 수락되었을 때
    PROJECT_INVITE_REJECTED("초대 거절"), // 보낸 프로젝트 초대가 거절되었을 때
    PROJECT_MEMBER_JOINED("새 멤버 참여"), // 다른 멤버가 프로젝트에 참여했을 때 (초대 수락 등)
    PROJECT_MEMBER_LEFT("멤버 탈퇴/제외"), // 멤버가 프로젝트를 떠났거나 제외되었을 때

    // 업무(Task) 관련
    NEW_TASK_ASSIGNED("새 업무 할당"), // 자신에게 새 업무가 할당될 때
    TASK_DEADLINE_REMINDER("업무 마감 알림"), // 맡은 업무의 마감 기한이 다가올 때
    TASK_STATUS_CHANGED("업무 상태 변경"), // 자신이 참여한 업무의 상태가 변경될 때
    TASK_UPDATED("업무 내용 변경"), // 자신이 참여한 업무의 내용이 변경될 때

    // 친구(Friend) 관련
    FRIEND_REQUEST_RECEIVED("친구 요청 도착"), // 친구 요청을 받았을 때
    FRIEND_REQUEST_ACCEPTED("친구 요청 수락"), // 보낸 친구 요청이 수락되었을 때
    FRIEND_REQUEST_REJECTED("친구 요청 거절"), // 보낸 친구 요청이 거절되었을 때

    // 게시글/댓글(Post/Comment) 관련
    NEW_COMMENT_ON_MY_POST("내 게시글에 새 댓글"), // 자신의 게시글에 댓글이 달렸을 때
    NEW_REPLY_TO_MY_COMMENT("내 댓글에 새 답글"); // 자신의 댓글에 답글이 달렸을 때 (대댓글)

    private final String description;

    NotificationType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}