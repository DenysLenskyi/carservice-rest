package ua.foxminded.javaspring.lenskyi.carservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.foxminded.javaspring.lenskyi.carservice.model.CarModel;
import ua.foxminded.javaspring.lenskyi.carservice.model.dto.CarModelDto;
import ua.foxminded.javaspring.lenskyi.carservice.service.CarModelService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/model")
public class CarModelController {

        private CarModelService carModelService;

        public CarModelController(CarModelService carModelService) {
                this.carModelService = carModelService;
        }

        @Operation(summary = "Search CarModels")
        @ApiResponses(value = {
                @ApiResponse(responseCode = "200", description = "List of CarModels by given params",
                        content = {@Content(mediaType = "application/json",
                                schema = @Schema(implementation = CarModel.class))})})
        @GetMapping("/search")
        public List<CarModelDto> findAll(@RequestParam(defaultValue = "0") int pageNumber,
                                         @RequestParam(defaultValue = "0") int pageSize,
                                         @RequestParam(defaultValue = "id") String sort,
                                         @RequestParam(required = false) String modelName,
                                         @RequestParam(required = false) Integer year,
                                         @RequestParam(required = false) String brandName,
                                         @RequestParam(required = false) String typeName) {
                return carModelService.findAll(pageNumber, pageSize, sort, modelName, year, brandName, typeName);
        }

        @Operation(summary = "Create CarModel",
                security = @SecurityRequirement(name = "bearerAuth"))
        @ApiResponses(value = {
                @ApiResponse(responseCode = "200", description = "CarModel created",
                        content = {@Content(mediaType = "application/json",
                                schema = @Schema(implementation = CarModel.class))}),
                @ApiResponse(responseCode = "400", description = "Invalid data supplied",
                        content = @Content)})
        @PostMapping
        public ResponseEntity<CarModelDto> createCarModel(@RequestBody @Valid CarModelDto carModelDto) {
                return ResponseEntity
                        .status(HttpStatus.CREATED)
                        .body(carModelService.createCarModel(carModelDto));
        }

        @Operation(summary = "Delete CarModel by its id",
                security = @SecurityRequirement(name = "bearerAuth"))
        @ApiResponses(value = {
                @ApiResponse(responseCode = "200", description = "CarModel deleted",
                        content = {@Content(mediaType = "application/json",
                                schema = @Schema(implementation = CarModel.class))})})
        @DeleteMapping("/{id}")
        public void deleteCarModel(@PathVariable("id") String id) {
                carModelService.deleteCarModel(id);
        }

        @Operation(summary = "Update CarModel",
                security = @SecurityRequirement(name = "bearerAuth"))
        @ApiResponses(value = {
                @ApiResponse(responseCode = "200", description = "CarModel updated",
                        content = {@Content(mediaType = "application/json",
                                schema = @Schema(implementation = CarModel.class))}),
                @ApiResponse(responseCode = "400", description = "Invalid id supplied",
                        content = @Content)})
        @PutMapping
        public ResponseEntity<CarModelDto> updateCarModel(@RequestBody CarModelDto carModelDto) {
                return ResponseEntity
                        .status(HttpStatus.OK)
                        .body(carModelService.updateCarModel(carModelDto));
        }
}