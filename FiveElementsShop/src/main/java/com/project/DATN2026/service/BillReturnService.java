package com.project.DATN2026.service;


import com.project.DATN2026.dto.BillReturn.BillReturnCreateDto;
import com.project.DATN2026.dto.BillReturn.BillReturnDetailDto;
import com.project.DATN2026.dto.BillReturn.BillReturnDto;
import com.project.DATN2026.dto.BillReturn.SearchBillReturnDto;

import java.util.List;

public interface BillReturnService {
    List<BillReturnDto> getAllBillReturns(SearchBillReturnDto searchBillReturnDto);

    BillReturnDto createBillReturn(BillReturnCreateDto billReturnCreateDto);

    BillReturnDetailDto getBillReturnDetailById(Long id);
    BillReturnDetailDto getBillReturnDetailByCode(String code);

    String generateHtmlContent(Long billReturnId);

    BillReturnDto updateStatus(Long id, int returnStatus);
}
