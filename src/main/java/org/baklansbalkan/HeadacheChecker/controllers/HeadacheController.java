package org.baklansbalkan.HeadacheChecker.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.baklansbalkan.HeadacheChecker.aspect.annotation.CurrentUserId;
import org.baklansbalkan.HeadacheChecker.dto.HeadacheDTO;
import org.baklansbalkan.HeadacheChecker.services.HeadacheService;
import org.baklansbalkan.HeadacheChecker.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
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
    private final Logger log = LoggerFactory.getLogger(HeadacheController.class);

    @Autowired
    public HeadacheController(HeadacheService headacheService, HeadacheValidator headacheValidator) {
        this.headacheService = headacheService;
        this.headacheValidator = headacheValidator;
    }

    @Operation(summary = "Get information about headaches", description = "Returns the current user's data")
    @ApiResponse(responseCode = "200", description = "Success")
    @GetMapping
    public List<HeadacheDTO> getAllHeadacheForCurrentUser(@Parameter(hidden = true) @CurrentUserId Integer userId) {
        return headacheService.findAllHeadacheByUserId(userId);
    }

    @Operation(summary = "Get information about headaches using date", description = "Returns the current user's data by date")
    @ApiResponse(responseCode = "200", description = "Success")
    @GetMapping("/{date}")
    public HeadacheDTO getHeadacheByDate(@PathVariable("date") LocalDate date,
                                         @Parameter(hidden = true) @CurrentUserId Integer userId) {
        return headacheService.findHeadacheByDateAndUserId(date, userId);
    }

    @Operation(summary = "Create new headache entry")
    @ApiResponse(responseCode = "200", description = "Success")
    @PostMapping
    public HeadacheDTO createHeadache(@RequestBody @Valid HeadacheDTO headacheDTO,
                                      @Parameter(hidden = true) @CurrentUserId Integer userId,
                                      BindingResult bindingResult) {
        headacheDTO.setUserId(userId);
        headacheValidator.validate(headacheDTO, bindingResult);
        checkBindingResults(bindingResult);
        return headacheService.saveHeadache(headacheDTO);
    }

    @Operation(summary = "Edit the existing headache entry")
    @ApiResponse(responseCode = "200", description = "Success")
    @PutMapping("/{date}")
    public HeadacheDTO updateHeadache(@PathVariable("date") LocalDate date,
                                      @Parameter(hidden = true) @CurrentUserId Integer userId,
                                      @RequestBody @Valid HeadacheDTO headacheDTO,
                                      BindingResult bindingResult) {
        HeadacheDTO existingHeadache = headacheService.findHeadacheByDateAndUserId(date, userId);
        headacheDTO.setId(existingHeadache.getId());
        headacheDTO.setUserId(userId);
        headacheValidator.validate(headacheDTO, bindingResult);
        checkBindingResults(bindingResult);
        return headacheService.updateHeadache(existingHeadache.getId(), headacheDTO);
    }

    @Operation(summary = "Delete an entry")
    @ApiResponse(responseCode = "200", description = "Success")
    @DeleteMapping("/{date}")
    public void deleteHeadache(@PathVariable("date") LocalDate date,
                               @Parameter(hidden = true) @CurrentUserId Integer userId) {
        headacheService.deleteHeadache(headacheService.findHeadacheByDateAndUserId(date, userId));
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
}
