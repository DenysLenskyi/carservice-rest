package ua.foxminded.javaspring.lenskyi.carservice.util.swagger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.foxminded.javaspring.lenskyi.carservice.model.dto.CarModelDto;

import java.util.List;

public interface CarModelOpenApi {

        @Operation(summary = "Search CarModels")
        @ApiResponses(value = {
                @ApiResponse(responseCode = "200", description = "List of CarModels by given params",
                        content = {@Content(mediaType = "application/json",
                                schema = @Schema(implementation = CarModelDto.class))})})
        @GetMapping("/search")
        List<CarModelDto> findAll(@RequestParam(defaultValue = "0") int pageNumber,
                                  @RequestParam(defaultValue = "0") int pageSize,
                                  @RequestParam(defaultValue = "id") String sort,
                                  @RequestParam(required = false) String modelName,
                                  @RequestParam(required = false) Integer year,
                                  @RequestParam(required = false) String brandName,
                                  @RequestParam(required = false) String typeName);

        @Operation(summary = "Create CarModel",
                security = @SecurityRequirement(name = "bearerAuth"))
        @ApiResponses(value = {
                @ApiResponse(responseCode = "200", description = "CarModel created",
                        content = {@Content(mediaType = "application/json",
                                schema = @Schema(implementation = CarModelDto.class))}),
                @ApiResponse(responseCode = "400", description = "Invalid data supplied",
                        content = @Content),
                @ApiResponse(responseCode = "401", description = "Access denied. Unauthorized user can perform only GET requests.",
                        content = @Content)})
        ResponseEntity<CarModelDto> createCarModel(@RequestBody @Valid CarModelDto carModelDto);

        @Operation(summary = "Delete CarModel by its id",
                security = @SecurityRequirement(name = "bearerAuth"))
        @ApiResponses(value = {
                @ApiResponse(responseCode = "200", description = "CarModel deleted",
                        content = {@Content(mediaType = "application/json",
                                schema = @Schema(implementation = CarModelDto.class))}),
                @ApiResponse(responseCode = "401", description = "Access denied. Unauthorized user can perform only GET requests.",
                        content = @Content)})
        void deleteCarModel(@PathVariable("id") String id);

        @Operation(summary = "Update CarModel",
                security = @SecurityRequirement(name = "bearerAuth"))
        @ApiResponses(value = {
                @ApiResponse(responseCode = "200", description = "CarModel updated",
                        content = {@Content(mediaType = "application/json",
                                schema = @Schema(implementation = CarModelDto.class))}),
                @ApiResponse(responseCode = "400", description = "Invalid id supplied",
                        content = @Content),
                @ApiResponse(responseCode = "401", description = "Access denied. Unauthorized user can perform only GET requests.",
                        content = @Content)})
        ResponseEntity<CarModelDto> updateCarModel(@RequestBody CarModelDto carModelDto);
}
