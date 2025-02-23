package app.mathias.sector.mapper;

import app.mathias.sector.dto.UserDTO;
import app.mathias.sector.entity.SectorEntity;
import app.mathias.sector.entity.UserEntity;
import app.mathias.sector.model.Sector;
import app.mathias.sector.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final SectorMapper sectorMapper;

    public UserEntity toUserEntity(final UserDTO user) {
        return Optional.ofNullable(user)
                .map(userDTO -> UserEntity.builder()
                        .name(userDTO.name())
                        .sectors(mapIdsToSectorEntity(userDTO.sectors()))
                        .agreedToTerms(userDTO.agreedToTerms())
                        .build())
                .orElse(null);
    }

    private List<SectorEntity> mapIdsToSectorEntity(final List<Integer> sectorIds) {
        return Optional.ofNullable(sectorIds)
                .orElseGet(ArrayList::new)
                .stream()
                .map(id -> SectorEntity.builder().id(id).build())
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }

    public User toUser(final UserEntity userEntity) {
        return Optional.ofNullable(userEntity)
                .map(user -> User.builder()
                        .id(user.getId())
                        .name(user.getName())
                        .sectors(mapSectorEntityToSector(user.getSectors()))
                        .agreedToTerms(user.getAgreedToTerms())
                        .build())
                .orElse(null);
    }

    private List<Sector> mapSectorEntityToSector(final List<SectorEntity> sectorEntities) {
        return Optional.ofNullable(sectorEntities)
                .orElseGet(ArrayList::new)
                .stream()
                .map(sectorMapper::toSector)
                .toList();
    }

    public void updateUserEntity(final UserEntity userEntity, final UserDTO userDTO) {
        if (userEntity != null && userDTO != null) {
            userEntity.setName(userDTO.name());
            userEntity.setSectors(mapIdsToSectorEntity(userDTO.sectors()));
            userEntity.setAgreedToTerms(userDTO.agreedToTerms());
        }
    }
}
