package com.example.softengineerwebpr.domain.group.dto;

import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GroupUpdateRequestDto {

    @Size(max = 255, message = "그룹 이름은 255자를 넘을 수 없습니다.")
    private String name; // 변경할 이름 (null이 아니면 변경)

    @Pattern(regexp = "^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$", message = "올바른 색상 코드 형식이 아닙니다. (예: #RRGGBB)")
    private String color; // 변경할 색상 (null이 아니면 변경)

    public GroupUpdateRequestDto(String name, String color) {
        this.name = name;
        this.color = color;
    }
}