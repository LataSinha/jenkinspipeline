package com.nagarro.miniassignment.serviceTest;

import com.nagarro.miniassignment.dto.UserResponse;
import com.nagarro.miniassignment.entity.User;
import com.nagarro.miniassignment.repository.UserRepository;
import com.nagarro.miniassignment.service.ApiResponseExtractorService;
import com.nagarro.miniassignment.service.UserService;
import com.nagarro.miniassignment.service.VerificationCoordinatorService;
import com.nagarro.miniassignment.utilities.sortingStrategy.SortStrategy;
import com.nagarro.miniassignment.utilities.sortingStrategy.UserSorter;
import com.nagarro.miniassignment.webClientService.WebClientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class UserServiceTest {

    @Mock
    private WebClientService webClientService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ApiResponseExtractorService apiResponseExtractorService;

    @Mock
    private VerificationCoordinatorService verificationCoordinatorService;

    @Mock
    private SortStrategy sortStrategy;

    @Mock
    private UserSorter userSorter;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testGetUsers() {
        // Mock VerificationCoordinatorService response
        when(verificationCoordinatorService.coordinateVerificationWorkflow()).thenReturn("Verified");

        // Mock UserRepository response
        List<User> mockUsers = getMockUsers();
        when(userRepository.findAll()).thenReturn(mockUsers);

        // Mock UserSorter response
        when(userSorter.sortUsers(mockUsers)).thenReturn(sortedUsers());

        // Call the actual method
        List<UserResponse> result = userService.getUsers("age", "even", 2, 0);

        // Verify the result
        List<UserResponse> expected = getMockUserResponses();
        assertUserResponseListsEqual(expected, result);
    }

    private List<User> getMockUsers() {
        return Arrays.asList(
        		new User(1L,"John", 28, "Male", "1992-05-15", "US", "VERIFIED"),
                new User(2L,"Alice", 25, "Female", "1997-11-22", "UK", "TO_BE_VERIFY"),
                new User(3L,"Bob", 31, "Male", "1994-08-10", "CA", "VERIFIED")
        );
    }
    private List<User> sortedUsers() {
        return Arrays.asList(
        		new User(1L,"John", 28, "Male", "1992-05-15", "US", "VERIFIED"),
                new User(2L,"Alice", 25, "Female", "1997-11-22", "UK", "TO_BE_VERIFY"),
                new User(3L,"Bob", 31, "Male", "1994-08-10", "CA", "VERIFIED")
        );
    }

    private List<UserResponse> getMockUserResponses() {
        return Arrays.asList(
        		new UserResponse("John", 28, "Male", "1992-05-15", "US", "VERIFIED"),
        		new UserResponse("Alice", 25, "Female", "1997-11-22", "UK", "TO_BE_VERIFY")
                
        );
    }
    
    private void assertUserResponseListsEqual(List<UserResponse> expected, List<UserResponse> actual) {
        // Check if the size of the lists is the same
        assertEquals(expected.size(), actual.size(), "List sizes do not match");

        // Iterating through each element and compare individual properties
        for (int i = 0; i < expected.size(); i++) {
            UserResponse expectedResponse = expected.get(i);
            UserResponse actualResponse = actual.get(i);

            assertEquals(expectedResponse.getName(), actualResponse.getName(), "Name mismatch at index " + i);
            assertEquals(expectedResponse.getAge(), actualResponse.getAge(), "Age mismatch at index " + i);
            assertEquals(expectedResponse.getGender(), actualResponse.getGender(), "Gender mismatch at index " + i);
            assertEquals(expectedResponse.getDob(), actualResponse.getDob(), "DOB mismatch at index " + i);
            assertEquals(expectedResponse.getNationality(), actualResponse.getNationality(), "Nationality mismatch at index " + i);
            assertEquals(expectedResponse.getVerificationStatus(), actualResponse.getVerificationStatus(), "Verification status mismatch at index " + i);
        }
    }
}
