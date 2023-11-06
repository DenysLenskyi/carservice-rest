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
import ua.foxminded.javaspring.lenskyi.carservice.service.CarBrandService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
@ActiveProfiles("test")
@Transactional
class CarBrandControllerTest {

    private static final String ID_DOES_NOT_EXIST_ERROR_MESSAGE = "There is no CarBrand with id=";
    private static final String NAME_DOES_NOT_EXIST_ERROR_MESSAGE = "There is no CarBrand with name=";
    private static final String NOT_UNIQUE_BRAND_NAME_ERROR_MESSAGE = "Error. Not unique CarBrand name=";
    private final static int EXPECTED_NUM_BRANDS = 64;
    @Autowired
    private CarBrandService carBrandService;
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllCarBrandTest() throws Exception {
        MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/api/v1/brand/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        String content = result.getResponse().getContentAsString();
        List<CarBrandDto> carBrandDtoList = objectMapper.readValue(content, new TypeReference<>() {
        });
        assertEquals(EXPECTED_NUM_BRANDS, carBrandDtoList.size());
    }

    @Test
    void getCarBrandById() throws Exception {
        CarBrandDto carBrandDto = carBrandService.findAll().get(0);
        mvc.perform(MockMvcRequestBuilders.get("/api/v1/brand/" + carBrandDto.getId()))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").value(carBrandDto.getId()))
                .andExpect(jsonPath("$.name").value(carBrandDto.getName()));
    }

    @Test
    void getCarBrandByIdDoesNotExist() throws Exception {
        long wrongId = carBrandService.findAll().get(0).getId() - 500L;
        MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/api/v1/brand/" + wrongId))
                .andExpect(status().isBadRequest())
                .andReturn();
        String content = result.getResponse().getContentAsString();
        assertTrue(content.contains(ID_DOES_NOT_EXIST_ERROR_MESSAGE));
    }

    @Test
    void getCarBrandByName() throws Exception {
        CarBrandDto carBrandDto = carBrandService.findAll().get(0);
        mvc.perform(MockMvcRequestBuilders.get("/api/v1/brand/by-name/" + carBrandDto.getName()))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").value(carBrandDto.getId()))
                .andExpect(jsonPath("$.name").value(carBrandDto.getName()));
    }

    @Test
    void getCarBrandByNameDoesNotExist() throws Exception {
        final String wrongName = "doesntexist";
        MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/api/v1/brand/by-name/" + wrongName))
                .andExpect(status().isBadRequest())
                .andReturn();
        String content = result.getResponse().getContentAsString();
        assertTrue(content.contains(NAME_DOES_NOT_EXIST_ERROR_MESSAGE));
    }

    @Test
    void createCarBrandTest() throws Exception {
        CarBrandDto carBrandDto = new CarBrandDto();
        carBrandDto.setName("testCarBrandName");
        mvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/brand")
                        .content(objectMapper.writeValueAsString(carBrandDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        CarBrandDto createdCarBrandDto = carBrandService.findByName(carBrandDto.getName());
        assertEquals(carBrandDto.getName(), createdCarBrandDto.getName());
    }

    @Test
    void createCarBrandNotUniqueNameTest() throws Exception {
        CarBrandDto carBrandDto = new CarBrandDto();
        carBrandDto.setName(carBrandService.findAll().get(0).getName());
        MvcResult result = mvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/brand")
                        .content(objectMapper.writeValueAsString(carBrandDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
        String content = result.getResponse().getContentAsString();
        assertTrue(content.contains(NOT_UNIQUE_BRAND_NAME_ERROR_MESSAGE));
    }

    @Test
    void updateCarBrandTest() throws Exception {
        CarBrandDto carBrandDto = carBrandService.findAll().get(0);
        final String updatedCarBrandName = "updatedTestName";
        carBrandDto.setName(updatedCarBrandName);
        mvc.perform(MockMvcRequestBuilders
                        .put("/api/v1/brand")
                        .content(objectMapper.writeValueAsString(carBrandDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        CarBrandDto updatedCarBrandDto = carBrandService.findByName(updatedCarBrandName);
        assertEquals(carBrandDto, updatedCarBrandDto);
    }

    @Test
    void updateCarBrandNotUniqueNameTest() throws Exception {
        CarBrandDto carBrandDto = carBrandService.findAll().get(0);
        carBrandDto.setName(carBrandService.findAll().get(1).getName());
        MvcResult result = mvc.perform(MockMvcRequestBuilders
                        .put("/api/v1/brand")
                        .content(objectMapper.writeValueAsString(carBrandDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
        String content = result.getResponse().getContentAsString();
        assertTrue(content.contains(NOT_UNIQUE_BRAND_NAME_ERROR_MESSAGE));
    }

    @Test
    void updateCarBrandIdDoesNotExistTest() throws Exception {
        CarBrandDto carBrandDto = carBrandService.findAll().get(0);
        carBrandDto.setId(carBrandDto.getId() - 500L);
        carBrandDto.setName("someName");
        MvcResult result = mvc.perform(MockMvcRequestBuilders
                        .put("/api/v1/brand")
                        .content(objectMapper.writeValueAsString(carBrandDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
        String content = result.getResponse().getContentAsString();
        assertTrue(content.contains(ID_DOES_NOT_EXIST_ERROR_MESSAGE));
    }

    @Test
    void deleteCarBrandTest() throws Exception {
        List<CarBrandDto> carBrandDtoListBeforeRemoval = carBrandService.findAll();
        CarBrandDto carBrandDto = carBrandDtoListBeforeRemoval.get(0);
        mvc.perform(MockMvcRequestBuilders
                        .delete("/api/v1/brand/" + carBrandDto.getId()))
                .andExpect(status().isOk());
        List<CarBrandDto> carBrandDtoListAfterRemoval = carBrandService.findAll();
        assertEquals(carBrandDtoListBeforeRemoval.size() - 1, carBrandDtoListAfterRemoval.size());
    }

    @Test
    void deleteCarBrandIdDoesNotExistTest() throws Exception {
        CarBrandDto carBrandDto = carBrandService.findAll().get(0);
        long wrongId = carBrandDto.getId() - 500L;
        MvcResult result = mvc.perform(MockMvcRequestBuilders
                        .delete("/api/v1/brand/" + wrongId))
                .andExpect(status().isBadRequest())
                .andReturn();
        String content = result.getResponse().getContentAsString();
        assertTrue(content.contains(ID_DOES_NOT_EXIST_ERROR_MESSAGE));
    }
}