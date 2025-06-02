package com.example.softengineerwebpr.domain.group.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GroupCreateRequestDto {

    @NotBlank(message = "그룹 이름은 필수입니다.")
    @Size(max = 255, message = "그룹 이름은 255자를 넘을 수 없습니다.")
    private String name;

    @Pattern(regexp = "^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$", message = "올바른 색상 코드 형식이 아닙니다. (예: #RRGGBB)")
    private String color; // 예: "#FF0000" (선택적 필드)

    private Integer permissionBitmask; // 그룹에 부여할 초기 권한 비트마스크 (선택적, 기본값은 서비스에서 0으로 처리 가능)

    public GroupCreateRequestDto(String name, String color, Integer permissionBitmask) {
        this.name = name;
        this.color = color;
        this.permissionBitmask = permissionBitmask;
    }
}