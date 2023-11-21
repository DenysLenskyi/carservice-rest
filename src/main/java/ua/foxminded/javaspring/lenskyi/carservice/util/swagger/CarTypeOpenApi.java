package ua.foxminded.javaspring.lenskyi.carservice.util.swagger;

import io.swagger.v3.oas.annotations.Operation;
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
import ua.foxminded.javaspring.lenskyi.carservice.model.dto.CarTypeDto;

import java.util.List;

public interface CarTypeOpenApi {

        @Operation(summary = "Get the list of CarType, paginated, sorted")
        @ApiResponses(value = {
                @ApiResponse(responseCode = "200", description = "Listed with given parameters",
                        content = {@Content(mediaType = "application/json",
                                schema = @Schema(implementation = CarTypeDto.class))})})
        List<CarTypeDto> findAll(@RequestParam(defaultValue = "0") int pageNumber,
                                 @RequestParam(defaultValue = "0") int pageSize,
                                 @RequestParam(defaultValue = "id") String sort);

        @Operation(summary = "Get a CarType by id")
        @ApiResponses(value = {
                @ApiResponse(responseCode = "200", description = "Found the CarType",
                        content = {@Content(mediaType = "application/json",
                                schema = @Schema(implementation = CarTypeDto.class))}),
                @ApiResponse(responseCode = "400", description = "Invalid id supplied",
                        content = @Content)})
        CarTypeDto findById(@PathVariable("id") Long id);

        @Operation(summary = "Get a CarType by name")
        @ApiResponses(value = {
                @ApiResponse(responseCode = "200", description = "Found the CarType",
                        content = {@Content(mediaType = "application/json",
                                schema = @Schema(implementation = CarTypeDto.class))})})
        CarTypeDto findByName(@PathVariable("name") String name);

        @Operation(summary = "Create new CarType",
                security = @SecurityRequirement(name = "bearerAuth"))
        @ApiResponses(value = {
                @ApiResponse(responseCode = "200", description = "CarType created",
                        content = {@Content(mediaType = "application/json",
                                schema = @Schema(implementation = CarTypeDto.class))}),
                @ApiResponse(responseCode = "201", description = "CarType created",
                        content = {@Content(mediaType = "application/json",
                                schema = @Schema(implementation = CarTypeDto.class))}),
                @ApiResponse(responseCode = "400", description = "Invalid data supplied",
                        content = @Content),
                @ApiResponse(responseCode = "401", description = "Access denied. Unauthorized user can perform only GET requests.",
                        content = @Content)})
        ResponseEntity<CarTypeDto> createCarType(@RequestBody @Valid CarTypeDto carTypeDto);

        @Operation(summary = "Update CarType",
                security = @SecurityRequirement(name = "bearerAuth"))
        @ApiResponses(value = {
                @ApiResponse(responseCode = "200", description = "CarType updated",
                        content = {@Content(mediaType = "application/json",
                                schema = @Schema(implementation = CarTypeDto.class))}),
                @ApiResponse(responseCode = "400", description = "Invalid data supplied",
                        content = @Content),
                @ApiResponse(responseCode = "401", description = "Access denied. Unauthorized user can perform only GET requests.",
                        content = @Content)})
        ResponseEntity<CarTypeDto> updateCarType(@RequestBody @Valid CarTypeDto carTypeDto);

        @Operation(summary = "Delete CarType by id",
                security = @SecurityRequirement(name = "bearerAuth"))
        @ApiResponses(value = {
                @ApiResponse(responseCode = "200", description = "CarType with given id deleted",
                        content = {@Content(mediaType = "application/json",
                                schema = @Schema(implementation = CarTypeDto.class))}),
                @ApiResponse(responseCode = "401", description = "Access denied. Unauthorized user can perform only GET requests.",
                        content = @Content)})
        void deleteCarType(@PathVariable("id") Long id);
}
