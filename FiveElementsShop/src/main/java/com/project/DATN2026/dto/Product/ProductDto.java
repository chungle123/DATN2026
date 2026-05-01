package com.project.DATN2026.dto.Product;

import com.project.DATN2026.entity.Image;
import com.project.DATN2026.entity.Material;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    private Long id;
    private String code;
    private String name;
    private String description;
    private String categoryName;
    private String imageUrl;
    private Double priceMin;

    private List<ProductDetailDto> productDetailDtos;
    private LocalDateTime createDate;
    private LocalDateTime updatedDate;
    private boolean isDiscounted;
    private Double averageRating;

    public Double getAverageRating() {
        return averageRating != null ? averageRating : 0.0;
    }
}
