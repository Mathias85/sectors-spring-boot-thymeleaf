package app.mathias.sector.service.impl;

import app.mathias.sector.dto.UserDTO;
import app.mathias.sector.entity.UserEntity;
import app.mathias.sector.exception.ResourceNotFoundException;
import app.mathias.sector.exception.ValidationException;
import app.mathias.sector.mapper.UserMapper;
import app.mathias.sector.model.Sector;
import app.mathias.sector.model.User;
import app.mathias.sector.repository.UserRepository;
import app.mathias.sector.service.SectorService;
import app.mathias.sector.service.UserService;
import app.mathias.sector.utils.MessageHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final SectorService sectorService;

    @Override
    @Transactional
    public User saveUser(final UserDTO userDTO) {
        // Verify all sectors exist
        final List<Sector> selectedSectors = sectorService.findSectorsByIds(userDTO.sectors());
        if (selectedSectors.isEmpty()) {
            throw new ValidationException(MessageHelper
                    .getMessage("error.sectors.notfound"),
                    List.of(MessageHelper.getMessage("error.sectors.empty")));
        }
        
        UserEntity userEntity;
        // If updating existing user, verify it exists and use it as base
        if (userDTO.id() != null) {
            userEntity = userRepository.findById(userDTO.id())
                    .orElseThrow(() -> new ResourceNotFoundException("User", "id", userDTO.id()));
            // Update the existing entity with new values
            userMapper.updateUserEntity(userEntity, userDTO);
        } else {
            // Create new entity for new user
            userEntity = userMapper.toUserEntity(userDTO);
        }

        final UserEntity savedUser = userRepository.save(userEntity);
        return userMapper.toUser(savedUser);
    }
}
