package app.mathias.sector.service;

import app.mathias.sector.dto.UserDTO;
import app.mathias.sector.model.User;

public interface UserService {
    /**
     * Saves a new user or updates an existing one with their sector selections.
     * @param userDTO the user data to save
     * @return the saved user
     * @throws app.mathias.sector.exception.ValidationException if validation fails
     * @throws app.mathias.sector.exception.ResourceNotFoundException if referenced sectors are not found
     */
    User saveUser(final UserDTO userDTO);
}
