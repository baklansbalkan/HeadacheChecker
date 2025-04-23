package org.baklansbalkan.HeadacheChecker.services;

import org.baklansbalkan.HeadacheChecker.dto.HeadacheDTO;
import org.baklansbalkan.HeadacheChecker.models.Headache;
import org.baklansbalkan.HeadacheChecker.repositories.HeadacheRepository;
import org.baklansbalkan.HeadacheChecker.util.HeadacheMapper;
import org.baklansbalkan.HeadacheChecker.util.HeadacheNotCreatedException;
import org.baklansbalkan.HeadacheChecker.util.HeadacheNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class HeadacheService {

    private final HeadacheRepository headacheRepository;
    private final HeadacheMapper headacheMapper;

    @Autowired
    public HeadacheService(HeadacheRepository headacheRepository, HeadacheMapper headacheMapper) {
        this.headacheRepository = headacheRepository;
        this.headacheMapper = headacheMapper;
    }

    public List<HeadacheDTO> findAllHeadache() {
        return headacheRepository.findAll().stream()
                .map(headacheMapper::convertToHeadacheDTO)
                .toList();
    }

    public HeadacheDTO findHeadacheByDate(LocalDate date) {
        if (Optional.ofNullable(headacheRepository.findByDate(date)).isPresent()) {
            return headacheMapper.convertToHeadacheDTO(headacheRepository.findByDate(date));
        } else {
            throw new HeadacheNotFoundException("Sorry, this entry doesn't exist");
        }
    }

    @Transactional
    public HeadacheDTO saveHeadache(HeadacheDTO headacheDTO) {
        try {
            Headache headache = headacheMapper.convertToHeadache(headacheDTO);
            enrichHeadache(headache);
            headacheRepository.save(headache);
            return headacheMapper.convertToHeadacheDTO(headache);
        } catch (RuntimeException exception) {
            throw new HeadacheNotCreatedException("Sorry, this entry hasn't been saved");
        }
    }

    @Transactional
    public HeadacheDTO updateHeadache(int id, HeadacheDTO updatedHeadacheDTO) {
        try {
            Headache updatedHeadache = headacheMapper.convertToHeadache(updatedHeadacheDTO);
            enrichHeadache(updatedHeadache);
            updatedHeadache.setId(id);
            headacheRepository.save(updatedHeadache);
            return headacheMapper.convertToHeadacheDTO(updatedHeadache);
        } catch (RuntimeException exception) {
            throw new HeadacheNotCreatedException("Sorry, this entry hasn't been updated");
        }
    }

    @Transactional
    public void deleteHeadache(HeadacheDTO headacheDTO) {
        headacheRepository.findById(headacheDTO.getId()).orElseThrow(
                () -> new HeadacheNotFoundException("Sorry, this entry doesn't exist"));
        headacheRepository.delete(headacheMapper.convertToHeadache(headacheDTO));
    }

    private void enrichHeadache(Headache headache) {
        headache.setCreatedAt(LocalDateTime.now());
    }
}
