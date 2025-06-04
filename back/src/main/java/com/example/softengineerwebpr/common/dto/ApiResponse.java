package com.example.softengineerwebpr.common.dto; //

import lombok.Getter;
import org.springframework.http.HttpStatus; // HttpStatus 임포트 추가

@Getter
public class ApiResponse<T> {
    private final int status;       // HTTP 상태 코드 또는 커스텀 상태 코드
    private final String message;   // 응답 메시지
    private final T data;           // 실제 응답 데이터 (성공 시)

    private ApiResponse(int status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    // 성공 응답 (데이터 포함)
    public static <T> ApiResponse<T> success(int status, String message, T data) { //
        return new ApiResponse<>(status, message, data);
    }

    // 성공 응답 (데이터 없음, 메시지만)
    public static <T> ApiResponse<T> success(int status, String message) { //
        return new ApiResponse<>(status, message, null);
    }

    /**
     * 실패 또는 오류 응답을 생성합니다 (데이터 없음).
     * 이 메소드는 간단한 오류 메시지를 바로 반환해야 할 때 사용될 수 있습니다.
     * 복잡한 오류 정보나 표준화된 오류 응답은 GlobalExceptionHandler와 ErrorResponse를 통해 처리하는 것이 좋습니다.
     * @param httpStatus HTTP 상태 코드 Enum
     * @param message 오류 메시지
     * @return ApiResponse 객체 (data는 null)
     * @param <T> 데이터 타입 (여기서는 사용되지 않음)
     */
    public static <T> ApiResponse<T> error(HttpStatus httpStatus, String message) { // << 새로 추가된 메소드
        return new ApiResponse<>(httpStatus.value(), message, null);
    }

    /**
     * 실패 또는 오류 응답을 생성합니다 (커스텀 상태 코드 사용).
     * @param customStatusValue 커스텀 int 상태 값
     * @param message 오류 메시지
     * @return ApiResponse 객체 (data는 null)
     * @param <T> 데이터 타입
     */
    public static <T> ApiResponse<T> error(int customStatusValue, String message) { // << 새로 추가된 메소드 (오버로딩)
        return new ApiResponse<>(customStatusValue, message, null);
    }
}