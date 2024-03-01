package com.hsj.force.open.service;

import com.hsj.force.common.ComUtils;
import com.hsj.force.domain.dto.OpenDTO;
import com.hsj.force.domain.dto.OpenSaveDTO;
import com.hsj.force.open.repository.OpenMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import static com.hsj.force.common.Constants.*;

@Service
@RequiredArgsConstructor
public class OpenService {

    public final OpenMapper openMapper;

    public int selectIsOpen(String storeNo) {
        return openMapper.selectIsOpen(storeNo);
    }

    public OpenDTO selectOpenInfo(String storeNo) {

        OpenDTO open = openMapper.selectOpenInfo(storeNo);
        if(open != null) {
            open.setCloser(open.getCloserId() + " - " + open.getCloserName());
            open.setCloseDate(ComUtils.stringTolocalDateTime(open.getModifyDate()));
            open.setCloseTime(ComUtils.stringTolocalDateTime(open.getModifyDate()));
        } else {
            open = new OpenDTO();
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

    public int insertOpen(OpenSaveDTO openSave) {
        openSave.setOpenCloseNo(ComUtils.getNextNo(openMapper.selectOpenCloseNo(openSave.getStoreNo()), OPEN_CLOSE_NO_PREFIX));
        openSave.setOpenCloseSeq(ComUtils.getNextSeq(openMapper.selectOpenCloseSeq(openSave.getStoreNo())));
        return openMapper.insertOpen(openSave);
    }
}
