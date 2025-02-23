package app.mathias.sector.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;

import java.util.List;

@Schema(description = "User data transfer object for sector selection form")
@Builder
public record UserDTO(
        @Schema(description = "User ID (null for new users)", example = "1")
        Long id,

        @Schema(description = "User's full name", example = "John Doe", required = true)
        @NotBlank String name,

        @Schema(description = "Whether user has agreed to terms and conditions", example = "true", required = true)
        @AssertTrue Boolean agreedToTerms,

        @Schema(description = "List of selected sector IDs", example = "[1, 19, 18]", required = true)
        @NotEmpty List<Integer> sectors
) {}
