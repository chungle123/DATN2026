package com.project.DATN2026.service.serviceImpl;

import com.project.DATN2026.dto.DiscountCode.DiscountCodeDto;
import com.project.DATN2026.dto.DiscountCode.SearchDiscountCodeDto;
import com.project.DATN2026.dto.Product.SearchProductDto;
import com.project.DATN2026.entity.Color;
import com.project.DATN2026.entity.DiscountCode;
import com.project.DATN2026.exception.NotFoundException;
import com.project.DATN2026.exception.ShopApiException;
import com.project.DATN2026.repository.DiscountCodeRepository;
import com.project.DATN2026.repository.Specification.DiscountCodeSpec;
import com.project.DATN2026.service.DiscountCodeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class DiscountCodeImpl implements DiscountCodeService {
    private final DiscountCodeRepository discountCodeRepository;

    public DiscountCodeImpl(DiscountCodeRepository discountCodeRepository) {
        this.discountCodeRepository = discountCodeRepository;
    }

    @Override
    public Page<DiscountCodeDto> getAllDiscountCode(SearchDiscountCodeDto searchDiscountCodeDto, Pageable pageable) {
        Specification<DiscountCode> spec = new DiscountCodeSpec(searchDiscountCodeDto);
        Page<DiscountCode> discountCodes = discountCodeRepository.findAll(spec, pageable);
        return discountCodes.map(this::convertToDto);
    }

    @Override
    public DiscountCodeDto saveDiscountCode(DiscountCodeDto discountCodeDto) {
       if (discountCodeRepository.existsByCode(discountCodeDto.getCode())) {
            throw new ShopApiException(HttpStatus.BAD_REQUEST, "Mã giảm giá "+discountCodeDto.getCode()+" đã tồn tại");
        }
        DiscountCode discountCode = convertToEntity(discountCodeDto);
        discountCode.setStatus(1);
        discountCode.setDeleteFlag(false);
        DiscountCode discountCodeSave = discountCodeRepository.save(discountCode);
        return convertToDto(discountCodeSave);
    }

    @Override
    public DiscountCodeDto updateDiscountCode(DiscountCodeDto discountCodeDto) {
        DiscountCode existingColor = discountCodeRepository.findById(discountCodeDto.getId()).orElseThrow(() -> new NotFoundException("Không tìm thấy mã giảm giá " + discountCodeDto.getCode()) );
        if(!existingColor.getCode().equals(discountCodeDto.getCode())) {
            if(discountCodeRepository.existsByCode(discountCodeDto.getCode())) {
                throw new ShopApiException(HttpStatus.BAD_REQUEST, "Mã giảm giá " + discountCodeDto.getCode() + " đã tồn tại");
            }
        }

        DiscountCode discountCode = convertToEntity(discountCodeDto);
        discountCode.setStatus(1);
        discountCode.setDeleteFlag(false);
        DiscountCode discountCodeSave = discountCodeRepository.save(discountCode);
        return convertToDto(discountCodeSave);
    }

    public boolean isExistsDiscountCode(String code) {
        return discountCodeRepository.existsByCode(code);
    }

    @Override
    public DiscountCodeDto getDiscountCodeById(Long id) {
        DiscountCode discountCode = discountCodeRepository.findById(id).orElseThrow(() -> new NotFoundException("Không tìm thấy mã giảm giá"));
        return convertToDto(discountCode);
    }

    @Override
    public DiscountCodeDto getDiscountCodeByCode(Long code) {
        return null;
    }

    @Override
    public DiscountCodeDto updateStatus(Long discountCodeId, int status) {
        DiscountCode discountCode = discountCodeRepository.findById(discountCodeId).orElseThrow(() -> new NotFoundException("Không tìm thấy mã giảm giá"));
        discountCode.setStatus(status);
        return convertToDto(discountCodeRepository.save(discountCode));
    }

    @Override
    public Page<DiscountCodeDto> getAllAvailableDiscountCode(Pageable pageable) {
        Page<DiscountCode> validCodes = discountCodeRepository.findAllAvailableValid(pageable);
        return validCodes.map(this::convertToDto);
    }

    private DiscountCodeDto convertToDto(DiscountCode discountCode) {
        DiscountCodeDto dto = new DiscountCodeDto();
        dto.setId(discountCode.getId());
        dto.setCode(discountCode.getCode().trim());
        dto.setDiscountAmount(discountCode.getDiscountAmount());
        dto.setMaximumAmount(discountCode.getMaximumAmount());
        dto.setDetail(discountCode.getDetail());
        dto.setPercentage(discountCode.getPercentage());
        dto.setStartDate(discountCode.getStartDate());
        dto.setEndDate(discountCode.getEndDate());
        dto.setType(discountCode.getType());
        dto.setMinimumAmountInCart(discountCode.getMinimumAmountInCart());
        dto.setMaximumUsage(discountCode.getMaximumUsage());
        dto.setStatus(discountCode.getStatus());
        return dto;
    }

    private DiscountCode convertToEntity(DiscountCodeDto discountCodeDto) {
        DiscountCode discountCode = new DiscountCode();
        discountCode.setId(discountCodeDto.getId());
        discountCode.setCode(discountCodeDto.getCode() != null ? discountCodeDto.getCode().trim() : "");
        
        // Handle potentially null fields that might cause DB ConstraintViolation
        discountCode.setDetail((discountCodeDto.getDetail() != null && !discountCodeDto.getDetail().isEmpty()) 
                               ? discountCodeDto.getDetail() : discountCode.getCode());
                               
        discountCode.setMaximumAmount(discountCodeDto.getMaximumAmount() != null ? discountCodeDto.getMaximumAmount() : 0);
        discountCode.setDiscountAmount(discountCodeDto.getDiscountAmount() != null ? discountCodeDto.getDiscountAmount() : 0.0);
        discountCode.setPercentage(discountCodeDto.getPercentage() != null ? discountCodeDto.getPercentage() : 0);
        discountCode.setMinimumAmountInCart(discountCodeDto.getMinimumAmountInCart() != null ? discountCodeDto.getMinimumAmountInCart() : 0.0);
        
        discountCode.setStartDate(discountCodeDto.getStartDate() != null ? discountCodeDto.getStartDate() : new java.util.Date());
        discountCode.setEndDate(discountCodeDto.getEndDate() != null ? discountCodeDto.getEndDate() : new java.util.Date());
        discountCode.setType(discountCodeDto.getType() != null ? discountCodeDto.getType() : 1);
        discountCode.setMaximumUsage(discountCodeDto.getMaximumUsage());
        discountCode.setStatus(discountCodeDto.getStatus());
        discountCode.setDeleteFlag(false);
        return discountCode;
    }
}
