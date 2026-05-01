package com.project.DATN2026.dto.Product;

public interface ProductSearchDto {
    Long getIdSanPham();
    String getMaSanPham();
    String getTenSanPham();
    String getImage();
    String getNhanHang();
    String getChatLieu();
    String getTheLoai();

    String getTrangThai();
    Double getAverageRating();
}
