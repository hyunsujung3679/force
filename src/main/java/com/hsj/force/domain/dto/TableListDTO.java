package com.hsj.force.domain.dto;

import com.hsj.force.domain.entity.TTable;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

@Data
public class TableListDTO {

    private String tableNo;
    private String tableName;
    private int tableTotalPrice;

    public TableListDTO() {}

    @QueryProjection
    public TableListDTO(String tableNo, String tableName) {
        this.tableNo = tableNo;
        this.tableName = tableName;
    }

    public TableListDTO(TTable table) {
        tableNo = table.getTableNo();
        tableName = table.getTableName();
    }

}
