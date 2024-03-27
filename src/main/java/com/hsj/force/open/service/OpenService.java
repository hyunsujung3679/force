package com.hsj.force.open.service;

import com.hsj.force.common.ComUtils;
import com.hsj.force.domain.dto.OpenCloseInsertDTO;
import com.hsj.force.open.repository.OpenMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.hsj.force.common.Constants.OPEN_CLOSE_NO_PREFIX;

@Transactional
@Service
@RequiredArgsConstructor
public class OpenService {

    public final OpenMapper openMapper;

    public int selectIsOpen(String storeNo) {
        return openMapper.selectIsOpen(storeNo);
    }

    public OpenCloseInsertDTO selectOpenInfo(String storeNo) {

        OpenCloseInsertDTO open = openMapper.selectOpenInfo(storeNo);
        if(open != null) {
            open.setCloser(open.getCloserId() + " - " + open.getCloserName());
            open.setCloseDate(ComUtils.stringTolocalDateTime(open.getModifyDate()));
            open.setCloseTime(ComUtils.stringTolocalDateTime(open.getModifyDate()));
        } else {
            open = new OpenCloseInsertDTO();
        }

        int procedure = 1;
        String procedureStr = openMapper.selectOpenCloseSeq(storeNo);
        if(procedureStr == null) {
            open.setProcedure(procedure);
        } else {
            open.setProcedure(Integer.parseInt(procedureStr) + 1);
        }

        return open;
    }

    public int insertOpen(OpenCloseInsertDTO open) {
        open.setOpenCloseNo(ComUtils.getNextNo(openMapper.selectOpenCloseNo(open.getStoreNo()), OPEN_CLOSE_NO_PREFIX));
        open.setOpenCloseSeq(ComUtils.getNextSeq(openMapper.selectOpenCloseSeq(open.getStoreNo())));
        return openMapper.insertOpen(open);
    }
}
