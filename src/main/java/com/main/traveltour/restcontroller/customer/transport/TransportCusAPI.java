package com.main.traveltour.restcontroller.customer.transport;

import com.main.traveltour.dto.customer.TransportationBrandsDto;
import com.main.traveltour.entity.ResponseObject;
import com.main.traveltour.entity.TransportationBrands;
import com.main.traveltour.service.agent.TransportationBrandsService;
import com.main.traveltour.utils.EntityDtoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api/v1/")
public class TransportCusAPI {

    @Autowired
    private TransportationBrandsService transportationBrandsService;

    @GetMapping("customer/transport/find-all-transport-brand")
    public ResponseObject findAllTransportCus(@RequestParam(defaultValue = "0") int page,
                                              @RequestParam(defaultValue = "9") int size) {
        Page<TransportationBrands> brandsPage = transportationBrandsService.findAllCus(PageRequest.of(page, size));
        Page<TransportationBrandsDto> brandsDto = brandsPage.map(brands -> EntityDtoUtils.convertToDto(brands, TransportationBrandsDto.class));

        if (brandsDto.isEmpty()) {
            return new ResponseObject("404", "Không tìm thấy dữ liệu", null);
        } else {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", brandsDto);
        }
    }

    @GetMapping("customer/transport/find-transport-brand-by-id/{brandId}")
    public ResponseObject findTransportCusById(@PathVariable String brandId) {
        TransportationBrands brands = transportationBrandsService.findByTransportBrandId(brandId);
        TransportationBrandsDto brandsDto = EntityDtoUtils.convertToDto(brands, TransportationBrandsDto.class);

        if (brandsDto == null) {
            return new ResponseObject("404", "Không tìm thấy dữ liệu", null);
        } else {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", brandsDto);
        }
    }
}
