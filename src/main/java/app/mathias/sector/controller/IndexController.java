package app.mathias.sector.controller;

import app.mathias.sector.dto.UserDTO;
import app.mathias.sector.exception.ResourceNotFoundException;
import app.mathias.sector.exception.ValidationException;
import app.mathias.sector.model.Sector;
import app.mathias.sector.model.User;
import app.mathias.sector.service.SectorService;
import app.mathias.sector.service.UserService;
import app.mathias.sector.utils.MessageHelper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Tag(name = "Sector Selection", description = "Sector selection management APIs")
@RequiredArgsConstructor
@Controller
public class IndexController {

    private final SectorService sectorService;
    private final UserService userService;

    @Operation(
        summary = "Display sector selection form",
        description = "Retrieves all available sectors and displays the sector selection form"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved sectors and displayed form",
            content = @Content(mediaType = "text/html")
        )
    })
    @GetMapping("/")
    public String index(final Model model) {
        final List<Sector> sectors = sectorService.fetchAllSectors();
        model.addAttribute("sectors", sectors);
        model.addAttribute("user", User.builder().build());
        return "index";
    }

    @Operation(
        summary = "Save user sector selections",
        description = "Saves the user's name, selected sectors, and terms agreement"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Successfully saved user selections",
            content = @Content(mediaType = "text/html")
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid input data",
            content = @Content(mediaType = "text/html")
        )
    })
    @PostMapping("save")
    public String index(
            @Parameter(description = "User data including name, sectors, and terms agreement")
            @Valid @ModelAttribute("user") final UserDTO user,
            final BindingResult result,
            final Model model
    ) {

        if (!result.hasErrors()) {
            try {
                final User savedUser = userService.saveUser(user);
                final List<Integer> sectorIds = savedUser.sectors().stream().map(Sector::id).toList();
                model.addAttribute("user", savedUser);
                model.addAttribute("sectorIds", sectorIds);
                model.addAttribute("successMessage", MessageHelper.getMessage("success.save"));
            } catch (ValidationException | ResourceNotFoundException e) {
                model.addAttribute("errorMessage", e.getMessage());
            }
        }

        final List<Sector> sectors = sectorService.fetchAllSectors();
        model.addAttribute("sectors", sectors);

        return "index";
    }

    @ExceptionHandler(Exception.class)
    public String handleError(Exception ex, Model model) {
        final List<Sector> sectors = sectorService.fetchAllSectors();

        ex.printStackTrace();

        model.addAttribute("errorMessage", MessageHelper.getMessage("error.unexpected"));
        model.addAttribute("user", User.builder().build());
        model.addAttribute("sectors", sectors);

        return "index";
    }
}
