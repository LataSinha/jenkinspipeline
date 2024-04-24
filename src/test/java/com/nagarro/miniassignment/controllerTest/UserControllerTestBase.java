package com.nagarro.miniassignment.controllerTest;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.MockitoAnnotations;

public class UserControllerTestBase {

    @BeforeEach
    void initMocks() {
        MockitoAnnotations.openMocks(this);
    }
}

