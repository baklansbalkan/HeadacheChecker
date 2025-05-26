package org.baklansbalkan.HeadacheChecker.services;

import org.baklansbalkan.HeadacheChecker.dto.UserDTO;
import org.baklansbalkan.HeadacheChecker.models.User;
import org.baklansbalkan.HeadacheChecker.repositories.UserRepository;
import org.baklansbalkan.HeadacheChecker.util.EntryNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class AdminService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;


    public AdminService(UserRepository userRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> modelMapper.map(user, UserDTO.class))
                .toList();
    }

    public UserDTO getUserById(int id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntryNotFoundException("Sorry, this user doesn't exist"));
        return modelMapper.map(user, UserDTO.class);
    }

    public UserDTO getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntryNotFoundException("Sorry, this user doesn't exist"));
        return modelMapper.map(user, UserDTO.class);
    }

    public UserDTO getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntryNotFoundException("User not found"));
        return modelMapper.map(user, UserDTO.class);
    }

    @Transactional
    public void deleteUserById(int id) {
        userRepository.deleteById(id);
    }
}
