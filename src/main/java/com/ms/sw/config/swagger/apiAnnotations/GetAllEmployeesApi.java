package com.ms.sw.config.swagger.apiAnnotations;

import com.ms.sw.entity.Employees;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Map;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Operation(
        summary = "Retrieve all employees for the currently authenticated user",
        description = "Fetches a list of all employee records associated with the logged-in user's account.",
        tags = {"Employees"},
        security = @SecurityRequirement(name = "Bearer Authentication")
)
@ApiResponses(value = {
        @ApiResponse(
                responseCode = "200",
                description = "Successfully retrieved list of employees",
                content = @Content(
                        mediaType = "application/json",
                        array = @ArraySchema(schema = @Schema(implementation = Employees.class))
                )
        ),
        @ApiResponse(
                responseCode = "401",
                description = "Unauthorized - Authentication required or token is invalid/expired",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = Map.class, example = "{\"error\": \"Full authentication is required to access this resource\"}")
                )
        ),
        @ApiResponse(
                responseCode = "404",
                description = "Employees Not Found - User exists but has no employees.",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = Map.class, example = "{\"error\": \"No employees found for user\"}")
                )
        )
})

public @interface GetAllEmployeesApi {
}
