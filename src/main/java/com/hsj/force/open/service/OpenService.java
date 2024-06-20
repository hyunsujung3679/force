package com.hsj.force.open.service;

import com.hsj.force.common.ComUtils;
import com.hsj.force.domain.dto.OpenCloseInsertDTO;
import com.hsj.force.domain.entity.TOpenClose;
import com.hsj.force.domain.entity.TUser;
import com.hsj.force.domain.entity.embedded.CommonData;
import com.hsj.force.domain.entity.embedded.TOpenCloseId;
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

    //TODO: 삭제필요
    public int selectIsOpen(String storeNo) {
        return openMapper.selectIsOpen(storeNo);
    }

    public boolean findIsOpen(String storeNo) {
        return openRepository.findIsOpen(storeNo);
    }

    public OpenCloseInsertDTO findOpenInfo(String storeNo) {

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
        // TODO: JPA 적용 필요
        String procedureStr = openMapper.selectOpenCloseSeq(storeNo);
        if(procedureStr == null) {
            openCloseInsertDTO.setProcedure(procedure);
        } else {
            openCloseInsertDTO.setProcedure(Integer.parseInt(procedureStr) + 1);
        }

        return openCloseInsertDTO;
    }

    public void saveOpen(int openMoeny, String storeNo, String userId) {

        String openCloseNo = "";
        if(openRepository.findOpenCloseNo(storeNo).isPresent()) {
            openCloseNo = openRepository.findOpenCloseNo(storeNo).get();
        }

        TOpenClose open = new TOpenClose();
        TOpenCloseId id = new TOpenCloseId();
        open.setOpenCloseId(id);

        open.getOpenCloseId().setOpenCloseNo(ComUtils.getNextNo(openCloseNo, OPEN_CLOSE_NO_PREFIX));
        // TODO: JPA 적용 필요
        open.setOpenCloseSeq(ComUtils.getNextSeq(openMapper.selectOpenCloseSeq(storeNo)));
        open.getOpenCloseId().setStoreNo(storeNo);
        open.setOpenMoney(openMoeny);
        open.setCloseMoney(null);
        open.setInsertId(userId);
        open.setInsertDate(LocalDateTime.now());
        open.setModifyId(userId);
        open.setModifyDate(LocalDateTime.now());

        openRepository.saveOpen(open);
    }
}
