package com.nagarro.miniassignment.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;

@Service
public class ApiResponseExtractorService {
	
	public static Optional<Map<String, Object>> extractUserData(JsonNode apiResponse) {
        JsonNode resultsArray = apiResponse.get("results");
        if (resultsArray.isArray() && resultsArray.size() > 0) {
            JsonNode firstUser = resultsArray.get(0);

            // Extracting relevant fields
            String firstName = firstUser.get("name").get("first").asText();
            String lastName = firstUser.get("name").get("last").asText();
            int age = firstUser.get("dob").get("age").asInt();
            String gender = firstUser.get("gender").asText();
            String dob = firstUser.get("dob").get("date").asText();
            String nationality = firstUser.get("nat").asText();

            Map<String, Object> userData = new HashMap<>();
            userData.put("firstName", firstName);
            userData.put("lastName", lastName);
            userData.put("age", age);
            userData.put("gender", gender);
            userData.put("dob", dob);
            userData.put("nationality", nationality);

            return Optional.of(userData);
        }
        // Return an empty optional if no results are found
        return Optional.empty();
    }


	 public static List<String> extractNationalities(JsonNode api2Response) {
	        List<String> nationalities = new ArrayList<>();
	        JsonNode countryArray = api2Response.get("country");
	        if (countryArray.isArray()) {
	            for (JsonNode countryNode : countryArray) {
	                nationalities.add(countryNode.get("country_id").asText());
	            }
	        }
	        return nationalities;
	    }

    public static String extractGender(JsonNode api3Response) {
        return api3Response.get("gender").asText();
    }
}

