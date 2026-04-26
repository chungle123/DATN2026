package com.project.DATN2026.controller.api;

import com.project.DATN2026.config.ConfigVNPay;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class MockVNPayController {

    @GetMapping("/mock-vnpay/pay")
    public String showMockPaymentPage(HttpServletRequest request, Model model) {
        // Lấy tất cả tham số truyền từ hệ thống sang VNPAY
        Map<String, String> vnp_Params = new HashMap<>();
        for (Enumeration<String> params = request.getParameterNames(); params.hasMoreElements(); ) {
            String fieldName = params.nextElement();
            String fieldValue = request.getParameter(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                vnp_Params.put(fieldName, fieldValue);
            }
        }
        
        // Truyền thông tin ra view giả lập
        model.addAttribute("vnp_Params", vnp_Params);
        
        String amountStr = vnp_Params.get("vnp_Amount");
        long amount = amountStr != null ? Long.parseLong(amountStr) / 100 : 0;
        model.addAttribute("amount", amount);
        model.addAttribute("txnRef", vnp_Params.get("vnp_TxnRef"));
        model.addAttribute("orderInfo", vnp_Params.get("vnp_OrderInfo"));
        
        return "mock-vnpay"; // Render ra view mock-vnpay.html
    }

    @PostMapping("/mock-vnpay/process")
    public String processMockPayment(@RequestParam("action") String action, HttpServletRequest request) throws UnsupportedEncodingException {
        // Các tham số trả về vnp_ReturnUrl
        Map<String, String> responseParams = new HashMap<>();
        
        responseParams.put("vnp_Amount", request.getParameter("vnp_Amount"));
        responseParams.put("vnp_BankCode", "NCB"); // Giả lập ngân hàng NCB
        responseParams.put("vnp_BankTranNo", "VNP123456789");
        responseParams.put("vnp_CardType", "ATM");
        responseParams.put("vnp_OrderInfo", request.getParameter("vnp_OrderInfo"));
        
        // Thời gian giả lập
        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_PayDate = formatter.format(cld.getTime());
        responseParams.put("vnp_PayDate", vnp_PayDate);
        
        // Tùy theo người dùng chọn Thành công hay Hủy
        String responseCode = "success".equals(action) ? "00" : "24";
        responseParams.put("vnp_ResponseCode", responseCode);
        responseParams.put("vnp_TransactionStatus", responseCode); // Status cũng là 00 cho thành công
        
        responseParams.put("vnp_TmnCode", request.getParameter("vnp_TmnCode"));
        responseParams.put("vnp_TransactionNo", "1357902468"); // Mã ngẫu nhiên
        responseParams.put("vnp_TxnRef", request.getParameter("vnp_TxnRef"));

        // Tạo chuỗi mã hóa SecureHash
        List<String> fieldNames = new ArrayList<>(responseParams.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator<String> itr = fieldNames.iterator();
        
        while (itr.hasNext()) {
            String fieldName = itr.next();
            String fieldValue = responseParams.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                //Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                //Build query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        
        String queryUrl = query.toString();
        // Dùng secretKey trong cấu hình để tạo chữ ký
        String vnp_SecureHash = ConfigVNPay.hmacSHA512(ConfigVNPay.secretKey, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        
        String returnUrl = request.getParameter("vnp_ReturnUrl");
        return "redirect:" + returnUrl + "?" + queryUrl;
    }
}
