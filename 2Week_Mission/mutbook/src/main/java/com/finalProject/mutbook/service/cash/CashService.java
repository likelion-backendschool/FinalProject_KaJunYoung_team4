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

    /**
     * 돈의 흐름 기록을 추가하는 메소드
     * @param member 결제를 진행한 회원
     * @param price 총 결제 금액
     * @param eventType 주문, 환불 등에 대한 내역
     */
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