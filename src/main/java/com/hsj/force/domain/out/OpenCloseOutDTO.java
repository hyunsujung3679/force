package com.hsj.force.domain.out;

import com.hsj.force.domain.OpenClose;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class OpenCloseOutDTO extends OpenClose {

    private String closeDate;
    private String closeTime;
    private String closer;
    private String closerId;
    private String closerName;
    private Integer procedure;
    private String opener;

}
