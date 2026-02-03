package org.example.fitnesspj.api.workout.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class WorkoutMemoUpdateRequest {
    // 빈 문자열/공백만 들어오는 것을 막음
    @NotBlank(message = "memo는 비어 있을 수 없습니다.")
    // 길이 제한(원하는 값으로 조정 가능)
    @Size(max = 200, message = "memo는 최대 200자까지 가능합니다.")
    private String memo;
}
