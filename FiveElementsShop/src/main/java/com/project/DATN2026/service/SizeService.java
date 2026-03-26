package com.project.DATN2026.service;

import com.project.DATN2026.dto.Size.SizeDto;
import com.project.DATN2026.entity.Size;
import com.project.DATN2026.exception.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface SizeService {

    Page<Size> getAllSize(Pageable pageable);

    Size save(Size size);

    Size createSize(Size size);
    Size updateSize(Size size);

    void delete(Long id);

    Optional<Size> findById(Long id);

    List<Size> getAll();

    List<Size> getSizesByProductId(Long productId) throws NotFoundException;

    List<Size> getSizesByProductIdAndColorId(Long productId, Long colorId) throws NotFoundException;

    SizeDto createSizeApi(SizeDto sizeDto);
}
