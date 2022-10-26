package com.finalProject.mutbook.service.cash;

import com.finalProject.mutbook.domain.cash.CashLog;
import com.finalProject.mutbook.domain.cash.CashLogRepository;
import com.finalProject.mutbook.domain.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CashService {
    private final CashLogRepository cashLogRepository;

    public CashLog addCash(Member member, long price, String eventType) {
        CashLog cashLog = CashLog.builder()
                .member(member)
                .price(price)
                .eventType(eventType)
                .build();

        cashLogRepository.save(cashLog);

        return cashLog;
    }
}