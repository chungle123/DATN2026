package com.project.DATN2026.dto.Bill;

import com.project.DATN2026.entity.enumClass.BillStatus;
import com.project.DATN2026.entity.enumClass.InvoiceType;

import java.time.LocalDateTime;

public interface BillDetailDtoInterface {
    String getMaDonHang();

    String getMaDinhDanh();
    String getDiaChi();

    Double getTongTien();

    Double getTienKhuyenMai();

    String getTenKhachHang();

    String getSoDienThoai();

    String getEmail();

    BillStatus getTrangThaiDonHang();

    String getPhuongThucThanhToan();

    String getMaGiaoDich();

    InvoiceType getLoaiHoaDon();

    String getVoucherName();

    LocalDateTime getCreatedDate();
}
