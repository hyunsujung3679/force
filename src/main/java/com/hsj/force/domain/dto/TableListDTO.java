package com.hsj.force.domain.dto;

import com.hsj.force.domain.entity.TTable;
import lombok.Data;

@Data
public class TableListDTO {

    private String tableNo;
    private String tableName;
    private int tableTotalPrice;

    public TableListDTO() {
    }

    public TableListDTO(TTable table) {
        tableNo = table.getTableNo();
        tableName = table.getTableName();
    }
}
