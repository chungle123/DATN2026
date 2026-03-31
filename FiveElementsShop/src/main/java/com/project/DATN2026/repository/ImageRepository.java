package com.project.DATN2026.repository;

import com.project.DATN2026.entity.Image;
import com.project.DATN2026.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {

    List<Image> findAllByProduct(Product product);
    Image findImageById(Long id);
}