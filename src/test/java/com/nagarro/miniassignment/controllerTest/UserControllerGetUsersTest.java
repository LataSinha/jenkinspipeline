package com.nagarro.miniassignment.controllerTest;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.nagarro.miniassignment.controller.UserController;
import com.nagarro.miniassignment.dto.UserResponse;
import com.nagarro.miniassignment.service.UserService;
import com.nagarro.miniassignment.utilities.exceptions.ValidationException;
import com.nagarro.miniassignment.utilities.validators.EnglishAlphabetValidator;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.*;


public class UserControllerGetUsersTest extends UserControllerTestBase {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Test
    void testGetUsersWithValidParams() {
        // Mock the UserService behavior
        List<UserResponse> mockUserResponses = createMockUserResponses();
        when(userService.getUsers(anyString(), anyString(), anyInt(), anyInt())).thenReturn(mockUserResponses);

        // Call the endpoint
        String sortType = "age";
        String sortOrder = "even";
        int limit = 3;
        int offset = 0;
        var responseEntity = userController.getUsers(sortType, sortOrder, limit, offset);

        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();

        // Verify the response
        assertEquals(200, responseEntity.getStatusCodeValue());
        assertEquals(mockUserResponses, responseBody.get("data"));
    }

    private List<UserResponse> createMockUserResponses() {
        return List.of(
                new UserResponse("John", 30, "Male", "1998-01-15", "US", "Verified"),
                new UserResponse("Alice", 23, "Female", "1993-05-22", "UK", "TO_BE_VERIFIED"),
                new UserResponse("Bob", 25, "Male", "2001-11-10", "CA", "Verified")
        );
    }

    @Test
    void testGetUsersWithInvalidSortType() {
        // Validating input parameters
        assertThrows(ValidationException.class, () -> {
            EnglishAlphabetValidator.getInstance().validate("23a");
        });

        // Call the endpoint with an invalid sortType
        ResponseEntity<Object> responseEntity = userController.getUsers("23a", "odd", 5, 0);

        // Verify the response
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody() instanceof Map);

        Map<?, ?> errorResponse = (Map<?, ?>) responseEntity.getBody();
        assertEquals(400, errorResponse.get("errorCode"));
        assertEquals("English alphabets validation failed for value: 23a", errorResponse.get("message"));
        assertNotNull(errorResponse.get("timestamp"));
    }


    @Test
    void testGetUsersWithInvalidSortOrder() {
        // Validating input parameters
        assertThrows(ValidationException.class, () -> {
            EnglishAlphabetValidator.getInstance().validate("12a");
        });

        // Call the endpoint with an invalid sortOrder
        ResponseEntity<Object> responseEntity = userController.getUsers("name", "12a", 5, 0);

        // Verify the response
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody() instanceof Map);

        Map<?, ?> errorResponse = (Map<?, ?>) responseEntity.getBody();
        assertEquals(400, errorResponse.get("errorCode"));
        assertEquals("English alphabets validation failed for value: 12a", errorResponse.get("message"));
        assertNotNull(errorResponse.get("timestamp"));
    }



}
