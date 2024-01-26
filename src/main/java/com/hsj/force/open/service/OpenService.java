package com.hsj.force.open.service;

import com.hsj.force.common.ComUtils;
import com.hsj.force.domain.OpenForm;
import com.hsj.force.domain.OpenSave;
import com.hsj.force.open.repository.OpenMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import static com.hsj.force.common.Constants.*;

@Service
@RequiredArgsConstructor
public class OpenService {

    public final OpenMapper openMapper;

    public int selectIsOpen() {
        return openMapper.selectIsOpen();
    }

    public OpenForm selectOpenInfo() {

        OpenForm openForm = openMapper.selectOpenInfo();
        if(openForm != null) {
            openForm.setCloser(openForm.getCloserId() + " - " + openForm.getCloserName());
            openForm.setCloseDate(ComUtils.stringTolocalDateTime(openForm.getModifyDate()));
            openForm.setCloseTime(ComUtils.stringTolocalDateTime(openForm.getModifyDate()));
        } else {
            openForm = new OpenForm();
        }

        int procedure = 1;
        String procedureStr = openMapper.selectOpenCloseSeq();
        if(procedureStr == null) {
            openForm.setProcedure(procedure);
        } else {
            openForm.setProcedure(Integer.parseInt(procedureStr) + 1);
        }

        return openForm;
    }

    public int insertOpen(OpenSave openSave) {
        openSave.setOpenCloseNo(ComUtils.getNextNo(openMapper.selectOpenCloseNo(), OPEN_CLOSE_NO_PREFIX));
        openSave.setOpenCloseSeq(ComUtils.getNextSeq(openMapper.selectOpenCloseSeq()));
        return openMapper.insertOpen(openSave);
    }
}
