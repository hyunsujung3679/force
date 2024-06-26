package com.hsj.force.open.service;

import com.hsj.force.common.ComUtils;
import com.hsj.force.domain.dto.OpenCloseInsertDTO;
import com.hsj.force.domain.entity.TOpenClose;
import com.hsj.force.domain.entity.TUser;
import com.hsj.force.login.repository.LoginJpaRepository;
import com.hsj.force.open.repository.OpenJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.hsj.force.common.Constants.OPEN_CLOSE_NO_PREFIX;

@Transactional
@Service
@RequiredArgsConstructor
public class OpenService {

    public final OpenJpaRepository openJpaRepository;
    public final LoginJpaRepository loginJpaRepository;

    public boolean selectIsOpen() {
        return openJpaRepository.countByCloseMoneyIsNull() > 0;
    }

    public OpenCloseInsertDTO findOpenInfo() {

        TOpenClose open = openJpaRepository.findFirstByOrderByModifyDateDesc();
        TUser user = loginJpaRepository.findOneByUserId(open.getModifyId());

        OpenCloseInsertDTO openCloseInsertDTO = new OpenCloseInsertDTO();
        openCloseInsertDTO.setCloser(user.getUserId() + " - " + user.getUserName());
        openCloseInsertDTO.setCloseDate(open.getModifyDate());
        openCloseInsertDTO.setCloseTime(open.getModifyDate());

        int procedure = 1;
        TOpenClose openCloseSeq = openJpaRepository.findOneV1();
        String procedureStr = openCloseSeq != null? openCloseSeq.getOpenCloseSeq() : null;
        if(procedureStr == null) {
            openCloseInsertDTO.setProcedure(procedure);
        } else {
            openCloseInsertDTO.setProcedure(Integer.parseInt(procedureStr) + 1);
        }

        return openCloseInsertDTO;
    }

    public void insertOpen(int openMoney) {

        TOpenClose open = openJpaRepository.findFirstByOrderByOpenCloseNoDesc();
        String nextOpenCloseNo = ComUtils.getNextNo(open.getOpenCloseNo(), OPEN_CLOSE_NO_PREFIX);

        TOpenClose openCloseSeq = openJpaRepository.findOneV1();
        String nextOpenSeq = ComUtils.getNextSeq(openCloseSeq != null? openCloseSeq.getOpenCloseSeq() : null);

        openJpaRepository.save(new TOpenClose(nextOpenCloseNo, nextOpenSeq, openMoney, null));
    }
}
