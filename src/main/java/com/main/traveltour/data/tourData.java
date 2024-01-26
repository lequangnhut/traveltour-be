package com.main.traveltour.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.github.javafaker.Faker;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import com.main.traveltour.entity.ResponseObject;
import com.main.traveltour.entity.Tours;
import com.main.traveltour.service.staff.ToursService;
import com.main.traveltour.utils.GenerateNextID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.io.InputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api/v1/add-data")
public class tourData {
    @Autowired
    private ToursService toursService;

    @Autowired
    private ResourceLoader resourceLoader;

    @GetMapping("tour-basic")
    public ResponseObject generateTours() {
        // http://localhost:8080/api/v1/add-data/tour-basic
        int numberOfTours = 50;
        List<Tours> toursList = new ArrayList<>();
        Faker faker = new Faker();

        try {
            List<String> provinceNames = new ArrayList<>();

            Resource resource = resourceLoader.getResource("classpath:static/lib/address/data.json");

            InputStream inputStream = resource.getInputStream();

            ObjectMapper objectMapper = new ObjectMapper();

            List<Map<String, Object>> provinceDataList = objectMapper.readValue(
                    inputStream, new TypeReference<>() {
                    }
            );

            for (Map<String, Object> provinceData : provinceDataList) {
                String provinceName = (String) provinceData.get("Name");
                provinceNames.add(provinceName);
            }

            for (int i = 0; i < numberOfTours; i++) {
                Tours tours = new Tours();
                String tourId = GenerateNextID.generateNextCode("TR", toursService.maxCodeTourId());
                tours.setId(tourId);
                tours.setTourTypeId(faker.number().numberBetween(1, 5));
                String randomProvinceCityName1 = getRandomProvinceCityName(provinceNames);
                String randomProvinceCityName2;
                do {
                    randomProvinceCityName2 = getRandomProvinceCityName(provinceNames);
                } while (randomProvinceCityName2.equals(randomProvinceCityName1));
                String tourName = randomProvinceCityName1 + " - " + randomProvinceCityName2;
                tours.setTourName(tourName);
                tours.setTourDescription(faker.lorem().paragraph());
                tours.setTourImg("http://res.cloudinary.com/daawxyvyj/image/upload/v1706263274/362d198e-bb4c-460d-9f08-b70cedd5d7f1.jpg");
                tours.setDateCreated(new Timestamp(System.currentTimeMillis()));
                tours.setIsActive(true);

                toursList.add(tours);
                toursService.save(tours);
            }

            return new ResponseObject("200", "Đã thêm dữ liệu tour thành công", toursList);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseObject("404", "Đã thêm dữ liệu Product thất bại", toursList);
        }
    }

    private String getRandomProvinceCityName(List<String> provinceNames) {
        Random random = new Random();
        int randomIndex = random.nextInt(provinceNames.size());
        return provinceNames.get(randomIndex);
    }
}
