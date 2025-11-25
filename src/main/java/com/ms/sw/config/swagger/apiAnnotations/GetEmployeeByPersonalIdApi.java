package com.ms.sw.config.swagger.apiAnnotations;

import com.ms.sw.entity.Employees;
import io.swagger.v3.oas.annotations.Operation;
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
        summary = "Retrieve a specific employee by personal ID",
        description = "Fetches the detailed record for a single employee using their unique personal ID, scoped to the current user.",
        tags = {"Employees"},
        security = @SecurityRequirement(name = "Bearer Authentication")
)
@ApiResponses(value = {
        @ApiResponse(
                responseCode = "200",
                description = "Successfully retrieved employee details",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = Employees.class)
                )
        ),
        @ApiResponse(
                responseCode = "401",
                description = "Unauthorized - Authentication required or token is invalid/expired",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = Map.class, example = "{\"error\": \"Authentication failed. Invalid username or password.\"}")
                )
        ),
        @ApiResponse(
                responseCode = "404",
                description = "Not Found - Employee with the given personal ID was not found under the user's account.",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = Map.class, example = "{\"error\": \"The requested resource was not found.\"}")
                )
        )
})
public @interface GetEmployeeByPersonalIdApi {
}
