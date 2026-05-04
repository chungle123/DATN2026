package com.project.DATN2026.controller.admin;

import com.project.DATN2026.dto.Refund.SearchRefundDto;
import com.project.DATN2026.entity.MockBankAccount;
import com.project.DATN2026.entity.Payment;
import com.project.DATN2026.entity.enumClass.PaymentStatus;
import com.project.DATN2026.repository.BillRepository;
import com.project.DATN2026.repository.BillReturnRepository;
import com.project.DATN2026.repository.MockBankAccountRepository;
import com.project.DATN2026.repository.PaymentRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class RefundController {
    private final BillReturnRepository billReturnRepository;

    private final BillRepository billRepository;
    private final PaymentRepository paymentRepository;
    private final MockBankAccountRepository mockBankAccountRepository;

    public RefundController(BillReturnRepository billReturnRepository, BillRepository billRepository, PaymentRepository paymentRepository, MockBankAccountRepository mockBankAccountRepository) {
        this.billReturnRepository = billReturnRepository;
        this.billRepository = billRepository;
        this.paymentRepository = paymentRepository;
        this.mockBankAccountRepository = mockBankAccountRepository;
    }

    @GetMapping("/admin-only/need-refund-mng")
    public String viewRefundPage(SearchRefundDto searchRefundDto, Model model) {
        model.addAttribute("refundList", billRepository.findListNeedRefund());
        return "/admin/refund-mng";
    }

    @PostMapping("/admin/confirm-refund/{id}")
    public String confirmRefund(@PathVariable String id, RedirectAttributes redirectAttributes) {
        Payment payment = paymentRepository.findByOrderId(id);
        if (payment != null) {
            // QUAN TRỌNG: Chỉ hoàn tiền nếu trạng thái là APPROVED (chưa hoàn)
            // Nếu là REJECTED nghĩa là đã được hệ thống tự động hoàn hoặc admin bấm trước đó rồi
            if (payment.getPaymentStatus() != PaymentStatus.APPROVED) {
                redirectAttributes.addFlashAttribute("infoMessage", "Giao dịch này đã được hoàn tiền (Trạng thái: " + payment.getPaymentStatus() + ")");
                return "redirect:/admin-only/need-refund-mng";
            }

            try {
                double refundAmount = Double.parseDouble(payment.getAmount());
                // Tìm TK user theo thông tin đã lưu lúc thanh toán
                MockBankAccount userAccount = mockBankAccountRepository
                        .findByAccountNumberAndBankName(payment.getAccountNumber(), payment.getBankName());
                MockBankAccount shopAccount = mockBankAccountRepository.findByIsShopAccountTrue();

                if (userAccount != null && shopAccount != null && shopAccount.getBalance() >= refundAmount) {
                    // Trừ tiền Shop, cộng lại cho Khách
                    shopAccount.setBalance(shopAccount.getBalance() - refundAmount);
                    userAccount.setBalance(userAccount.getBalance() + refundAmount);
                    
                    mockBankAccountRepository.save(shopAccount);
                    mockBankAccountRepository.save(userAccount);
                    
                    payment.setStatusExchange(1);
                    payment.setPaymentStatus(PaymentStatus.REJECTED); // Đánh dấu là đã hoàn tiền
                    paymentRepository.save(payment);
                    
                    redirectAttributes.addFlashAttribute("successMessage", "Xác nhận hoàn " + String.format("%,.0f", refundAmount) + " cho mã giao dịch " + payment.getOrderId() + " thành công");
                } else {
                    String error = "Không thể hoàn tiền: ";
                    if (userAccount == null) error += "Không tìm thấy tài khoản khách (" + payment.getAccountNumber() + "). ";
                    if (shopAccount == null) error += "Không tìm thấy tài khoản Shop. ";
                    if (shopAccount != null && shopAccount.getBalance() < refundAmount) error += "Số dư Shop không đủ. ";
                    redirectAttributes.addFlashAttribute("errorMessage", error);
                }
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("errorMessage", "Lỗi xử lý hoàn tiền: " + e.getMessage());
            }
        }
        return "redirect:/admin-only/need-refund-mng";
    }

}
