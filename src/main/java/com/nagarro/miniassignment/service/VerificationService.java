package com.nagarro.miniassignment.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;

@Service
public class VerificationService {

    public String verifyUser(JsonNode api1Response, JsonNode api2Response, JsonNode api3Response) {
        // Extract relevant information from the API1 responses
        String nationalityFromAPI1 = api1Response.get("results").get(0).get("nat").asText();
        String genderFromAPI1 = api1Response.get("results").get(0).get("gender").asText();

        // Extract relevant information from the API 2 and API 3 responses
        List<String> nationalitiesFromAPI2 = ApiResponseExtractorService.extractNationalities(api2Response);
        String genderFromAPI3 = ApiResponseExtractorService.extractGender(api3Response);

        // Verification criteria
        boolean isNationalityMatch = nationalitiesFromAPI2.contains(nationalityFromAPI1);
        boolean isGenderMatch = genderFromAPI1.equalsIgnoreCase(genderFromAPI3);

        // Verification status
        if (isNationalityMatch && isGenderMatch) {
            return "VERIFIED";
        } else {
            return "TO_BE_VERIFIED";
        }
    }
}
