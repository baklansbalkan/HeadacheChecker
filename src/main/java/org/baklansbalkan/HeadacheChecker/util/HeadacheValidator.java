package org.baklansbalkan.HeadacheChecker.util;

import org.baklansbalkan.HeadacheChecker.dto.HeadacheDTO;
import org.baklansbalkan.HeadacheChecker.services.HeadacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class HeadacheValidator implements Validator {

    private final HeadacheService headacheService;

    @Autowired
    public HeadacheValidator(HeadacheService headacheService) {
        this.headacheService = headacheService;
    }

    @Override
    public boolean supports(Class<?> myClass) {
        return HeadacheDTO.class.equals(myClass);
    }

    @Override
    public void validate(Object target, Errors errors) {
        HeadacheDTO headache = (HeadacheDTO) target;
        try {
            headacheService.findHeadacheByDate(headache.getDate());
        } catch (HeadacheNotFoundException ignored) {
            return;
        }
        errors.rejectValue("Date", "", "Entry for this date is already exist");
    }
}

