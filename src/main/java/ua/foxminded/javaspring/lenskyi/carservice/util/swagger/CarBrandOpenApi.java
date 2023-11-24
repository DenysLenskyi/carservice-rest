package ua.foxminded.javaspring.lenskyi.carservice.util.swagger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import ua.foxminded.javaspring.lenskyi.carservice.model.dto.CarBrandDto;

import java.util.List;

public interface CarBrandOpenApi {

        @Operation(summary = "Get the list of CarBrand, paginated, sorted")
        @ApiResponses(value = {
                @ApiResponse(responseCode = "200", description = "Listed with given parameters",
                        content = {@Content(mediaType = "application/json",
                                schema = @Schema(implementation = CarBrandDto.class))})})
        List<CarBrandDto> findAll(@RequestParam(defaultValue = "0") int pageNumber,
                                  @RequestParam(defaultValue = "0") int pageSize,
                                  @RequestParam(defaultValue = "id") String sort);

        @Operation(summary = "Get a CarBrand by id")
        @ApiResponses(value = {
                @ApiResponse(responseCode = "200", description = "Found the CarBrand",
                        content = {@Content(mediaType = "application/json",
                                schema = @Schema(implementation = CarBrandDto.class))}),
                @ApiResponse(responseCode = "400", description = "Invalid id supplied",
                        content = @Content)})
        CarBrandDto findById(@Parameter(description = "An id of the CarBrand to be searched")
                             @PathVariable("id") Long id);

        @Operation(summary = "Get a CarBrand by name")
        @ApiResponses(value = {
                @ApiResponse(responseCode = "200", description = "Found the CarBrand",
                        content = {@Content(mediaType = "application/json",
                                schema = @Schema(implementation = CarBrandDto.class))})})
        CarBrandDto findByName(@PathVariable("name") String name);

        @Operation(summary = "Create new CarBrand",
                security = @SecurityRequirement(name = "bearerAuth"))
        @ApiResponses(value = {
                @ApiResponse(responseCode = "200", description = "CarBrand created",
                        content = {@Content(mediaType = "application/json",
                                schema = @Schema(implementation = CarBrandDto.class))}),
                @ApiResponse(responseCode = "201", description = "CarBrand created",
                        content = {@Content(mediaType = "application/json",
                                schema = @Schema(implementation = CarBrandDto.class))}),
                @ApiResponse(responseCode = "400", description = "Invalid data supplied",
                        content = @Content),
                @ApiResponse(responseCode = "401", description = "Access denied. Unauthorized user can perform only GET requests.",
                        content = @Content)})
        ResponseEntity<CarBrandDto> createCarBrand(@Parameter(description = "The data to create CarBrand")
                                                   @RequestBody @Valid CarBrandDto carBrandDto);

        @Operation(summary = "Update CarBrand",
                security = @SecurityRequirement(name = "bearerAuth"))
        @ApiResponses(value = {
                @ApiResponse(responseCode = "200", description = "CarBrand updated",
                        content = {@Content(mediaType = "application/json",
                                schema = @Schema(implementation = CarBrandDto.class))}),
                @ApiResponse(responseCode = "400", description = "Invalid data supplied",
                        content = @Content),
                @ApiResponse(responseCode = "401", description = "Access denied. Unauthorized user can perform only GET requests.",
                        content = @Content)})
        ResponseEntity<CarBrandDto> updateCarBrand(@Parameter(description = "The data to update CarBrand")
                                                   @RequestBody @Valid CarBrandDto carBrandDto);

        @Operation(summary = "Delete CarBrand by id",
                security = @SecurityRequirement(name = "bearerAuth"))
        @ApiResponses(value = {
                @ApiResponse(responseCode = "200", description = "CarBrand with given id deleted",
                        content = {@Content(mediaType = "application/json",
                                schema = @Schema(implementation = CarBrandDto.class))}),
                @ApiResponse(responseCode = "401", description = "Access denied. Unauthorized user can perform only GET requests.",
                        content = @Content)})
        void deleteCarBrand(@Parameter(description = "An id of the CarBrand to be deleted")
                            @PathVariable("id") Long id);
}