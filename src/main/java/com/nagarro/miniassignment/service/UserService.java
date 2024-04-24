package com.nagarro.miniassignment.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.JsonNode;
import com.nagarro.miniassignment.dto.UserResponse;
import com.nagarro.miniassignment.entity.User;
import com.nagarro.miniassignment.repository.UserRepository;
import com.nagarro.miniassignment.utilities.exceptions.ExternalApiException;
import com.nagarro.miniassignment.utilities.exceptions.InvalidSizeException;
import com.nagarro.miniassignment.utilities.exceptions.ValidationException;
import com.nagarro.miniassignment.utilities.sortingStrategy.EvenSortStrategy;
import com.nagarro.miniassignment.utilities.sortingStrategy.OddSortStrategy;
import com.nagarro.miniassignment.utilities.sortingStrategy.SortStrategy;
import com.nagarro.miniassignment.utilities.sortingStrategy.UserSorter;
import com.nagarro.miniassignment.webClientService.WebClientService;

import org.springframework.stereotype.Service;

@Service
public class UserService {

	private final WebClientService webClientService;
	private final UserRepository userRepository;
	private final ApiResponseExtractorService apiResponseExtractorService;
	private final VerificationCoordinatorService verificationCoordinatorService;

	public UserService(WebClientService webClientService, UserRepository userRepository,
			ApiResponseExtractorService apiResponseExtractorService,
			VerificationCoordinatorService verificationCoordinatorService) {
		this.webClientService = webClientService;
		this.userRepository = userRepository;
		this.apiResponseExtractorService = apiResponseExtractorService;
		this.verificationCoordinatorService = verificationCoordinatorService;
	}

	public List<User> createUsers(int size) {
	    if (size <= 0 || size > 5) {
	        throw new InvalidSizeException("Size should be greater than zero and less than 5.");
	    }

	    List<User> userList = new ArrayList<>();

	    // Create the specified number of users
	    for (int i = 0; i < size; i++) {
	        try {
	            // Get responses from WebClientService
	        	webClientService.executeParallelApiCalls();
	            JsonNode api1Response = webClientService.getApi1Response();

	            // Creating a user based on the API responses
	            User user = createUser(api1Response);

	            // Adding the user to the list
	            userList.add(user);
	        } catch (ExternalApiException e) {
	            throw e;
	        }
	    }
	    return userList;
	}

	public User createUser(JsonNode api1Response) {
		// Extracting user data from the API response
		Optional<Map<String, Object>> userData = apiResponseExtractorService.extractUserData(api1Response);

		// If user data is present, save it to the database
		if (userData.isPresent()) {
			User user = mapToUser(userData.get());

			// Verify user based on extracted data
			String verificationStatus = verificationCoordinatorService.coordinateVerificationWorkflow();
			user.setVerificationStatus(verificationStatus);

			// Save user to the database
			return userRepository.save(user);			
		}

		return null;
	}

	private User mapToUser(Map<String, Object> userData) {
		User user = new User();
		user.setName(userData.get("firstName") + " " + userData.get("lastName"));
		user.setAge((int) userData.get("age"));
		user.setGender((String) userData.get("gender"));
		user.setDob((String) userData.get("dob"));
		user.setNationality((String) userData.get("nationality"));
		user.setDateCreated(LocalDateTime.now());
		user.setDateModified(LocalDateTime.now());
		return user;
	}

	public List<UserResponse> getUsers(String sortType, String sortOrder, Integer limit, Integer offset) {
		SortStrategy sortStrategy = getSortStrategy(sortType, sortOrder);
		List<User> users = userRepository.findAll();

		// Apply limit and offset
		int endIndex = Math.min(offset + limit, users.size());
		users = users.subList(offset, endIndex);
		// Sort the users using the UserSorter
		UserSorter userSorter = new UserSorter(sortStrategy);
		users = userSorter.sortUsers(users);
		
		List<UserResponse> userResponseDTO = users.stream()
                .map(user -> {
                    UserResponse dto = new UserResponse();
                    dto.setName(user.getName());
                    dto.setAge(user.getAge());
                    dto.setGender(user.getGender());
                    dto.setDob(user.getDob());
                    dto.setNationality(user.getNationality());
                    dto.setVerificationStatus(user.getVerificationStatus());
                    return dto;
                })
                .collect(Collectors.toList());
		
		return userResponseDTO;
	}

	private SortStrategy getSortStrategy(String sortType, String sortOrder) {
		try {
			switch (sortOrder.toLowerCase()) {
			case "even":
				return new EvenSortStrategy(sortType);
			case "odd":
				return new OddSortStrategy(sortType);
			default:
				throw new IllegalArgumentException("Unsupported sortOrder: " + sortOrder);
			}
		} catch (IllegalArgumentException iae) {
			throw new ValidationException("Invalid sortType or sortOrder");
		}
	}

	public Map<String, Object> getPageInfo(Integer limit, Integer offset) {
		try {
			long total = userRepository.getTotalCount();
			boolean hasNextPage = offset + limit < total;
			boolean hasPreviousPage = offset > 0;

			// Create a Map to represent PageInfo
			Map<String, Object> pageInfo = new HashMap<>();
			pageInfo.put("hasNextPage", hasNextPage);
			pageInfo.put("hasPreviousPage", hasPreviousPage);
			pageInfo.put("total", total);

			return pageInfo;
		} catch (Exception e) {
			throw new RuntimeException("Error calculating PageInfo", e);
		}
	}
}