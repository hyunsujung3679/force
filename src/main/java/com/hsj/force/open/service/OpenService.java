package com.hsj.force.open.service;

import com.hsj.force.common.ComUtils;
import com.hsj.force.domain.dto.OpenCloseInsertDTO;
import com.hsj.force.domain.entity.TOpenClose;
import com.hsj.force.domain.entity.TUser;
import com.hsj.force.open.repository.OpenMapper;
import com.hsj.force.open.repository.OpenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.hsj.force.common.Constants.OPEN_CLOSE_NO_PREFIX;

@Transactional
@Service
@RequiredArgsConstructor
public class OpenService {

    public final OpenMapper openMapper;
    public final OpenRepository openRepository;

    public int selectIsOpen(String storeNo) {
        return openMapper.selectIsOpen(storeNo);
    }

    public boolean findIsOpen(String storeNo) {
        return openRepository.findIsOpen(storeNo);
    }

    public OpenCloseInsertDTO findOpen(String storeNo) {

        TOpenClose open = null;
        TUser user = null;
        OpenCloseInsertDTO openCloseInsertDTO = new OpenCloseInsertDTO();
        if(openRepository.findOpen(storeNo).isPresent()) {
            open = openRepository.findOpen(storeNo).get();

            if(openRepository.findUser(open.getModifyId()).isPresent()) {
                user = openRepository.findUser(open.getModifyId()).get();

                openCloseInsertDTO.setCloser(user.getUserId() + " - " + user.getUserName());
                openCloseInsertDTO.setCloseDate(open.getModifyDate());
                openCloseInsertDTO.setCloseTime(open.getModifyDate());
            }
        }

        int procedure = 1;
//        String procedureStr = openRepository.findOpenCloseSeq(storeNo);
        String procedureStr = openMapper.selectOpenCloseSeq(storeNo);
        if(procedureStr == null) {
            openCloseInsertDTO.setProcedure(procedure);
        } else {
            openCloseInsertDTO.setProcedure(Integer.parseInt(procedureStr) + 1);
        }

        return openCloseInsertDTO;
    }

    public int saveOpen(int openMoeny, String storeNo, String userId) {

        TOpenClose open = new TOpenClose();
        open.getTOpenCloseId().setOpenCloseNo(ComUtils.getNextNo(openRepository.findOpenCloseNo(storeNo), OPEN_CLOSE_NO_PREFIX));
        open.setOpenCloseSeq(ComUtils.getNextSeq(openMapper.selectOpenCloseSeq(storeNo)));
        open.getTOpenCloseId().setStoreNo(storeNo);
        open.setOpenMoney(openMoeny);
        open.setInsertId(userId);
        open.setInsertDate(LocalDateTime.now());
        open.setModifyId(userId);
        open.setModifyDate(LocalDateTime.now());

        return openRepository.saveOpen(open);
    }
}
