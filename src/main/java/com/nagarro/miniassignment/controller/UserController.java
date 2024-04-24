package com.nagarro.miniassignment.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nagarro.miniassignment.dto.UserResponse;
import com.nagarro.miniassignment.entity.User;
import com.nagarro.miniassignment.service.UserService;
import com.nagarro.miniassignment.utilities.ErrorUtils;
import com.nagarro.miniassignment.utilities.exceptions.ExternalApiException;
import com.nagarro.miniassignment.utilities.exceptions.InvalidSizeException;
import com.nagarro.miniassignment.utilities.exceptions.ValidationException;
import com.nagarro.miniassignment.utilities.validators.EnglishAlphabetValidator;
import com.nagarro.miniassignment.utilities.validators.NumericValidator;
import com.nagarro.miniassignment.utilities.validators.Validator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class UserController {

    private final UserService userService;
    private final Validator<String> englishAlphabetValidator;
    private final Validator<Integer> numericValidator;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
        this.englishAlphabetValidator = EnglishAlphabetValidator.getInstance();
        this.numericValidator = NumericValidator.getInstance();
      
    }

    @PostMapping("/users")
    public ResponseEntity<?> createUsers(@RequestBody(required = false) Map<String, Integer> requestBody) {
        try {
            // Retrieving the size parameter from the request body, if size is not represent then default is 1
            int size = (requestBody != null && requestBody.containsKey("size")) ? requestBody.get("size") : 1;

            // Validating the size
            if (size <= 0 || size > 5) {
                throw new InvalidSizeException("Size should be greater than zero and less than 5.");
            }
            
            List<User> users = userService.createUsers(size);
            return ResponseEntity.ok(users);
        } catch (InvalidSizeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ErrorUtils.createErrorResponse("Invalid size provided", HttpStatus.BAD_REQUEST.value()));
        } catch (ExternalApiException e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body(ErrorUtils.createErrorResponse("External API not available", HttpStatus.SERVICE_UNAVAILABLE.value()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ErrorUtils.createErrorResponse("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR.value()));
        }
    }
    
    @GetMapping("/users")
    public ResponseEntity<Object> getUsers(
            @RequestParam(required = true) String sortType,
            @RequestParam(required = true) String sortOrder,
            @RequestParam(required = true) Integer limit,
            @RequestParam(required = true) Integer offset
    ) {
        try {
            // Validating parameters
            if (sortType != null) {
                englishAlphabetValidator.validate(sortType);
            }
            if (sortOrder != null) {
                englishAlphabetValidator.validate(sortOrder);
            }
            if (limit != null) {
                numericValidator.validate(limit);
            }
            if (offset != null) {
                numericValidator.validate(offset);
            }
            
            if (limit < 1 || limit > 5) {
                throw new ValidationException("Limit should be between 1 and 5.");
            }
       
            List<UserResponse> users = userService.getUsers(sortType, sortOrder, limit, offset);
            Map<String, Object> response = new HashMap<>();
            response.put("data", users);
            response.put("pageInfo", userService.getPageInfo(limit, offset));
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (ValidationException ve) {
            return new ResponseEntity<>(ErrorUtils.createErrorResponse(ve.getMessage(), 400), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
        	e.printStackTrace(); 
            return new ResponseEntity<>(ErrorUtils.createErrorResponse("Internal Server Error", 500), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

