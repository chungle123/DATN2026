package com.project.DATN2026.config;

import com.project.DATN2026.entity.MockBankAccount;
import com.project.DATN2026.repository.MockBankAccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1)
public class MockBankDataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(MockBankDataInitializer.class);
    private final MockBankAccountRepository bankAccountRepository;

    public MockBankDataInitializer(MockBankAccountRepository bankAccountRepository) {
        this.bankAccountRepository = bankAccountRepository;
    }

    @Override
    public void run(String... args) {
        try {
            long count = bankAccountRepository.count();
            log.info("MockBankAccount count: {}", count);
            if (count > 0) {
                log.info("MockBankAccount data already exists, skipping seed.");
                return;
            }

            log.info("Seeding MockBankAccount data...");

            // Tài khoản SHOP
            bankAccountRepository.save(new MockBankAccount("Vietcombank", "1900888868", "FIVE ELEMENTS SHOP", 50000000.0, true));

            // Tài khoản khách hàng mẫu
            bankAccountRepository.save(new MockBankAccount("Vietcombank", "1234567890", "NGUYEN VAN A", 10000000.0, false));
            bankAccountRepository.save(new MockBankAccount("Techcombank", "9876543210", "TRAN THI B", 5000000.0, false));
            bankAccountRepository.save(new MockBankAccount("MB Bank", "1122334455", "LE VAN C", 20000000.0, false));
            bankAccountRepository.save(new MockBankAccount("BIDV", "5566778899", "PHAM THI D", 8000000.0, false));
            bankAccountRepository.save(new MockBankAccount("Agribank", "6677889900", "HOANG VAN E", 15000000.0, false));
            bankAccountRepository.save(new MockBankAccount("VPBank", "2233445566", "DO THI F", 3000000.0, false));
            bankAccountRepository.save(new MockBankAccount("TPBank", "3344556677", "VU VAN G", 12000000.0, false));
            bankAccountRepository.save(new MockBankAccount("Sacombank", "4455667788", "BUI THI H", 7000000.0, false));
            bankAccountRepository.save(new MockBankAccount("ACB", "5566778800", "DANG VAN I", 25000000.0, false));

            log.info("MockBankAccount seeded successfully! Total: {}", bankAccountRepository.count());
        } catch (Exception e) {
            log.error("Error seeding MockBankAccount data: {}", e.getMessage());
        }
    }
}
