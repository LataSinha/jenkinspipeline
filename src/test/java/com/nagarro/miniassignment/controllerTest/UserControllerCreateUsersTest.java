package com.nagarro.miniassignment.controllerTest;

import com.fasterxml.jackson.databind.JsonNode;

import com.nagarro.miniassignment.controller.UserController;
import com.nagarro.miniassignment.entity.User;
import com.nagarro.miniassignment.service.UserService;
import com.nagarro.miniassignment.service.VerificationService;
import com.nagarro.miniassignment.utilities.exceptions.ExternalApiException;
import com.nagarro.miniassignment.utils.TestUtils;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class UserControllerCreateUsersTest extends UserControllerTestBase {

    @Mock
    private UserService userService;
    
    @Mock
    private VerificationService verificationService;

    @InjectMocks
    private UserController userController;

    @Test
    void testCreateUsersWithValidSize() {
        // Mock the external API responses
        JsonNode api1Response = createMockApi1Response();
        JsonNode api2Response = createMockApi2Response();
        JsonNode api3Response = createMockApi3Response();

        // Mock the VerificationService behavior
        when(verificationService.verifyUser(api1Response, api2Response, api3Response)).thenReturn("VERIFIED");
        
        // Mock the UserService behavior
        List<User> mockUsers = List.of(createMockUser(), createMockUser(), createMockUser());
        when(userService.createUsers(3)).thenReturn(mockUsers);

        // Mock the request body
        Map<String, Integer> requestBody = Map.of("size", 3);

        // Call the endpoint
        ResponseEntity<?> responseEntity = userController.createUsers(requestBody);

        // Verify the response
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(mockUsers, responseEntity.getBody());
    }
    
    @Test
    void testCreateUsersWithDefaultSize() {
    	// Mock the external API responses
        JsonNode api1Response = createMockApi1Response();
        JsonNode api2Response = createMockApi2Response();
        JsonNode api3Response = createMockApi3Response();

        // Mock the VerificationService behavior
        when(verificationService.verifyUser(api1Response, api2Response, api3Response)).thenReturn("VERIFIED");
        
        // Mock the UserService behavior
        List<User> mockUsers = List.of(createMockUser());
        when(userService.createUsers(1)).thenReturn(mockUsers);


        // Call the endpoint
        ResponseEntity<?> responseEntity = userController.createUsers(null);

        // Verify the response
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(mockUsers, responseEntity.getBody());
    }

    @Test
    void testCreateUsersWithInvalidSize() {
    	// Mock the external API responses
        JsonNode api1Response = createMockApi1Response();
        JsonNode api2Response = createMockApi2Response();
        JsonNode api3Response = createMockApi3Response();

        // Mock the VerificationService behavior
        when(verificationService.verifyUser(api1Response, api2Response, api3Response)).thenReturn("VERIFIED");
        
        // Mock the UserService behavior
        List<User> mockUsers = List.of(createMockUser(), createMockUser(), createMockUser(), createMockUser(), createMockUser(),createMockUser(),createMockUser());
        when(userService.createUsers(7)).thenReturn(mockUsers);

        Map<String, Integer> requestBody = Map.of("size", 7);
        // Call the endpoint
        ResponseEntity<?> responseEntity = userController.createUsers(requestBody);

        // Verify the response
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody() instanceof Map);
 
        Map<?, ?> errorResponse = (Map<?, ?>) responseEntity.getBody();
        assertEquals(400, errorResponse.get("errorCode"));
        assertEquals("Invalid size provided", errorResponse.get("message"));
        assertNotNull(errorResponse.get("timestamp"));
    }

    @Test
    void testCreateUsersWithExternalApiError() {
    	// Mock the external API responses
        JsonNode api1Response = createMockApi1Response();
        JsonNode api2Response = createMockApi2Response();
        JsonNode api3Response = createMockApi3Response();

        // Mock the VerificationService behavior
        when(verificationService.verifyUser(api1Response, api2Response, api3Response)).thenReturn("VERIFIED");
        
        // Mock the UserService behavior
        when(userService.createUsers(3)).thenThrow(new ExternalApiException("External API error"));

        Map<String, Integer> requestBody = Map.of("size", 3);
        // Call the endpoint
        ResponseEntity<?> responseEntity = userController.createUsers(requestBody);
        // Verify the response
        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody() instanceof Map);

        Map<?, ?> errorResponse = (Map<?, ?>) responseEntity.getBody();
        assertEquals(503, errorResponse.get("errorCode"));
        assertEquals("External API not available", errorResponse.get("message"));
        assertNotNull(errorResponse.get("timestamp"));
    }

    @Test
    void testCreateUsersWithInternalServerError() {
    	// Mock the external API responses
        JsonNode api1Response = createMockApi1Response();
        JsonNode api2Response = createMockApi2Response();
        JsonNode api3Response = createMockApi3Response();

        // Mock the VerificationService behavior
        when(verificationService.verifyUser(api1Response, api2Response, api3Response)).thenReturn("VERIFIED");
        
        // Mock the UserService behavior
        when(userService.createUsers(3)).thenThrow(new RuntimeException("Unexpected Error"));

        Map<String, Integer> requestBody = Map.of("size", 3);
        // Call the endpoint
        ResponseEntity<?> responseEntity = userController.createUsers(requestBody);

        // Verify the response
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody() instanceof Map);

        Map<?, ?> errorResponse = (Map<?, ?>) responseEntity.getBody();
        assertEquals(500, errorResponse.get("errorCode"));
        assertEquals("Internal Server Error", errorResponse.get("message"));
        assertNotNull(errorResponse.get("timestamp"));
    }
    
    private JsonNode createMockApi1Response() {
        String api1ResponseString = """
                {
                    "results": [
                        {
                            "gender": "female",
                            "name": {
                                "title": "Ms",
                                "first": "Ada",
                                "last": "Patel"
                            },
                            "dob": {
                                "date": "1995-06-19T21:48:59.676Z",
                                "age": 30
                            },
                            "nat": "NZ"
                        }
                    ]
                }
                """;
        return TestUtils.convertJsonStringToObject(api1ResponseString, JsonNode.class);
    }

    private JsonNode createMockApi2Response() {
        String api2ResponseString = """
                {
                    "count": 222801,
                    "name": "Ada",
                    "country": [
                        {
                            "country_id": "SK",
                            "probability": 0.258
                        },
                        {
                            "country_id": "IT",
                            "probability": 0.07
                        },
                        {
                            "country_id": "AR",
                            "probability": 0.057
                        },
                        {
                            "country_id": "ES",
                            "probability": 0.057
                        },
                        {
                            "country_id": "NZ",
                            "probability": 0.052
                        }
                    ]
                }
                """;
        return TestUtils.convertJsonStringToObject(api2ResponseString, JsonNode.class);
    }

    private JsonNode createMockApi3Response() {
        String api3ResponseString = """
                [
                    {
                        "count": 514,
        		    	"name": "Ada",
        		    	"gender": "female",
        				"probability": 0.94
                    }
                   
                ]
                """;
        return TestUtils.convertJsonStringToObject(api3ResponseString, JsonNode.class);
    }

    private User createMockUser() {
        User user = new User();
        user.setName("Ada Patel");
        user.setAge(30);
        user.setGender("female");
        user.setDob("1995-06-19T21:48:59.676Z");
        user.setNationality("NZ");
        user.setVerificationStatus("VERIFIED");
        return user;
    }
}