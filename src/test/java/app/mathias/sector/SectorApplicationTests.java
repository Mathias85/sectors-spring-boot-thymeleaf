package app.mathias.sector;

import app.mathias.sector.utils.MessageHelper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class SectorApplicationTests {

    @Test
    void contextLoads() {
        // Test message helper not throws exception which means it is being initialized correctly
        assertThat(MessageHelper.getMessage("")).isEmpty();
    }

}
