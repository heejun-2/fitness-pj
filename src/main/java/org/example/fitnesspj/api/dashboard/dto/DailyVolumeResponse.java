package org.example.fitnesspj.api.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class DailyVolumeResponse {
    private LocalDate date;
    private long volume;
    private long setCount;
}
