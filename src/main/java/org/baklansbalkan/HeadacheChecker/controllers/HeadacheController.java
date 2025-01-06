package org.baklansbalkan.HeadacheChecker.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import org.baklansbalkan.HeadacheChecker.dto.HeadacheDTO;
import org.baklansbalkan.HeadacheChecker.models.Headache;
import org.baklansbalkan.HeadacheChecker.services.HeadacheService;
import org.baklansbalkan.HeadacheChecker.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/headache")
public class HeadacheController {

    private final HeadacheService headacheService;
    private final ObjectMapper objectMapper;
    private final HeadacheValidator headacheValidator;

    @Autowired
    public HeadacheController(HeadacheService headacheService, ObjectMapper objectMapper, HeadacheValidator headacheValidator) {
        this.headacheService = headacheService;
        this.objectMapper = objectMapper;
        this.headacheValidator = headacheValidator;
    }

    @GetMapping
    public List<HeadacheDTO> getAllHeadache() {
        return headacheService.findAllHeadache().stream()
                .map(this::convertToHeadacheDTO)
                .toList();
    }

    @GetMapping("/{date}")
    public HeadacheDTO getHeadacheByDate(@PathVariable("date") LocalDate date) {
        return convertToHeadacheDTO(headacheService.findHeadacheByDate(date));
    }

    @PostMapping
    public ResponseEntity<HttpStatus> createHeadache(@RequestBody @Valid HeadacheDTO headacheDTO, BindingResult bindingResult) {
        headacheValidator.validate(headacheDTO, bindingResult);
        checkBindingResults(bindingResult);
        headacheService.saveHeadache(convertToHeadache(headacheDTO));
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PutMapping("/{date}")
    public ResponseEntity<HttpStatus> updateHeadache(@PathVariable("date") LocalDate date, @RequestBody @Valid
    HeadacheDTO headacheDTO, BindingResult bindingResult) {
        checkBindingResults(bindingResult);
        int updatedHeadacheId = headacheService.findHeadacheByDate(date).getId();
        headacheService.updateHeadache(updatedHeadacheId, convertToHeadache(headacheDTO));
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/{date}")
    public ResponseEntity<HttpStatus> deleteHeadache(@PathVariable("date") LocalDate date) {
        headacheService.deleteHeadache(headacheService.findHeadacheByDate(date));
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @ExceptionHandler
    private ResponseEntity<HeadacheErrorResponse> handleHeadacheNotFoundException(HeadacheNotFoundException e) {
        HeadacheErrorResponse response = new HeadacheErrorResponse(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    private ResponseEntity<HeadacheErrorResponse> handleHeadacheNotCreatedException(HeadacheNotCreatedException e) {
        HeadacheErrorResponse response = new HeadacheErrorResponse(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    private Headache convertToHeadache(HeadacheDTO headacheDTO) {
        return objectMapper.convertValue(headacheDTO, Headache.class);
    }

    private HeadacheDTO convertToHeadacheDTO(Headache headache) {
        return objectMapper.convertValue(headache, HeadacheDTO.class);
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
