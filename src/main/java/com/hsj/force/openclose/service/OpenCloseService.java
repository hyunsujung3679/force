package com.hsj.force.openclose.service;

import com.hsj.force.common.ComUtils;
import com.hsj.force.domain.out.OpenCloseOutDTO;
import com.hsj.force.openclose.repository.OpenCloseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class OpenCloseService {

    public final OpenCloseMapper openCloseMapper;

    public int selectIsOpen() {
        return openCloseMapper.selectIsOpen();
    }

    public OpenCloseOutDTO selectOpenCloseInfo() throws ParseException {
        OpenCloseOutDTO openCloseOutDTO = openCloseMapper.selectOpenCloseInfo();
        openCloseOutDTO.setCloser(openCloseOutDTO.getCloserId() + " - " + openCloseOutDTO.getCloserName());
        openCloseOutDTO.setCloseDate(ComUtils.simpleDataFormat(openCloseOutDTO.getModifyDate(), "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd (E)"));
        openCloseOutDTO.setCloseTime(ComUtils.simpleDataFormat(openCloseOutDTO.getModifyDate(), "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm"));
        openCloseOutDTO.setProcedure(Integer.valueOf(openCloseMapper.selectOpenCloseSeq()));

        return openCloseOutDTO;
    }
}
