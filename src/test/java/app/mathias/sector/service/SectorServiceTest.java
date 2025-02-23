package app.mathias.sector.service;

import app.mathias.sector.entity.SectorEntity;
import app.mathias.sector.mapper.SectorMapper;
import app.mathias.sector.model.Sector;
import app.mathias.sector.repository.SectorRepository;
import app.mathias.sector.service.impl.SectorServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class SectorServiceTest {

    @Mock
    private SectorRepository sectorRepository;

    @Spy
    private SectorMapper sectorMapper;

    private SectorService sectorService;

    @BeforeEach
    void setUp() {
        sectorService = new SectorServiceImpl(sectorRepository, sectorMapper);
    }

    @Test
    void fetchAllSectors_ShouldReturnAllSectors() {
        // Given
        final List<SectorEntity> expectedEntitySectors = createSectorEntities();
        final List<Sector> expectedSectors = createSectors();

        when(sectorRepository.findAllByParentIdIsNullOrderByName()).thenReturn(expectedEntitySectors);

        // When
        final List<Sector> actualSectors = sectorService.fetchAllSectors();

        // Then
        assertThat(actualSectors).isNotNull();
        assertThat(actualSectors).hasSize(2);
        assertThat(actualSectors).containsExactlyElementsOf(expectedSectors);

        Mockito.verify(sectorMapper, Mockito.times(3)).toSectorList(anyList());
        Mockito.verify(sectorMapper, Mockito.times(2)).toSector(any(SectorEntity.class));
        Mockito.verify(sectorRepository, Mockito.times(1)).findAllByParentIdIsNullOrderByName();
    }

    private static List<Sector> createSectors() {
        return Arrays.asList(
                Sector.builder()
                        .id(1)
                        .name("Manufacturing")
                        .parentId(null)
                        .children(Collections.emptyList())
                        .build(),
                Sector.builder()
                        .id(2)
                        .name("Construction")
                        .parentId(null)
                        .children(Collections.emptyList())
                        .build()
        );
    }

    private static List<SectorEntity> createSectorEntities() {
        return Arrays.asList(
                SectorEntity.builder()
                        .id(1)
                        .name("Manufacturing")
                        .children(Collections.emptyList())
                        .build(),
                SectorEntity.builder()
                        .id(2)
                        .name("Construction")
                        .children(Collections.emptyList())
                        .build()
        );
    }

} 