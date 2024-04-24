package com.nagarro.miniassignment.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.nagarro.miniassignment.webClientService.WebClientService;

@Service
public class VerificationCoordinatorService {

    private final WebClientService webClientService;
    private final VerificationService verificationService;

    @Autowired
    public VerificationCoordinatorService(WebClientService webClientService, VerificationService verificationService) {
        this.webClientService = webClientService;
        this.verificationService = verificationService;
    }

    public String coordinateVerificationWorkflow() {
        // Call WebClientService to get API responses
        JsonNode api1Response = webClientService.getApi1Response();
        JsonNode api2Response = webClientService.getApi2Response();
        JsonNode api3Response = webClientService.getApi3Response();

        // Pass API responses to VerificationService
        String verificationStatus = verificationService.verifyUser(api1Response, api2Response, api3Response);
        System.out.println("User Verification Status: " + verificationStatus);
        return verificationStatus;
    }
}

