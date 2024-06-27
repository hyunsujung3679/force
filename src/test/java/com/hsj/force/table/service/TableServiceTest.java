//package com.hsj.force.table.service;
//
//import com.hsj.force.domain.dto.TableDTO;
//import com.hsj.force.domain.dto.TableListDTO;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@SpringBootTest
//@Transactional
//class TableServiceTest {
//
//    @Autowired
//    private TableService tableService;
//    private User loginMember = null;
//
//    @BeforeEach
//    public void beforeEach() {
//        loginMember = new User();
//        loginMember.setStoreNo("S001");
//        loginMember.setUserId("SUSU");
//        loginMember.setUserNo("U001");
//        loginMember.setUserName("정현수");
//        loginMember.setPhoneNum("01027287526");
//        loginMember.setPassword("1234");
//        loginMember.setUseYn("1");
//    }
//
////    @Test
////    void selectTableInfo() {
////        Map<String, Object> map = tableService.selectTableInfo(loginMember);
////        assertThat(map.get("commonLayoutForm")).isNotNull();
////        assertThat(map.get("tableList")).isNotNull();
////        assertThat(map.get("tableTotalPriceList")).isNotNull();
////        assertThat(map.get("tableOfOrderMap")).isNotNull();
////    }
//
//    @Test
//    void selectTableExistOrderList() {
//        List<TableListDTO> tableListList = tableService.selectTableExistOrderList(loginMember.getStoreNo());
//        assertThat(tableListList).isNotNull();
//    }
//
//    @Test
//    void selectTableNotExistOrderList() {
//        List<TableListDTO> tableListList = tableService.selectTableNotExistOrderList(loginMember.getStoreNo());
//        assertThat(tableListList).isNotNull();
//    }
//
//    @Test
//    void moveTable() {
//        TableDTO table = new TableDTO();
//        table.setBeforeTableNo("T001");
//        table.setAfterTableNo("T003");
//        int result = tableService.moveTable(loginMember, table);
//        assertThat(result).isEqualTo(1);
//    }
//
//    @Test
//    void combineTable() {
//        TableDTO table = new TableDTO();
//        table.setFirstTableNo("T001");
//        table.setSecondTableNo("TOO2");
//        int result = tableService.combineTable(loginMember, table);
//        assertThat(result).isEqualTo(1);
//    }
//}