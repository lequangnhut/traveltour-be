package com.main.traveltour.restcontroller.staff;

import com.main.traveltour.dto.staff.BookingToursDto;
import com.main.traveltour.dto.staff.InvoicesDto;
import com.main.traveltour.entity.BookingTours;
import com.main.traveltour.entity.Invoices;
import com.main.traveltour.entity.ResponseObject;
import com.main.traveltour.service.staff.BookingTourService;
import com.main.traveltour.service.staff.InvoicesService;
import com.main.traveltour.utils.EntityDtoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api/v1/staff/invoices/")
public class InvoicesAPI {

    @Autowired
    private InvoicesService invoicesService;

    @GetMapping("find-all-invoices")
    public ResponseObject getAllBookingTour(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(required = false) String searchTerm) {

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Page<Invoices> invoicesPage = invoicesService.findAllBySearchTerm(searchTerm, PageRequest.of(page, size, sort));

        Page<InvoicesDto> invoicesDtoPage = invoicesPage.map(invoice -> EntityDtoUtils.convertToDto(invoice, InvoicesDto.class));

        if (invoicesDtoPage.isEmpty()) {
            return new ResponseObject("404", "Không tìm thấy dữ liệu", null);
        } else {
            return new ResponseObject("200", "Đã tìm thấy dữ liệu", invoicesDtoPage);
        }
    }

}
