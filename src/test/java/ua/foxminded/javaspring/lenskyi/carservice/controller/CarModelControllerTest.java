package ua.foxminded.javaspring.lenskyi.carservice.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.junit.jupiter.Testcontainers;
import ua.foxminded.javaspring.lenskyi.carservice.controller.dto.CarBrandDto;
import ua.foxminded.javaspring.lenskyi.carservice.controller.dto.CarModelDto;
import ua.foxminded.javaspring.lenskyi.carservice.controller.dto.CarTypeDto;
import ua.foxminded.javaspring.lenskyi.carservice.controller.dto.mapper.CarModelDtoMapper;
import ua.foxminded.javaspring.lenskyi.carservice.repository.CarModelRepository;
import ua.foxminded.javaspring.lenskyi.carservice.service.CarBrandService;
import ua.foxminded.javaspring.lenskyi.carservice.service.CarTypeService;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
@ActiveProfiles("test")
@Transactional
class CarModelControllerTest {

    private static final String BRAND_ID_DOES_NOT_EXIST = "There is no CarBrand with id=";
    private static final String TYPE_ID_DOES_NOT_EXIST = "There is no CarType with id=";
    private static final String MODEL_ID_DOES_NOT_EXIST = "There is no CarModel with id=";
    private static final String MODEL_NAME_YEAR_BRAND_CONSTRAINT_VIOLATION_MESSAGE =
            "This CarModel name, year, Brand already exist";
    private final static int EXPECTED_NUM_MODELS = 9836;
    @Autowired
    private CarBrandService carBrandService;
    @Autowired
    private CarTypeService carTypeService;
    @Autowired
    private CarModelRepository carModelRepository;
    @Autowired
    private CarModelDtoMapper carModelDtoMapper;
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllCarModelTest() throws Exception {
        MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/api/v1/model/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        String content = result.getResponse().getContentAsString();
        List<CarModelDto> carModelDtoList = objectMapper.readValue(content, new TypeReference<>() {
        });
        assertEquals(EXPECTED_NUM_MODELS, carModelDtoList.size());
    }

    @Test
    void getAllCarModelTestPaginated() throws Exception {
        MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/api/v1/model/all?pageSize=5")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        String content = result.getResponse().getContentAsString();
        List<CarModelDto> carModelDtoList = objectMapper.readValue(content, new TypeReference<>() {
        });
        assertEquals(5, carModelDtoList.size());
    }

    @Test
    void getAllCarModelTestPaginatedWithName() throws Exception {
        MvcResult result = mvc.perform(MockMvcRequestBuilders.get(
                                "/api/v1/model/all?pageSize=5&modelName=Armada")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        String content = result.getResponse().getContentAsString();
        List<CarModelDto> carModelDtoList = objectMapper.readValue(content, new TypeReference<>() {
        });
        assertEquals(5, carModelDtoList.size());
        carModelDtoList.forEach(e -> {
            assertTrue(e.getName().equals("Armada"));
            assertEquals("Nissan", e.getCarBrandDto().getName());
        });
    }

    @Test
    void getAllCarModelTestPaginatedWithNameAndYear() throws Exception {
        MvcResult result = mvc.perform(MockMvcRequestBuilders.get(
                                "/api/v1/model/all?pageSize=5&modelName=Armada&year=2017")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        String content = result.getResponse().getContentAsString();
        List<CarModelDto> carModelDtoList = objectMapper.readValue(content, new TypeReference<>() {
        });
        assertEquals(1, carModelDtoList.size());
        carModelDtoList.forEach(e -> {
            assertTrue(e.getName().equals("Armada"));
            assertEquals("Nissan", e.getCarBrandDto().getName());
            assertEquals(2017, e.getYear());
        });
    }

    @Test
    void getAllCarModelTestPaginatedWithBrand() throws Exception {
        MvcResult result = mvc.perform(MockMvcRequestBuilders.get(
                                "/api/v1/model/all?pageSize=5&brandName=BMW")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        String content = result.getResponse().getContentAsString();
        List<CarModelDto> carModelDtoList = objectMapper.readValue(content, new TypeReference<>() {
        });
        assertEquals(5, carModelDtoList.size());
        carModelDtoList.forEach(e -> {
            assertEquals("BMW", e.getCarBrandDto().getName());
        });
    }

    @Test
    void getAllCarModelTestPaginatedWithType() throws Exception {
        CarTypeDto sedanDto = carTypeService.findByName("Sedan");
        MvcResult result = mvc.perform(MockMvcRequestBuilders.get(
                                "/api/v1/model/all?pageSize=5&typeName=Sedan")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        String content = result.getResponse().getContentAsString();
        List<CarModelDto> carModelDtoList = objectMapper.readValue(content, new TypeReference<>() {
        });
        assertEquals(5, carModelDtoList.size());
        carModelDtoList.forEach(e ->
                assertTrue(e.getCarTypeDtos().contains(sedanDto))
        );
    }

    @Test
    void getAllCarModelTestPaginatedWithAllFields() throws Exception {
        CarTypeDto suvDto = carTypeService.findByName("SUV");
        MvcResult result = mvc.perform(MockMvcRequestBuilders.get(
                                "/api/v1/model/all?pageSize=5&modelName=Armada&year=2005&brandName=nissan&typeName=SUV")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        String content = result.getResponse().getContentAsString();
        List<CarModelDto> carModelDtoList = objectMapper.readValue(content, new TypeReference<>() {
        });
        assertEquals(1, carModelDtoList.size());
        carModelDtoList.forEach(e -> {
            assertEquals("Armada", e.getName());
            assertEquals(2005, e.getYear());
            assertEquals("Nissan", e.getCarBrandDto().getName());
            assertTrue(e.getCarTypeDtos().contains(suvDto));
        });
    }

    @Test
    void createCarModelTest() throws Exception {
        CarModelDto carModelDto = new CarModelDto();
        CarTypeDto suvDto = carTypeService.findByName("SUV");
        CarBrandDto nissanDto = carBrandService.findByName("Nissan");
        carModelDto.setName("testName");
        carModelDto.setYear(986);
        carModelDto.setCarBrandDto(nissanDto);
        carModelDto.setCarTypeDtos(Set.of(suvDto));
        MvcResult result = mvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/model")
                        .content(objectMapper.writeValueAsString(carModelDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();
        String content = result.getResponse().getContentAsString();
        CarModelDto createdCarModelDto = objectMapper.readValue(content, new TypeReference<>() {
        });
        assertEquals("testName", createdCarModelDto.getName());
        assertEquals(986, createdCarModelDto.getYear());
        assertEquals("Nissan", createdCarModelDto.getCarBrandDto().getName());
        assertTrue(createdCarModelDto.getCarTypeDtos().contains(suvDto));
    }

    @Test
    void createCarModelWrongBrandIdTest() throws Exception {
        CarModelDto carModelDto = new CarModelDto();
        CarTypeDto suvDto = carTypeService.findByName("SUV");
        CarBrandDto wrongBrandDto = new CarBrandDto();
        wrongBrandDto.setId(-1L);
        wrongBrandDto.setName("wrongName");
        carModelDto.setName("testName");
        carModelDto.setYear(986);
        carModelDto.setCarBrandDto(wrongBrandDto);
        carModelDto.setCarTypeDtos(Set.of(suvDto));
        MvcResult result = mvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/model")
                        .content(objectMapper.writeValueAsString(carModelDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andReturn();
        String content = result.getResponse().getContentAsString();
        assertTrue(content.contains(BRAND_ID_DOES_NOT_EXIST));
    }

    @Test
    void createCarModelWrongCarTypeTest() throws Exception {
        CarModelDto carModelDto = new CarModelDto();
        CarTypeDto wrongCarType = new CarTypeDto();
        wrongCarType.setId(-1L);
        wrongCarType.setName("anotherWrongName");
        CarBrandDto nissanDto = carBrandService.findByName("Nissan");
        carModelDto.setName("testName");
        carModelDto.setYear(986);
        carModelDto.setCarBrandDto(nissanDto);
        carModelDto.setCarTypeDtos(Set.of(wrongCarType));
        MvcResult result = mvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/model")
                        .content(objectMapper.writeValueAsString(carModelDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andReturn();
        String content = result.getResponse().getContentAsString();
        assertTrue(content.contains(TYPE_ID_DOES_NOT_EXIST));
    }

    @Test
    void createCarModelConstraintViolationTest() throws Exception {
        CarModelDto carModelDto = new CarModelDto();
        CarTypeDto suvDto = carTypeService.findByName("SUV");
        CarBrandDto nissanDto = carBrandService.findByName("Nissan");
        carModelDto.setName("Armada");
        carModelDto.setYear(2005);
        carModelDto.setCarBrandDto(nissanDto);
        carModelDto.setCarTypeDtos(Set.of(suvDto));
        MvcResult result = mvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/model")
                        .content(objectMapper.writeValueAsString(carModelDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andReturn();
        String content = result.getResponse().getContentAsString();
        assertTrue(content.contains(MODEL_NAME_YEAR_BRAND_CONSTRAINT_VIOLATION_MESSAGE));
    }

    @Test
    void updateAllFieldsCarModelTest() throws Exception {
        CarModelDto carModelDto = carModelDtoMapper.carModelEntityToCarModelDto(
                carModelRepository.findAll().get(100)
        );
        CarTypeDto suvDto = carTypeService.findByName("SUV");
        CarBrandDto nissanDto = carBrandService.findByName("Nissan");
        carModelDto.setName("updatedName");
        carModelDto.setYear(999);
        carModelDto.setCarBrandDto(nissanDto);
        carModelDto.setCarTypeDtos(Set.of(suvDto));
        MvcResult result = mvc.perform(MockMvcRequestBuilders
                        .put("/api/v1/model")
                        .content(objectMapper.writeValueAsString(carModelDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();
        String content = result.getResponse().getContentAsString();
        CarModelDto updatedCarModelDto = objectMapper.readValue(content, new TypeReference<>() {
        });
        assertEquals("updatedName", updatedCarModelDto.getName());
        assertEquals(999, updatedCarModelDto.getYear());
        assertEquals(nissanDto, updatedCarModelDto.getCarBrandDto());
        assertEquals(Set.of(suvDto), updatedCarModelDto.getCarTypeDtos());
    }

    @Test
    void updateOneFieldCarModelTest() throws Exception {
        CarModelDto carModelDto = carModelDtoMapper.carModelEntityToCarModelDto(
                carModelRepository.findAll().get(100)
        );
        CarTypeDto suvDto = carTypeService.findByName("SUV");
        carModelDto.setCarTypeDtos(Set.of(suvDto));
        MvcResult result = mvc.perform(MockMvcRequestBuilders
                        .put("/api/v1/model")
                        .content(objectMapper.writeValueAsString(carModelDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();
        String content = result.getResponse().getContentAsString();
        CarModelDto updatedCarModelDto = objectMapper.readValue(content, new TypeReference<>() {
        });
        assertEquals(carModelDto.getName(), updatedCarModelDto.getName());
        assertEquals(carModelDto.getYear(), updatedCarModelDto.getYear());
        assertEquals(carModelDto.getCarBrandDto(), updatedCarModelDto.getCarBrandDto());
        assertEquals(Set.of(suvDto), updatedCarModelDto.getCarTypeDtos());
    }

    @Test
    void updateCarModelWrongIdTest() throws Exception {
        CarModelDto carModelDto = carModelDtoMapper.carModelEntityToCarModelDto(
                carModelRepository.findAll().get(100)
        );
        carModelDto.setId("wrongId");
        CarTypeDto suvDto = carTypeService.findByName("SUV");
        carModelDto.setCarTypeDtos(Set.of(suvDto));
        MvcResult result = mvc.perform(MockMvcRequestBuilders
                        .put("/api/v1/model")
                        .content(objectMapper.writeValueAsString(carModelDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andReturn();
        String content = result.getResponse().getContentAsString();
        assertTrue(content.contains(MODEL_ID_DOES_NOT_EXIST));
    }

    @Test
    void updateCarModelWrongCarTypeIdTest() throws Exception {
        CarModelDto carModelDto = carModelDtoMapper.carModelEntityToCarModelDto(
                carModelRepository.findAll().get(100)
        );
        CarTypeDto wrongTypeDto = new CarTypeDto();
        wrongTypeDto.setId(-1L);
        wrongTypeDto.setName("name");
        carModelDto.setCarTypeDtos(Set.of(wrongTypeDto));
        MvcResult result = mvc.perform(MockMvcRequestBuilders
                        .put("/api/v1/model")
                        .content(objectMapper.writeValueAsString(carModelDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andReturn();
        String content = result.getResponse().getContentAsString();
        assertTrue(content.contains(TYPE_ID_DOES_NOT_EXIST));
    }

    @Test
    void updateCarModelWrongBrandIdTest() throws Exception {
        CarModelDto carModelDto = carModelDtoMapper.carModelEntityToCarModelDto(
                carModelRepository.findAll().get(100)
        );
        CarBrandDto wrongCarBrandDto = new CarBrandDto();
        wrongCarBrandDto.setId(-1L);
        wrongCarBrandDto.setName("name");
        carModelDto.setCarBrandDto(wrongCarBrandDto);
        MvcResult result = mvc.perform(MockMvcRequestBuilders
                        .put("/api/v1/model")
                        .content(objectMapper.writeValueAsString(carModelDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andReturn();
        String content = result.getResponse().getContentAsString();
        assertTrue(content.contains(BRAND_ID_DOES_NOT_EXIST));
    }

    @Test
    void deleteCarModelTest() throws Exception {
        String carModelId = carModelRepository.findAll().get(200).getName();
        mvc.perform(MockMvcRequestBuilders
                        .delete("/api/v1/model/" + carModelId))
                .andExpect(status().isOk());
        assertFalse(carModelRepository.existsById(carModelId));
    }
}