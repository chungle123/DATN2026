package com.project.DATN2026.config;

import com.project.DATN2026.entity.DiscountCode;
import com.project.DATN2026.repository.DiscountCodeRepository;
import com.project.DATN2026.service.DiscountCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class ScheduledApp {
    @Autowired
    private DiscountCodeService discountCodeService;

    @Autowired
    private DiscountCodeRepository discountCodeRepository;

    @Scheduled(fixedRate = 60*60*100) // Run every 24 hours, adjust as needed
    public void checkAndSetExpiredStatus() {
        Date currentDate = new Date();
        List<DiscountCode> expiredDiscountCodes = null;
        if(!expiredDiscountCodes.isEmpty()) {
            for (DiscountCode discountCode : expiredDiscountCodes) {
                if (currentDate.after(discountCode.getEndDate())) {
                    discountCode.setStatus(3);
                    discountCodeRepository.save(discountCode);
                }
            }
        }

    }
}
