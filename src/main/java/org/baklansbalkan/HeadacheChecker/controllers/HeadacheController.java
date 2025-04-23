package org.baklansbalkan.HeadacheChecker.controllers;

import jakarta.validation.Valid;
import org.baklansbalkan.HeadacheChecker.dto.HeadacheDTO;
import org.baklansbalkan.HeadacheChecker.services.HeadacheService;
import org.baklansbalkan.HeadacheChecker.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/headache")
public class HeadacheController {

    private final HeadacheService headacheService;
    private final HeadacheValidator headacheValidator;

    @Autowired
    public HeadacheController(HeadacheService headacheService, HeadacheValidator headacheValidator) {
        this.headacheService = headacheService;
        this.headacheValidator = headacheValidator;
    }

    @GetMapping
    public List<HeadacheDTO> getAllHeadache() {
        return headacheService.findAllHeadache();
    }

    @GetMapping("/{date}")
    public HeadacheDTO getHeadacheByDate(@PathVariable("date") LocalDate date) {
        return headacheService.findHeadacheByDate(date);
    }

    @PostMapping
    public HeadacheDTO createHeadache(@RequestBody @Valid HeadacheDTO headacheDTO, BindingResult bindingResult) {
        headacheValidator.validate(headacheDTO, bindingResult);
        checkBindingResults(bindingResult);
        return headacheService.saveHeadache(headacheDTO);
    }

    @PutMapping("/{date}")
    public HeadacheDTO updateHeadache(@PathVariable("date") LocalDate date, @RequestBody @Valid
    HeadacheDTO headacheDTO, BindingResult bindingResult) {
        checkBindingResults(bindingResult);
        int updatedHeadacheId = headacheService.findHeadacheByDate(date).getId();
        headacheService.updateHeadache(updatedHeadacheId, headacheDTO);
        return headacheDTO;
    }

    @DeleteMapping("/{date}")
    public void deleteHeadache(@PathVariable("date") LocalDate date) {
        headacheService.deleteHeadache(headacheService.findHeadacheByDate(date));
    }

    @ExceptionHandler
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    private HeadacheErrorResponse handleHeadacheNotFoundException(HeadacheNotFoundException exception) {
        return new HeadacheErrorResponse(exception.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    private HeadacheErrorResponse handleHeadacheNotCreatedException(HeadacheNotCreatedException exception) {
        return new HeadacheErrorResponse(exception.getMessage());
    }

    private void checkBindingResults(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<FieldError> errors = bindingResult.getFieldErrors();
            StringBuilder errorMessage = new StringBuilder();
            for (FieldError error : errors) {
                errorMessage.append(error.getField())
                        .append(": ")
                        .append(error.getDefaultMessage())
                        .append("; ");
            }
            throw new HeadacheNotCreatedException(errorMessage.toString());
        }
    }
}
