package app.mathias.sector.integration;

import app.mathias.sector.entity.UserEntity;
import app.mathias.sector.model.Sector;
import app.mathias.sector.repository.UserRepository;
import app.mathias.sector.service.SectorService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class UserIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SectorService sectorService;

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldLoadIndexPageWithSectors() throws Exception {
        // When & Then
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attributeExists("sectors"))
                .andExpect(model().attributeExists("user"))
                .andExpect(content().string(containsString("name=\"name\"")))
                .andExpect(content().string(containsString("name=\"sectors\"")))
                .andExpect(content().string(containsString("name=\"agreedToTerms\"")))
                .andExpect(content().string(containsString("type=\"submit\"")));
    }

    @Test
    void shouldSaveUserAndRedirectToSuccess() throws Exception {
        // Given
        final List<Sector> availableSectors = sectorService.fetchAllSectors();
        assertThat(availableSectors).isNotEmpty();
        final Integer selectedSectorId = availableSectors.getFirst().id();

        // When & Then
        mockMvc.perform(post("/save")
                        .param("name", "John Doe")
                        .param("sectors", selectedSectorId.toString())
                        .param("agreedToTerms", "true"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("index"));

        // Verify the user was saved in the database
        assertThat(userRepository.findAll())
                .hasSize(1)
                .first()
                .satisfies(user -> {
                    assertThat(user.getName()).isEqualTo("John Doe");
                    assertThat(user.getAgreedToTerms()).isTrue();
                    assertThat(user.getSectors()).hasSize(1);
                    assertThat(user.getSectors().getFirst().getId()).isEqualTo(selectedSectorId);
                });
    }

    @Test
    void shouldShowValidationErrorsWhenUserDataIsInvalid() throws Exception {
        // When & Then
        mockMvc.perform(post("/save")
                        .param("name", "")  // empty name
                        .param("agreedToTerms", "false"))  // terms not agreed
                .andExpect(status().isOk())  // returns to the same page
                .andExpect(view().name("index"))  // shows the form again
                .andExpect(model().attributeExists("sectors"))  // sectors should still be there
                .andExpect(model().hasErrors())  // should have validation errors
                .andExpect(model().attributeHasFieldErrors("user", "name"))
                .andExpect(model().attributeHasFieldErrors("user", "sectors"))
                .andExpect(model().attributeHasFieldErrors("user", "agreedToTerms"))
                .andExpect(content().string(containsString("Name is required")))
                .andExpect(content().string(containsString("Must agree to terms")))
                .andExpect(content().string(containsString("At least one sector must be selected")));
    }

    @Test
    void shouldSaveUserWithMultipleSectors() throws Exception {
        // Given
        final List<Sector> availableSectors = sectorService.fetchAllSectors();
        assertThat(availableSectors).size().isGreaterThanOrEqualTo(2);
        final String[] selectedSectorIds = {
                availableSectors.get(0).id().toString(),
                availableSectors.get(1).id().toString()
        };

        // When & Then
        mockMvc.perform(post("/save")
                        .param("name", "Jane Doe")
                        .param("sectors", selectedSectorIds)
                        .param("agreedToTerms", "true"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("index"));  // shows the form again

        // Verify the user was saved with multiple sectors
        assertThat(userRepository.findAll())
                .hasSize(1)
                .first()
                .satisfies(user -> {
                    assertThat(user.getName()).isEqualTo("Jane Doe");
                    assertThat(user.getAgreedToTerms()).isTrue();
                    assertThat(user.getSectors())
                            .hasSize(2)
                            .extracting("id")
                            .containsExactlyInAnyOrder(
                                    Integer.valueOf(selectedSectorIds[0]),
                                    Integer.valueOf(selectedSectorIds[1]));
                });
    }

    @Test
    void shouldUpdateExistingUser() throws Exception {
        // Given - First save a user
        final List<Sector> availableSectors = sectorService.fetchAllSectors();
        assertThat(availableSectors.size()).isGreaterThanOrEqualTo(2);
        final Integer initialSectorId = availableSectors.getFirst().id();

        mockMvc.perform(post("/save")
                        .param("name", "Initial Name")
                        .param("sectors", initialSectorId.toString())
                        .param("agreedToTerms", "true"))
                .andExpect(status().is2xxSuccessful());

        // Get the saved user's ID
        final UserEntity savedUser = userRepository.findAll().get(0);
        final Long userId = savedUser.getId();

        // When - Update the user with new data
        final Integer newSectorId = availableSectors.get(1).id();
        mockMvc.perform(post("/save")
                        .param("id", userId.toString())
                        .param("name", "Updated Name")
                        .param("sectors", newSectorId.toString())
                        .param("agreedToTerms", "true"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("index"));

        // Then - Verify the user was updated in the database
        assertThat(userRepository.findAll())
                .hasSize(1)
                .first()
                .satisfies(user -> {
                    assertThat(user.getId()).isEqualTo(userId);
                    assertThat(user.getName()).isEqualTo("Updated Name");
                    assertThat(user.getAgreedToTerms()).isTrue();
                    assertThat(user.getSectors())
                            .hasSize(1)
                            .first()
                            .satisfies(sector -> assertThat(sector.getId()).isEqualTo(newSectorId));
                });
    }
} 