package com.hsj.force.domain.response;

import com.hsj.force.domain.OpenClose;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
public class OpenCloseRes extends OpenClose {

    private LocalDateTime closeDate;
    private LocalDateTime closeTime;
    private String closer;
    private String closerId;
    private String closerName;
    private Integer procedure;
    private String opener;
    private LocalDateTime currentDate;

}
