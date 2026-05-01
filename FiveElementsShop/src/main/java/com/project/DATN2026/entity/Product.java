package com.project.DATN2026.entity;

import com.project.DATN2026.entity.enumClass.ClothingType;
import lombok.*;
import org.hibernate.annotations.Nationalized;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "Product")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String code;

    @Nationalized
    private String name;

    private LocalDateTime createDate;
    private LocalDateTime updatedDate;
    private int status;

    @Column(nullable = false)
    private boolean deleteFlag;

    @Column(nullable = false)
    private double price;

    @Column(nullable = false)
    private int gender;

    @Nationalized
    private String describe;

    @Column(name = "average_rating", columnDefinition = "float default 0.0")
    private Double averageRating = 0.0;

    @ManyToOne
    @JoinColumn(name = "categoryId")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "brandId")
    private Brand brand;

    @ManyToOne
    @JoinColumn(name = "materialId")
    private Material material;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> image;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductDetail> productDetails;

    public Double getAverageRating() {
        return averageRating != null ? averageRating : 0.0;
    }
}