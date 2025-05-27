package org.baklansbalkan.HeadacheChecker.services;

import org.baklansbalkan.HeadacheChecker.dto.HeadacheDTO;
import org.baklansbalkan.HeadacheChecker.models.Headache;
import org.baklansbalkan.HeadacheChecker.repositories.HeadacheRepository;
import org.baklansbalkan.HeadacheChecker.util.EntryNotCreatedException;
import org.baklansbalkan.HeadacheChecker.util.EntryNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class HeadacheService {

    private final HeadacheRepository headacheRepository;
    private final ModelMapper modelMapper;


    @Autowired
    public HeadacheService(HeadacheRepository headacheRepository, ModelMapper modelMapper) {
        this.headacheRepository = headacheRepository;
        this.modelMapper = modelMapper;
    }

    public List<HeadacheDTO> findAllHeadacheByUserId(Integer userId) {
        return headacheRepository.findByUserId(userId).stream()
                .map(headache -> modelMapper.map(headache, HeadacheDTO.class))
                .toList();
    }

    public HeadacheDTO findHeadacheByDateAndUserId(LocalDate date, Integer userId) {
        Headache headache = headacheRepository.findByDateAndUserId(date, userId)
                .orElseThrow(() -> new EntryNotFoundException("Sorry, this entry doesn't exist"));
        return modelMapper.map(headache, HeadacheDTO.class);
    }

    @Transactional
    public HeadacheDTO saveHeadache(HeadacheDTO headacheDTO) {
        try {
            Headache headache = modelMapper.map(headacheDTO, Headache.class);
            enrichHeadache(headache);
            headacheRepository.save(headache);
            return modelMapper.map(headache, HeadacheDTO.class);
        } catch (RuntimeException exception) {
            throw new EntryNotCreatedException("Sorry, this entry hasn't been saved");
        }
    }

    @Transactional
    public HeadacheDTO updateHeadache(int id, HeadacheDTO updatedHeadacheDTO) {
        headacheRepository.findById(id).orElseThrow(
                () -> new EntryNotFoundException("Sorry, this entry doesn't exist"));
        try {
            Headache updatedHeadache = modelMapper.map(updatedHeadacheDTO, Headache.class);
            enrichHeadache(updatedHeadache);
            updatedHeadache.setId(id);
            headacheRepository.save(updatedHeadache);
            return modelMapper.map(updatedHeadache, HeadacheDTO.class);
        } catch (RuntimeException exception) {
            throw new EntryNotCreatedException("Sorry, this entry hasn't been updated");
        }
    }

    @Transactional
    public void deleteHeadache(HeadacheDTO headacheDTO) {
        headacheRepository.findById(headacheDTO.getId())
                .orElseThrow(() -> new EntryNotFoundException("Sorry, this entry doesn't exist"));
        headacheRepository.delete(modelMapper.map(headacheDTO, Headache.class));
    }

    private void enrichHeadache(Headache headache) {
        headache.setCreatedAt(LocalDateTime.now());
    }
}
