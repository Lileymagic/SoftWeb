package com.example.softengineerwebpr.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    // Auth
    NICKNAME_DUPLICATION(HttpStatus.CONFLICT, "AUTH_001", "이미 사용 중인 닉네임입니다."),
    EMAIL_DUPLICATION(HttpStatus.CONFLICT, "AUTH_002", "이미 사용 중인 이메일입니다."),
    LOGIN_ID_DUPLICATION(HttpStatus.CONFLICT, "AUTH_003", "이미 사용 중인 아이디입니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "AUTH_004", "해당 사용자를 찾을 수 없습니다."),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "AUTH_005", "비밀번호가 일치하지 않습니다."),
    UNSUPPORTED_OAUTH_PROVIDER(HttpStatus.BAD_REQUEST, "AUTH_006", "지원하지 않는 소셜 로그인 제공자입니다."),
    EMAIL_VERIFICATION_FAILED(HttpStatus.BAD_REQUEST, "AUTH_007", "이메일 인증에 실패했습니다. 인증번호를 확인해주세요."),
    EMAIL_SEND_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "AUTH_008", "이메일 발송에 실패했습니다. 잠시 후 다시 시도해주세요."),
    USER_REGISTRATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "AUTH_009", "사용자 등록 중 오류가 발생했습니다."),
    USER_INFO_MISMATCH(HttpStatus.BAD_REQUEST, "AUTH_010", "입력하신 사용자 정보가 일치하지 않습니다."),
    PASSWORD_RESET_NOT_SUPPORTED_FOR_SOCIAL(HttpStatus.BAD_REQUEST, "AUTH_011", "소셜 로그인 사용자는 이 기능을 사용할 수 없습니다."),

    // Project & Member Errors
    PROJECT_NOT_FOUND(HttpStatus.NOT_FOUND, "PROJECT_001", "해당 프로젝트를 찾을 수 없습니다."),
    NOT_PROJECT_MEMBER(HttpStatus.FORBIDDEN, "PROJECT_002", "해당 프로젝트의 멤버가 아니므로 접근 권한이 없습니다."),
    ALREADY_PROJECT_MEMBER(HttpStatus.CONFLICT, "PROJECT_003", "이미 해당 프로젝트의 멤버입니다."),
    PROJECT_INVITATION_PENDING(HttpStatus.CONFLICT, "PROJECT_004", "이미 해당 사용자에게 프로젝트 초대 요청을 보냈거나, 초대 대기 중입니다."),
    NO_AUTHORITY_TO_INVITE(HttpStatus.FORBIDDEN, "PROJECT_005", "프로젝트 멤버를 초대할 권한이 없습니다."),
    NO_AUTHORITY_TO_MANAGE_MEMBERS(HttpStatus.FORBIDDEN, "PROJECT_006", "프로젝트 멤버를 관리할 권한이 없습니다."),
    TARGET_USER_NOT_PROJECT_MEMBER(HttpStatus.NOT_FOUND, "PROJECT_007", "관리하려는 사용자가 해당 프로젝트의 멤버가 아닙니다."),
    CANNOT_REMOVE_LAST_ADMIN(HttpStatus.BAD_REQUEST, "PROJECT_008", "프로젝트의 마지막 관리자는 자신을 제외하거나 역할을 변경할 수 없습니다."),
    CANNOT_MODIFY_OWN_ROLE_OR_REMOVE_ONESELF_AS_ADMIN(HttpStatus.BAD_REQUEST, "PROJECT_009", "관리자는 자신의 역할을 변경하거나 자신을 프로젝트에서 제외할 수 없습니다. 다른 관리자에게 요청하세요."),
    ROLE_UPDATE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "PROJECT_010", "멤버 역할 변경 중 오류가 발생했습니다."),
    MEMBER_REMOVAL_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "PROJECT_011", "멤버 제외 중 오류가 발생했습니다."),
    NO_PENDING_INVITATION(HttpStatus.NOT_FOUND, "PROJECT_012", "수락 또는 거절할 수 있는 초대 정보를 찾을 수 없습니다."),
    INVITATION_ALREADY_PROCESSED(HttpStatus.BAD_REQUEST, "PROJECT_013", "이미 처리된 초대입니다."),

    // Task Errors
    TASK_NOT_FOUND(HttpStatus.NOT_FOUND, "TASK_001", "해당 업무를 찾을 수 없습니다."),
    USER_ALREADY_ASSIGNED_TO_TASK(HttpStatus.CONFLICT, "TASK_002", "이미 해당 업무에 할당된 사용자입니다."),
    USER_NOT_ASSIGNED_TO_TASK(HttpStatus.NOT_FOUND, "TASK_003", "해당 사용자는 이 업무의 담당자가 아닙니다."),
    NO_AUTHORITY_TO_MANAGE_TASK(HttpStatus.FORBIDDEN, "TASK_004", "이 업무를 관리할(수정/삭제/담당자 변경 등) 권한이 없습니다."),
    TASK_ASSIGNEE_NOT_PROJECT_MEMBER(HttpStatus.BAD_REQUEST, "TASK_005", "프로젝트 멤버가 아닌 사용자는 업무에 할당할 수 없습니다."),
    INVALID_DEADLINE(HttpStatus.BAD_REQUEST, "TASK_006", "마감 기한은 현재 시각 이후로 설정해야 합니다."),

    // Friend Errors
    ALREADY_FRIEND(HttpStatus.CONFLICT, "FRIEND_001", "이미 친구 관계입니다."),
    FRIEND_REQUEST_ALREADY_SENT(HttpStatus.CONFLICT, "FRIEND_002", "이미 친구 요청을 보냈거나 받은 상태입니다."),
    FRIEND_REQUEST_NOT_FOUND(HttpStatus.NOT_FOUND, "FRIEND_003", "처리할 친구 요청이 없습니다."),
    NOT_FRIEND(HttpStatus.BAD_REQUEST, "FRIEND_004", "친구 관계가 아닙니다."),
    CANNOT_REQUEST_FRIEND_TO_SELF(HttpStatus.BAD_REQUEST, "FRIEND_005", "자기 자신에게 친구 요청을 보낼 수 없습니다."),

    // Common
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "COMMON_001", "입력 값이 올바르지 않습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON_002", "서버 내부 오류가 발생했습니다."),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "COMMON_003", "접근 권한이 없습니다."),
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "COMMON_004", "요청하신 리소스를 찾을 수 없습니다."),

    // File Errors (모든 enum 상수가 여기에 포함되어야 합니다)
    FILE_SIZE_EXCEEDED(HttpStatus.PAYLOAD_TOO_LARGE, "FILE_001", "파일 크기가 너무 큽니다. (최대 10MB)"),
    INVALID_FILE_TYPE(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "FILE_002", "지원하지 않는 파일 형식입니다.");

    // --- 여기부터 필드 선언과 생성자가 시작되어야 합니다. ---
    private final HttpStatus status;
    private final String code;
    private final String message;

    ErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}