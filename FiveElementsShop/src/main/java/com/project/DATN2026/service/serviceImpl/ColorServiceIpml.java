package com.project.DATN2026.service.serviceImpl;

import com.project.DATN2026.dto.Color.ColorDto;
import com.project.DATN2026.entity.Color;
import com.project.DATN2026.entity.Product;
import com.project.DATN2026.entity.Size;
import com.project.DATN2026.exception.NotFoundException;
import com.project.DATN2026.exception.ShopApiException;
import com.project.DATN2026.repository.ColorRepo;
import com.project.DATN2026.repository.ProductRepository;
import com.project.DATN2026.repository.SizeRepository;
import com.project.DATN2026.service.ColorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ColorServiceIpml implements ColorService {

    @Autowired
    private ColorRepo colorRepo;

    @Autowired
    private SizeRepository sizeRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public Color updateColor(Color color) {
        Color existingColor = colorRepo.findById(color.getId()).orElseThrow(() -> new NotFoundException("Không tìm thấy màu có mã " + color.getCode()) );
        if(!existingColor.getCode().equals(color.getCode())) {
            if(colorRepo.existsByCode(color.getCode())) {
                throw new ShopApiException(HttpStatus.BAD_REQUEST, "Mã màu " + color.getCode() + " đã tồn tại");
            }
        }
        color.setDeleteFlag(false);
        return colorRepo.save(color);
    }

    @Override
    public Color createColor(Color color) {
        if(colorRepo.existsByCode(color.getCode())) {
            throw new ShopApiException(HttpStatus.BAD_REQUEST, "Mã màu " + color.getCode() + " đã tồn tại");
        }
        color.setDeleteFlag(false);
        return colorRepo.save(color);
    }

    @Override
    public void delete(Long id) {
        Color existingColor = colorRepo.findById(id).orElseThrow(() -> new NotFoundException("Không tìm thấy màu có id " + id) );
        existingColor.setDeleteFlag(true);
        colorRepo.save(existingColor);
    }

    @Override
    public List<Color> findAll() {
        return colorRepo.findAll();
    }

    @Override
    public Optional<Color> findById(Long id) {
        return colorRepo.findById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return colorRepo.existsById(id);
    }

    @Override
    public Page<Color> findAll(Integer page, Integer limit) {
        Pageable pageable= PageRequest.of(page,limit);
        return colorRepo.findAll(pageable);
    }

    @Override
    public Page<Color> findAll(Pageable pageable) {
        return colorRepo.findAll(pageable);
    }

    @Override
    public List<Color> getColorByProductId(Long productId) throws NotFoundException {
        Product product = productRepository.findById(productId).orElseThrow(() -> new NotFoundException("Product not found"));
        return colorRepo.findColorsByProduct(product);
    }

    @Override
    public List<Color> getColorsByProductIdAndSizeId(Long productId, Long sizeId) throws NotFoundException {
        Product product = productRepository.findById(productId).orElseThrow(() -> new NotFoundException("Product not found"));
        Size size = sizeRepository.findById(sizeId).orElseThrow(() -> new NotFoundException("Size not found"));
        return colorRepo.findColorsByProductAndSize(product, size);
    }

    @Override
    public ColorDto createColorApi(ColorDto colorDto) {
        if(colorRepo.existsByCode(colorDto.getCode())) {
            throw new ShopApiException(HttpStatus.BAD_REQUEST, "Mã màu đã tồn tại");
        }

        Color color = new Color(null, colorDto.getCode(), colorDto.getName(), false);
        Color colorNew = colorRepo.save(color);
        return new ColorDto(colorNew.getId(), colorNew.getCode(), colorNew.getName());
    }
}
