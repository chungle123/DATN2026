package com.project.DATN2026.controller.api;

import com.project.DATN2026.config.ConfigVNPay;
import com.project.DATN2026.entity.MockBankAccount;
import com.project.DATN2026.entity.Payment;
import com.project.DATN2026.entity.enumClass.PaymentStatus;
import com.project.DATN2026.repository.MockBankAccountRepository;
import com.project.DATN2026.repository.PaymentRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class MockVNPayController {

    private final PaymentRepository paymentRepository;
    private final MockBankAccountRepository bankAccountRepository;

    public MockVNPayController(PaymentRepository paymentRepository,
                               MockBankAccountRepository bankAccountRepository) {
        this.paymentRepository = paymentRepository;
        this.bankAccountRepository = bankAccountRepository;
    }

    // ===================== USER PAYMENT PAGE =====================

    @GetMapping("/mock-vnpay/pay")
    public String showMockPaymentPage(HttpServletRequest request, Model model) {
        Map<String, String> params = new HashMap<>();
        for (Enumeration<String> p = request.getParameterNames(); p.hasMoreElements(); ) {
            String k = p.nextElement();
            String v = request.getParameter(k);
            if (v != null && !v.isEmpty()) params.put(k, v);
        }
        String amountStr = params.get("vnp_Amount");
        long amount = amountStr != null ? Long.parseLong(amountStr) / 100 : 0;
        model.addAttribute("amount", amount);
        model.addAttribute("txnRef", params.get("vnp_TxnRef"));

        // Lấy danh sách tên ngân hàng cho dropdown
        List<MockBankAccount> all = bankAccountRepository.findAll();
        List<String> banks = all.stream()
                .map(MockBankAccount::getBankName)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
        model.addAttribute("banks", banks);
        return "mock-vnpay";
    }

    // ===================== PUBLIC BANK ACCOUNTS PAGE =====================

    @GetMapping("/mock-bank/accounts")
    public String bankAccountsPage() {
        return "mock-bank-accounts";
    }

    /**
     * API: Lấy tất cả tài khoản (public - để trang ngoài xem)
     */
    @GetMapping("/api/mock-bank/all-accounts")
    @ResponseBody
    public ResponseEntity<List<Map<String, Object>>> getAllAccounts() {
        List<MockBankAccount> accounts = bankAccountRepository.findAll();
        List<Map<String, Object>> result = accounts.stream().map(a -> {
            Map<String, Object> m = new HashMap<>();
            m.put("id", a.getId());
            m.put("bankName", a.getBankName());
            m.put("accountNumber", a.getAccountNumber());
            m.put("accountHolder", a.getAccountHolder());
            m.put("balance", a.getBalance());
            m.put("isShopAccount", a.getIsShopAccount());
            return m;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    // ===================== VALIDATE ACCOUNT =====================

    /**
     * API: Validate thông tin tài khoản user nhập
     */
    @PostMapping("/api/mock-bank/validate-account")
    @ResponseBody
    public ResponseEntity<?> validateAccount(@RequestBody Map<String, String> body) {
        String bankName = body.get("bankName");
        String accountNumber = body.get("accountNumber");
        String accountHolder = body.get("accountHolder");
        double amount = Double.parseDouble(body.getOrDefault("amount", "0"));

        MockBankAccount account = bankAccountRepository.findByAccountNumberAndBankName(accountNumber, bankName);
        if (account == null || account.getIsShopAccount()) {
            return ResponseEntity.badRequest().body(Map.of("valid", false, "message", "Số tài khoản hoặc ngân hàng không đúng"));
        }
        // Check tên chủ TK (case-insensitive, bỏ dấu)
        String inputHolder = normalize(accountHolder);
        String dbHolder = normalize(account.getAccountHolder());
        if (!inputHolder.equals(dbHolder)) {
            return ResponseEntity.badRequest().body(Map.of("valid", false, "message", "Tên chủ tài khoản không khớp"));
        }
        if (account.getBalance() < amount) {
            return ResponseEntity.badRequest().body(Map.of("valid", false, "message",
                    "Số dư không đủ (Còn: " + String.format("%,.0f", account.getBalance()) + " VNĐ)"));
        }
        Map<String, Object> res = new HashMap<>();
        res.put("valid", true);
        res.put("accountId", account.getId());
        res.put("balance", account.getBalance());
        res.put("displayName", account.getAccountHolder());
        return ResponseEntity.ok(res);
    }

    // ===================== PROCESS PAYMENT =====================

    @PostMapping("/mock-vnpay/process-payment")
    @ResponseBody
    public ResponseEntity<?> processPayment(@RequestBody Map<String, Object> payload) {
        Long accountId = Long.parseLong(payload.get("accountId").toString());
        String txnRef = payload.get("txnRef").toString();
        double amount = Double.parseDouble(payload.get("amount").toString());

        MockBankAccount userAccount = bankAccountRepository.findById(accountId).orElse(null);
        if (userAccount == null)
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Tài khoản không tồn tại"));

        if (userAccount.getBalance() < amount)
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Số dư không đủ"));

        MockBankAccount shopAccount = bankAccountRepository.findByIsShopAccountTrue();
        if (shopAccount == null)
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Lỗi hệ thống"));

        // Trừ user, cộng shop
        userAccount.setBalance(userAccount.getBalance() - amount);
        shopAccount.setBalance(shopAccount.getBalance() + amount);
        bankAccountRepository.save(userAccount);
        bankAccountRepository.save(shopAccount);

        // Cập nhật Payment
        Payment payment = paymentRepository.findByOrderId(txnRef);
        if (payment != null) {
            payment.setBankName(userAccount.getBankName());
            payment.setAccountNumber(userAccount.getAccountNumber());
            payment.setAccountHolder(userAccount.getAccountHolder());
            payment.setTransactionContent("Thanh toan don hang " + txnRef);
            payment.setPaymentStatus(PaymentStatus.APPROVED);
            payment.setPaymentDate(LocalDateTime.now());
            paymentRepository.save(payment);
        }

        return ResponseEntity.ok(Map.of(
                "success", true,
                "newBalance", userAccount.getBalance(),
                "shopBalance", shopAccount.getBalance()
        ));
    }

    // ===================== COMPLETE / CANCEL =====================

    @GetMapping("/mock-vnpay/complete/{orderId}")
    public String completePayment(@PathVariable String orderId) throws UnsupportedEncodingException {
        Payment payment = paymentRepository.findByOrderId(orderId);
        if (payment == null) return "redirect:/";

        boolean approved = payment.getPaymentStatus() == PaymentStatus.APPROVED;
        String responseCode = approved ? "00" : "24";

        Map<String, String> params = new LinkedHashMap<>();
        long amtLong = (long)(Double.parseDouble(payment.getAmount()) * 100);
        params.put("vnp_Amount", String.valueOf(amtLong));
        params.put("vnp_BankCode", payment.getBankName() != null ? payment.getBankName() : "NCB");
        params.put("vnp_BankTranNo", "VNP" + System.currentTimeMillis());
        params.put("vnp_CardType", "ATM");
        params.put("vnp_OrderInfo", "Thanh toan don hang:" + orderId);
        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        params.put("vnp_PayDate", new SimpleDateFormat("yyyyMMddHHmmss").format(cld.getTime()));
        params.put("vnp_ResponseCode", responseCode);
        params.put("vnp_TransactionStatus", responseCode);
        params.put("vnp_TmnCode", ConfigVNPay.vnp_TmnCode);
        params.put("vnp_TransactionNo", String.valueOf(System.currentTimeMillis()));
        params.put("vnp_TxnRef", orderId);

        List<String> keys = new ArrayList<>(params.keySet());
        Collections.sort(keys);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        for (int i = 0; i < keys.size(); i++) {
            String k = keys.get(i), v = params.get(k);
            if (v != null && !v.isEmpty()) {
                hashData.append(k).append('=').append(URLEncoder.encode(v, StandardCharsets.US_ASCII.toString()));
                query.append(URLEncoder.encode(k, StandardCharsets.US_ASCII.toString()))
                     .append('=').append(URLEncoder.encode(v, StandardCharsets.US_ASCII.toString()));
                if (i < keys.size() - 1) { hashData.append('&'); query.append('&'); }
            }
        }
        String secureHash = ConfigVNPay.hmacSHA512(ConfigVNPay.secretKey, hashData.toString());
        return "redirect:" + ConfigVNPay.vnp_ReturnUrl + "?" + query + "&vnp_SecureHash=" + secureHash;
    }

    @PostMapping("/mock-vnpay/cancel-payment")
    @ResponseBody
    public ResponseEntity<?> cancelPayment(@RequestBody Map<String, String> payload) {
        Payment payment = paymentRepository.findByOrderId(payload.get("txnRef"));
        if (payment != null) {
            payment.setPaymentStatus(PaymentStatus.REJECTED);
            paymentRepository.save(payment);
        }
        return ResponseEntity.ok(Map.of("message", "Đã hủy"));
    }

    private String normalize(String s) {
        if (s == null) return "";
        return s.trim().toUpperCase()
                .replaceAll("[ÀÁÂÃÄÅĀĂĄẤẦẨẪẬẮẰẲẴẶ]", "A")
                .replaceAll("[ÈÉÊËĒĔĖĘĚẾỀỂỄỆ]", "E")
                .replaceAll("[ÌÍÎÏĪĬĮĨỈỊ]", "I")
                .replaceAll("[ÒÓÔÕÖŌŎŐƠỐỒỔỖỘỚỜỞỠỢ]", "O")
                .replaceAll("[ÙÚÛÜŪŬŮŰŲƯỨỪỬỮỰ]", "U")
                .replaceAll("[ÝŶŸỳýỷỹỵ]", "Y")
                .replaceAll("[Đ]", "D")
                .replaceAll("\\s+", " ");
    }
}
