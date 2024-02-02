package com.hsj.force.domain.dto;

import com.hsj.force.domain.Order;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class OrderDTO extends Order {

    private String menuName;

    private CommonLayoutDTO commonLayoutForm;

}
