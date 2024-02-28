package com.main.traveltour.data;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import com.main.traveltour.entity.ResponseObject;
import com.main.traveltour.entity.Tours;
import com.main.traveltour.service.staff.ToursService;
import com.main.traveltour.utils.GenerateNextID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.InputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api/v1/add-data")
public class tourDetailData {
    @Autowired
    private ToursService toursService;

    @Autowired
    private ResourceLoader resourceLoader;

    @GetMapping("tour-detail")
    public ResponseObject generateTours() {
        // http://localhost:8080/api/v1/add-data/tour-detail
        int numberOfTours = 50;
        List<Tours> toursList = new ArrayList<>();
        Faker faker = new Faker();

        try {
            List<String> provinceNames = new ArrayList<>();

            Resource resource = resourceLoader.getResource("classpath:static/lib/address/data.json");

            InputStream inputStream = resource.getInputStream();

            ObjectMapper objectMapper = new ObjectMapper();

            List<Map<String, Object>> provinceDataList = objectMapper.readValue(
                    inputStream,
                    new TypeReference<List<Map<String, Object>>>() {
                    }
            );

            for (Map<String, Object> provinceData : provinceDataList) {
                String provinceName = (String) provinceData.get("Name");
                provinceNames.add(provinceName);

                // If the JSON structure contains districts, you can extract district names here as well
                List<Map<String, Object>> districts = (List<Map<String, Object>>) provinceData.get("Districts");
                for (Map<String, Object> districtData : districts) {
                    String districtName = (String) districtData.get("Name");
                }
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

//    @GetMapping("order")
//    @ResponseBody
//    public ResponseObject generateOrders() {
//        int numberOfOrders = 9500;
//        List<Orders> ordersList = new ArrayList<>();
//        Faker faker = new Faker();
//
//        LocalDate startDate = LocalDate.of(2018, 1, 1);
//        long daysBetween = ChronoUnit.DAYS.between(startDate, LocalDate.now());
//
//        List<Integer> users = userService.findAllUser().stream()
//                .map(Users::getId)
//                .toList();
//
//        for (int i = 0; i < numberOfOrders; i++) {
//            Orders order = new Orders();
//
//            // Tạo ID đơn hàng
//            order.setId("DH" + faker.number().digits(14));
//
//            Integer randomUserId = getRandomIdInt(users);
//            order.setUserId(randomUserId);
//
//            order.setPaymentType(faker.bool().bool());
//            order.setPaymentStatus(faker.number().numberBetween(0, 2));
//            String[] statuses = {"Đã giao hàng", "Đã huỷ đơn", "Chờ xác nhận"};
//            order.setOrderStatus(faker.options().option(statuses));
//            order.setOrderShipCost(BigDecimal.valueOf(faker.number().randomDouble(2, 10000, 400000)));
//            order.setToName(faker.name().fullName());
//            order.setToPhone(faker.phoneNumber().cellPhone());
//            order.setToProvince(faker.address().state());
//            order.setToDistrict(faker.address().cityName());
//            order.setToWard(faker.address().secondaryAddress());
//            order.setToAddress(faker.address().fullAddress());
//            order.setOrderNote(faker.lorem().sentence());
//
//            // Tạo ngày ngẫu nhiên
//            Date randomDate = faker.date().past((int) daysBetween, TimeUnit.DAYS);
//            order.setDateCreated(new Timestamp(randomDate.getTime()));
//
//            orderService.saveBookingTour(order);
//            ordersList.add(order);
//        }
//        return new ResponseObject("200", "Đã thêm dữ liệu Order thành công", ordersList);
//    }
//
//
//    @GetMapping("items-order")
//    @ResponseBody
//    public ResponseObject generateOrderItems() {
//        int numberOfItems = 9500; // Số lượng items cần tạo
//        List<OrderItems> orderItemList = new ArrayList<>();
//        Faker faker = new Faker();
//
//        List<String> orderIds = orderService.findAll().stream()
//                .map(Orders::getId)
//                .toList();
//        List<String> products = productService.findAll().stream()
//                .map(Products::getId)
//                .toList();
//
//        for (int i = 0; i < numberOfItems; i++) {
//            OrderItems item = new OrderItems();
//
//            // Liên kết mỗi item với một order ngẫu nhiên từ danh sách orderIds
//            String randomOrderId = getRandomId(orderIds);
//            item.setOrderId(randomOrderId);
//
//            // Mã sản phẩm ngẫu nhiên từ danh sách products
//            String randomProductId = getRandomId(products);
//            item.setProductId(randomProductId);
//
//            // Số lượng sản phẩm (từ 1 đến 10)
//            item.setQuantity(faker.number().numberBetween(1, 10));
//
//            // Giá của sản phẩm (giả sử từ 10,000 đến 1,000,000)
//            item.setPrice(BigDecimal.valueOf(faker.number().randomDouble(2, 10000, 1000000)));
//
//            orderItemService.saveBookingTour(item);
//            orderItemList.add(item);
//        }
//
//        return new ResponseObject("200", "Đã thêm dữ liệu OrderItem thành công", orderItemList);
//    }
//
//    private String getRandomId(List<String> idList) {
//        Random random = new Random();
//        int randomIndex = random.nextInt(idList.size());
//        return idList.get(randomIndex);
//    }
//
//    private Integer getRandomIdInt(List<Integer> idList) {
//        Random random = new Random();
//        int randomIndex = random.nextInt(idList.size());
//        return idList.get(randomIndex);
//    }
}
