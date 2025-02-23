package app.mathias.sector.service;

import app.mathias.sector.dto.UserDTO;
import app.mathias.sector.entity.SectorEntity;
import app.mathias.sector.entity.UserEntity;
import app.mathias.sector.mapper.SectorMapper;
import app.mathias.sector.mapper.UserMapper;
import app.mathias.sector.model.Sector;
import app.mathias.sector.model.User;
import app.mathias.sector.repository.UserRepository;
import app.mathias.sector.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Spy
    private SectorMapper sectorMapper;

    @Mock
    private SectorService sectorService;

    @Mock
    private ApplicationContext applicationContext;

    private UserMapper userMapper;

    private UserService userService;

    @BeforeEach
    void setUp() {
        userMapper = Mockito.spy(new UserMapper(sectorMapper));
        userService = new UserServiceImpl(userRepository, userMapper, sectorService);
    }

    @Test
    void saveUser_ShouldSaveAndReturnUser() {
        // Given
        final UserDTO userDTO = getUserDTO();
        final UserEntity userEntity = getUserEntity();

        when(sectorService.findSectorsByIds(anyList())).thenReturn(getSectors());
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);

        // When
        final User savedUser = userService.saveUser(userDTO);

        // Then
        assertThat(savedUser).isNotNull();
        assertThat(savedUser.id()).isEqualTo(1L);
        assertThat(savedUser.name()).isEqualTo("John Doe");
        assertThat(savedUser.sectors()).hasSize(1);
        assertThat(savedUser.sectors().getFirst().id()).isEqualTo(1);
        assertThat(savedUser.agreedToTerms()).isTrue();

        Mockito.verify(userRepository, Mockito.times(1)).save(any(UserEntity.class));
        Mockito.verify(userMapper, Mockito.times(1)).toUser(any(UserEntity.class));
        Mockito.verify(sectorMapper, Mockito.times(1)).toSector(any(SectorEntity.class));
    }

    private static UserEntity getUserEntity() {
        return UserEntity.builder()
                .id(1L)
                .name("John Doe")
                .agreedToTerms(true)
                .sectors(List.of(SectorEntity.builder().id(1).build()))
                .build();
    }

    private static List<Sector> getSectors() {
        return List.of(Sector.builder().id(1).name("Sector 1").build());
    }

    private static UserDTO getUserDTO() {
        return UserDTO.builder()
                .name("John Doe")
                .agreedToTerms(true)
                .sectors(Collections.singletonList(1))
                .build();
    }
} 