package com.main.traveltour.restcontroller.agent;

import com.main.traveltour.dto.agent.TransportationBrandsDto;
import com.main.traveltour.entity.TransportationBrands;
import com.main.traveltour.service.agent.TransportationBrandsService;
import com.main.traveltour.service.utils.FileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api/v1")
public class TransportBrandAPI {

    @Autowired
    private FileUpload fileUpload;

    @Autowired
    private TransportationBrandsService transportationBrandsService;

    @GetMapping("/agent/transport-brand/find-all-transport-brand")
    private ResponseEntity<Page<TransportationBrands>> findAllTransportBrand(@RequestParam(defaultValue = "0") int page,
                                                                             @RequestParam(defaultValue = "10") int size,
                                                                             @RequestParam(defaultValue = "id") String sortBy,
                                                                             @RequestParam(defaultValue = "asc") String sortDir,
                                                                             @RequestParam(required = false) String searchTerm) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Page<TransportationBrands> transportationBrands = searchTerm == null || searchTerm.isEmpty()
                ? transportationBrandsService.findAllTransportBrand(PageRequest.of(page, size, sort))
                : transportationBrandsService.findAllTransportBrandWithSearch(searchTerm, PageRequest.of(page, size, sort));
        return new ResponseEntity<>(transportationBrands, HttpStatus.OK);
    }

    @GetMapping("/agent/transport-brand/find-by-agency-id/{agencyId}")
    private TransportationBrands findByUserId(@PathVariable int agencyId) {
        return transportationBrandsService.findByAgencyId(agencyId);
    }

    @PostMapping("/agent/transport-brand/register-transport")
    private void registerTransport(@RequestPart("transportDto") TransportationBrandsDto transportDto, @RequestPart("transportImg") MultipartFile transportImg) throws IOException {
        int agencyId = transportDto.getId();
        String transportImgUrl = fileUpload.uploadFile(transportImg);

        TransportationBrands transport = transportationBrandsService.findByAgencyId(agencyId);
        transport.setTransportationBrandName(transportDto.getTransportationBrandName());
        transport.setTransportationBrandDescription(transportDto.getTransportationBrandDescription());
        transport.setTransportationBrandImg(transportImgUrl);
        transport.setIsAccepted(Boolean.TRUE);
        transportationBrandsService.save(transport);
    }
}
