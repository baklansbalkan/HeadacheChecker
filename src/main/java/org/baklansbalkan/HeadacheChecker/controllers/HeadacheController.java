package org.baklansbalkan.HeadacheChecker.controllers;

import jakarta.validation.Valid;
import org.baklansbalkan.HeadacheChecker.dto.HeadacheDTO;
import org.baklansbalkan.HeadacheChecker.security.UserDetailsImpl;
import org.baklansbalkan.HeadacheChecker.services.HeadacheService;
import org.baklansbalkan.HeadacheChecker.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/headache")
@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
public class HeadacheController {

    private final HeadacheService headacheService;
    private final HeadacheValidator headacheValidator;

    @Autowired
    public HeadacheController(HeadacheService headacheService, HeadacheValidator headacheValidator) {
        this.headacheService = headacheService;
        this.headacheValidator = headacheValidator;
    }

    @GetMapping
    public List<HeadacheDTO> getAllHeadacheForCurrentUser() {
        return headacheService.findAllHeadacheByUserId(getCurrentUserId());
    }

    @GetMapping("/{date}")
    public HeadacheDTO getHeadacheByDate(@PathVariable("date") LocalDate date) {
        return headacheService.findHeadacheByDateAndUserId(date, getCurrentUserId());
    }

    @PostMapping
    public HeadacheDTO createHeadache(@RequestBody @Valid HeadacheDTO headacheDTO, BindingResult bindingResult) {
        headacheDTO.setUserId(getCurrentUserId());
        headacheValidator.validate(headacheDTO, bindingResult);
        checkBindingResults(bindingResult);
        return headacheService.saveHeadache(headacheDTO);
    }

    @PutMapping("/{date}")
    public HeadacheDTO updateHeadache(@PathVariable("date") LocalDate date, @RequestBody @Valid
    HeadacheDTO headacheDTO, BindingResult bindingResult) {
        checkBindingResults(bindingResult);
        HeadacheDTO existingHeadache = headacheService.findHeadacheByDateAndUserId(date, getCurrentUserId());
        headacheDTO.setId(existingHeadache.getId());
        headacheDTO.setUserId(getCurrentUserId());
        return headacheService.updateHeadache(existingHeadache.getId(), headacheDTO);
    }

    @DeleteMapping("/{date}")
    public void deleteHeadache(@PathVariable("date") LocalDate date) {
        headacheService.deleteHeadache(headacheService.findHeadacheByDateAndUserId(date, getCurrentUserId()));
    }

    @ExceptionHandler
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    private HeadacheErrorResponse handleHeadacheNotFoundException(EntryNotFoundException exception) {
        return new HeadacheErrorResponse(exception.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    private HeadacheErrorResponse handleHeadacheNotCreatedException(EntryNotCreatedException exception) {
        return new HeadacheErrorResponse(exception.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    private HeadacheErrorResponse handleSqlException(Exception exception) {
        return new HeadacheErrorResponse("Database error: " + exception.getMessage());
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
            throw new EntryNotCreatedException(errorMessage.toString());
        }
    }

    private Integer getCurrentUserId() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        return userDetails.getId();
    }
}
