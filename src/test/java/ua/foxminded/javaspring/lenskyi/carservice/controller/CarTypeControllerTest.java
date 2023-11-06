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
import ua.foxminded.javaspring.lenskyi.carservice.controller.dto.CarTypeDto;
import ua.foxminded.javaspring.lenskyi.carservice.service.CarTypeService;

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
class CarTypeControllerTest {

    private static final String ID_DOES_NOT_EXIST_ERROR_MESSAGE = "There is no CarType with id=";
    private static final String NAME_DOES_NOT_EXIST_ERROR_MESSAGE = "There is no CarType with name=";
    private static final String ERROR_NOT_UNIQUE_CAR_TYPE_NAME = "Error. Not unique CarType name=";
    private final static int EXPECTED_NUM_TYPES = 10;
    @Autowired
    private CarTypeService carTypeService;
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllCarTypeTest() throws Exception {
        MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/api/v1/type/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        String content = result.getResponse().getContentAsString();
        List<CarTypeDto> carTypeDtoList = objectMapper.readValue(content, new TypeReference<>() {
        });
        assertEquals(EXPECTED_NUM_TYPES, carTypeDtoList.size());
    }

    @Test
    void getAllCarTypePaginatedTest() throws Exception {
        MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/api/v1/type/all?pageNumber=0&pageSize=3")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        String content = result.getResponse().getContentAsString();
        List<CarTypeDto> carTypeDtoList = objectMapper.readValue(content, new TypeReference<>() {
        });
        assertEquals(3, carTypeDtoList.size());
    }

    @Test
    void getCarTypeById() throws Exception {
        CarTypeDto carTypeDto = carTypeService.findAll().get(0);
        mvc.perform(MockMvcRequestBuilders.get("/api/v1/type/" + carTypeDto.getId()))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").value(carTypeDto.getId()))
                .andExpect(jsonPath("$.name").value(carTypeDto.getName()));
    }

    @Test
    void getCarTypeByIdDoesNotExist() throws Exception {
        long wrongId = carTypeService.findAll().get(0).getId() - 500L;
        MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/api/v1/type/" + wrongId))
                .andExpect(status().isBadRequest())
                .andReturn();
        String content = result.getResponse().getContentAsString();
        assertTrue(content.contains(ID_DOES_NOT_EXIST_ERROR_MESSAGE));
    }

    @Test
    void getCarTypeByName() throws Exception {
        CarTypeDto carTypeDto = carTypeService.findAll().get(0);
        mvc.perform(MockMvcRequestBuilders.get("/api/v1/type/by-name/" + carTypeDto.getName()))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").value(carTypeDto.getId()))
                .andExpect(jsonPath("$.name").value(carTypeDto.getName()));
    }

    @Test
    void getCarTypeByNameDoesNotExist() throws Exception {
        final String wrongName = "doesntexist";
        MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/api/v1/type/by-name/" + wrongName))
                .andExpect(status().isBadRequest())
                .andReturn();
        String content = result.getResponse().getContentAsString();
        assertTrue(content.contains(NAME_DOES_NOT_EXIST_ERROR_MESSAGE));
    }

    @Test
    void createCarTypeTest() throws Exception {
        CarTypeDto carTypeDto = new CarTypeDto();
        carTypeDto.setName("testCarTypeName");
        mvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/type")
                        .content(objectMapper.writeValueAsString(carTypeDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        CarTypeDto createdCarTypeDto = carTypeService.findByName(carTypeDto.getName());
        assertEquals(carTypeDto.getName(), createdCarTypeDto.getName());
    }

    @Test
    void createCarTypeNotUniqueNameTest() throws Exception {
        CarTypeDto carTypeDto = new CarTypeDto();
        carTypeDto.setName(carTypeService.findAll().get(0).getName());
        MvcResult result = mvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/type")
                        .content(objectMapper.writeValueAsString(carTypeDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
        String content = result.getResponse().getContentAsString();
        assertTrue(content.contains(ERROR_NOT_UNIQUE_CAR_TYPE_NAME));
    }

    @Test
    void updateCarTypeTest() throws Exception {
        CarTypeDto carTypeDto = carTypeService.findAll().get(0);
        final String updatedTestName = "updatedTestName";
        carTypeDto.setName(updatedTestName);
        mvc.perform(MockMvcRequestBuilders
                        .put("/api/v1/type")
                        .content(objectMapper.writeValueAsString(carTypeDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        CarTypeDto updatedCarTypeDto = carTypeService.findByName(updatedTestName);
        assertEquals(carTypeDto, updatedCarTypeDto);
    }

    @Test
    void updateCarTypeNotUniqueNameTest() throws Exception {
        CarTypeDto carTypeDto = carTypeService.findAll().get(0);
        carTypeDto.setName(carTypeService.findAll().get(1).getName());
        MvcResult result = mvc.perform(MockMvcRequestBuilders
                        .put("/api/v1/type")
                        .content(objectMapper.writeValueAsString(carTypeDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
        String content = result.getResponse().getContentAsString();
        assertTrue(content.contains(ERROR_NOT_UNIQUE_CAR_TYPE_NAME));
    }

    @Test
    void updateCarTypeIdDoesNotExistTest() throws Exception {
        CarTypeDto carTypeDto = carTypeService.findAll().get(0);
        carTypeDto.setId(carTypeDto.getId() - 500L);
        carTypeDto.setName("someName");
        MvcResult result = mvc.perform(MockMvcRequestBuilders
                        .put("/api/v1/type")
                        .content(objectMapper.writeValueAsString(carTypeDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
        String content = result.getResponse().getContentAsString();
        assertTrue(content.contains(ID_DOES_NOT_EXIST_ERROR_MESSAGE));
    }

    @Test
    void deleteCarTypeTest() throws Exception {
        List<CarTypeDto> carTypeDtoListBeforeRemoval = carTypeService.findAll();
        CarTypeDto carTypeDto = carTypeDtoListBeforeRemoval.get(0);
        mvc.perform(MockMvcRequestBuilders
                        .delete("/api/v1/type/" + carTypeDto.getId()))
                .andExpect(status().isOk());
        List<CarTypeDto> carTypeDtoListAfterRemoval = carTypeService.findAll();
        assertEquals(carTypeDtoListBeforeRemoval.size() - 1, carTypeDtoListAfterRemoval.size());
    }

    @Test
    void deleteCarTypeIdDoesNotExistTest() throws Exception {
        CarTypeDto carTypeDto = carTypeService.findAll().get(0);
        long wrongId = carTypeDto.getId() - 500L;
        MvcResult result = mvc.perform(MockMvcRequestBuilders
                        .delete("/api/v1/type/" + wrongId))
                .andExpect(status().isBadRequest())
                .andReturn();
        String content = result.getResponse().getContentAsString();
        assertTrue(content.contains(ID_DOES_NOT_EXIST_ERROR_MESSAGE));
    }
}