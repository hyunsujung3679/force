package com.hsj.force.openclose.service;

import com.hsj.force.common.ComUtils;
import com.hsj.force.domain.response.OpenCloseRes;
import com.hsj.force.openclose.repository.OpenCloseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OpenCloseService {

    public final OpenCloseMapper openCloseMapper;

    public int selectIsOpen() {
        return openCloseMapper.selectIsOpen();
    }

    public OpenCloseRes selectOpenCloseInfo() {

        OpenCloseRes openCloseOutDTO = openCloseMapper.selectOpenCloseInfo();
        openCloseOutDTO.setCloser(openCloseOutDTO.getCloserId() + " - " + openCloseOutDTO.getCloserName());
        openCloseOutDTO.setCloseDate(ComUtils.stringTolocalDateTime(openCloseOutDTO.getModifyDate()));
        openCloseOutDTO.setCloseTime(ComUtils.stringTolocalDateTime(openCloseOutDTO.getModifyDate()));

        int procedure = 1;
        String procedureStr = openCloseMapper.selectOpenCloseSeq();
        if(procedureStr == null) {
            openCloseOutDTO.setProcedure(procedure);
        } else {
            openCloseOutDTO.setProcedure(Integer.parseInt(procedureStr) + 1);
        }

        return openCloseOutDTO;
    }
}
