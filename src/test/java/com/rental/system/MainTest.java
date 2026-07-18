package com.rental.system;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rental.system.controller.RentalController;
import com.rental.system.controller.StaffController;
import com.rental.system.model.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.test.context.ActiveProfiles;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@SuppressWarnings("null")
public class MainTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    private String obtainAccessToken() throws Exception {
        StaffController.LoginRequest request = new StaffController.LoginRequest();
        request.setUsername("root_admin");
        request.setPassword("Root@123");

        String responseJson = mockMvc.perform(post("/api/staffs/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        return com.jayway.jsonpath.JsonPath.read(responseJson, "$.token");
    }

    @Test
    public void testGetVehicles() throws Exception {
        String token = obtainAccessToken();
        mockMvc.perform(get("/api/vehicles")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThan(0))));
    }

    @Test
    public void testGetCustomers() throws Exception {
        String token = obtainAccessToken();
        mockMvc.perform(get("/api/customers")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThan(0))));
    }

    @Test
    public void testStaffLoginSuccess() throws Exception {
        StaffController.LoginRequest request = new StaffController.LoginRequest();
        request.setUsername("root_admin");
        request.setPassword("Root@123");

        mockMvc.perform(post("/api/staffs/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Login success")));
    }

    @Test
    public void testStaffLoginFailure() throws Exception {
        StaffController.LoginRequest request = new StaffController.LoginRequest();
        request.setUsername("root_admin");
        request.setPassword("Root@123_wrong");

        mockMvc.perform(post("/api/staffs/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("failed")));
    }

    @Test
    public void testRentalTransactionFlow() throws Exception {
        String token = obtainAccessToken();

        // 1. Create a customer
        Customer customer = new Customer("Test Customer", "ID" + System.currentTimeMillis(), "099" + (System.currentTimeMillis() % 1000000));
        customer.setEmail("test" + System.currentTimeMillis() + "@example.com");
        customer.setPassword("password123");
        String customerJson = mockMvc.perform(post("/api/customers/register")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customer)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Customer savedCustomer = objectMapper.readValue(customerJson, Customer.class);

        // 2. Perform rental
        RentalController.RentRequest rentRequest = new RentalController.RentRequest();
        rentRequest.setVehicleId(1); // Ford Escape seeded on startup
        rentRequest.setCustomerId(savedCustomer.getCustomerId());
        rentRequest.setStaffId(1); // admin_root
        rentRequest.setStaffUsername("root_admin");
        rentRequest.setRentDays(5);
        rentRequest.setStartDate("2026-06-05");
        rentRequest.setEndDate("2026-06-10");
        rentRequest.setDeposit(100.0);

        mockMvc.perform(post("/api/rentals")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(rentRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.rentDays").value(5));

        // 3. Return vehicle
        RentalController.ReturnRequest returnRequest = new RentalController.ReturnRequest();
        returnRequest.setPayDate("2026-06-10");
        returnRequest.setPaymentMethod("ABA");
        returnRequest.setDiscount(10.0);
        returnRequest.setDamageFee(25.0);

        mockMvc.perform(post("/api/rentals/1/return")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(returnRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(false))
                .andExpect(jsonPath("$.payment.status").value("PAID"));
    }
}
