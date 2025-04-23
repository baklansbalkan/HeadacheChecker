package org.baklansbalkan.HeadacheChecker.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.baklansbalkan.HeadacheChecker.dto.HeadacheDTO;
import org.baklansbalkan.HeadacheChecker.models.Headache;
import org.springframework.stereotype.Component;

@Component
public class HeadacheMapper {

    private final ObjectMapper objectMapper;

    public HeadacheMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public HeadacheDTO convertToHeadacheDTO(Headache headache) {
        return objectMapper.convertValue(headache, HeadacheDTO.class);
    }

    public Headache convertToHeadache(HeadacheDTO headacheDTO) {
        return objectMapper.convertValue(headacheDTO, Headache.class);
    }
}
