package com.example.oli.Controller;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import com.example.oli.ModelDTO.BookingModel;
import com.example.oli.Services.BusinessEntry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.hamcrest.Matchers.not;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;


import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@WebMvcTest(controllers = BusinessController.class)
class BusinessControllerTest {


    @Autowired
    private MockMvc mockMvc;


    @MockBean
    private BusinessEntry businessEntry;

    private JacksonTester<BookingModel> jsonBookingModel;

    @BeforeEach
    public void setup()
    {
        JacksonTester.initFields(this, new ObjectMapper());

    }

    public static BookingModel createBookingModel() {
        BookingModel bookingModel = new BookingModel();
        bookingModel.setDescription("Sample description");
        bookingModel.setPrice(100.0);
        bookingModel.setCurrency("USD");
        bookingModel.setSubscription_start_date(System.currentTimeMillis());
        bookingModel.setEmail("test@example.com");
        bookingModel.setDepartment("Cooltest Department");
        return bookingModel;
    }



    @Test
    void createBookingSuccessful() throws Exception{
        BookingModel bookingModel = createBookingModel();


        ObjectMapper objectMapper = new ObjectMapper();
        String jsonContent = objectMapper.writeValueAsString(bookingModel);


        mockMvc.perform(post("/bookingservice/bookings")
                        .contentType("application/json")
                        .content(jsonContent))
                .andExpect(status().isCreated()); // Expecting successful
    }



    @Test
    void createBookingInvalidEmail() throws Exception {
        BookingModel bookingModel = createBookingModel();
        bookingModel.setEmail("notok.com"); //Set some invalid email

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonContent = objectMapper.writeValueAsString(bookingModel);

        mockMvc.perform(post("/bookingservice/bookings")
                        .contentType("application/json")
                        .content(jsonContent))
                .andExpect(status().isBadRequest()); // Expecting a bad request due to invalid email
    }


    @Test
    void createBookingNegativePrice() throws Exception {
        BookingModel bookingModel = createBookingModel();
        bookingModel.setPrice(-50.0); // Set a negative price

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonContent = objectMapper.writeValueAsString(bookingModel);

        mockMvc.perform(post("/bookingservice/bookings")
                        .contentType("application/json")
                        .content(jsonContent))
                .andExpect(status().isBadRequest()); // Expecting a bad request due to negative price
    }


    @Test
    void createBookingEmptyDescription() throws Exception {
        BookingModel bookingModel = createBookingModel();
        bookingModel.setDescription(""); // Set an empty description

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonContent = objectMapper.writeValueAsString(bookingModel);

        mockMvc.perform(post("/bookingservice/bookings")
                        .contentType("application/json")
                        .content(jsonContent))
                .andExpect(status().isBadRequest()); // Expecting a bad request due to empty description
    }


    @Test
    void createBookingNegativeSubscriptionStartDate() throws Exception {
        BookingModel bookingModel = createBookingModel();
        bookingModel.setSubscription_start_date(-System.currentTimeMillis()); // Set a negative subscription start date

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonContent = objectMapper.writeValueAsString(bookingModel);

        mockMvc.perform(post("/bookingservice/bookings")
                        .contentType("application/json")
                        .content(jsonContent))
                .andExpect(status().isBadRequest()); // Expecting a bad request due to negative subscription start date
    }

    @Test
    void updateBookingSuccessful() throws Exception {
        BookingModel updatedBookingModel = createBookingModel();
        updatedBookingModel.setDescription("Updated description"); // Update the description

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonContent = objectMapper.writeValueAsString(updatedBookingModel);

        mockMvc.perform(post("/bookingservice/bookings")
                        .contentType("application/json")
                        .content(jsonContent))
                .andExpect(status().isCreated());

        mockMvc.perform(put("/bookingservice/bookings/{booking_id}", updatedBookingModel.getBookingId())
                        .contentType("application/json")
                        .content(jsonContent))
                .andExpect(status().isCreated());
    }


    @Test
    void updateBookingCreateNewonNotExists() throws Exception {
        BookingModel updatedBookingModel = createBookingModel();
        updatedBookingModel.setDescription("Updated description"); // Update the description

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonContent = objectMapper.writeValueAsString(updatedBookingModel);

        mockMvc.perform(put("/bookingservice/bookings/{booking_id}", "abc")
                        .contentType("application/json")
                        .content(jsonContent))
                .andExpect(status().isCreated());
    }


    @Test
    void getBookingByIdSuccessfully() throws Exception{
        BookingModel bookingModel = createBookingModel();

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonContent = objectMapper.writeValueAsString(bookingModel);

        mockMvc.perform(post("/bookingservice/bookings")
                        .contentType("application/json")
                        .content(jsonContent))
                .andExpect(status().isCreated());


        given(this.businessEntry.getBookingById(bookingModel.getBookingId())).willReturn(bookingModel);

        mockMvc.perform(get("/bookingservice/bookings/{booking_id}", bookingModel.getBookingId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bookingId").value(bookingModel.getBookingId()))
                .andExpect(jsonPath("$.description").value(bookingModel.getDescription()))
                .andExpect(jsonPath("$.price").value(bookingModel.getPrice()))
                .andExpect(jsonPath("$.currency").value(bookingModel.getCurrency()))
                .andExpect(jsonPath("$.subscription_start_date").value(bookingModel.getSubscription_start_date()))
                .andExpect(jsonPath("$.email").value(bookingModel.getEmail()))
                .andExpect(jsonPath("$.department").value(bookingModel.getDepartment()));


    }



    @Test
    void getBookingByIdfailed() throws Exception{
        BookingModel bookingModel = createBookingModel();

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonContent = objectMapper.writeValueAsString(bookingModel);

        mockMvc.perform(post("/bookingservice/bookings")
                        .contentType("application/json")
                        .content(jsonContent))
                .andExpect(status().isCreated());


        given(this.businessEntry.getBookingById(bookingModel.getBookingId())).willReturn(bookingModel);

        mockMvc.perform(get("/bookingservice/bookings/{booking_id}", "Random id"))
                .andExpect(status().isNotFound());

    }



    @Test
    void getBookingIdsByDepartment() throws Exception{

        BookingModel bookingModel = createBookingModel();

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonContent = objectMapper.writeValueAsString(bookingModel);

        List<String> bookingIds = new ArrayList<>();

        mockMvc.perform(post("/bookingservice/bookings")
                        .contentType("application/json")
                        .content(jsonContent))
                .andExpect(status().isCreated());

        bookingIds.add(bookingModel.getBookingId());

        given(this.businessEntry.getBookingIdsByDepartment(bookingModel.getDepartment())).willReturn(bookingIds);

        mockMvc.perform(post("/bookingservice/bookings")
                        .contentType("application/json")
                        .content(jsonContent))
                .andExpect(status().isCreated());

        bookingIds.add(bookingModel.getBookingId());

        given(this.businessEntry.getBookingIdsByDepartment(bookingModel.getDepartment())).willReturn(bookingIds);


        // Perform the GET request with the department
        mockMvc.perform(get("/bookingservice/bookings/department/{department}", "Cooltest Department"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value(bookingIds.get(0)))
                .andExpect(jsonPath("$[1]").value(bookingIds.get(1)));

    }



    @Test
    void getUsedCurrencies() throws Exception{
        BookingModel bookingModel = createBookingModel();

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonContent = objectMapper.writeValueAsString(bookingModel);

        List<String> currencies = new ArrayList<>();

        mockMvc.perform(post("/bookingservice/bookings")
                        .contentType("application/json")
                        .content(jsonContent))
                .andExpect(status().isCreated());

        currencies.add(bookingModel.getCurrency());

        given(this.businessEntry.getUsedCurrencies()).willReturn(currencies);

        // Perform the GET request to get currencies
        mockMvc.perform(get("/bookingservice/bookings/currencies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value(currencies.get(0)));

    }

    @Test
    void getSumByCurrency() throws Exception{

        BookingModel bookingModel = createBookingModel();

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonContent = objectMapper.writeValueAsString(bookingModel);

        double sum = 0;

        mockMvc.perform(post("/bookingservice/bookings")
                        .contentType("application/json")
                        .content(jsonContent))
                .andExpect(status().isCreated());

        sum+=bookingModel.getPrice();

        given(this.businessEntry.getSumByCurrency(bookingModel.getCurrency())).willReturn(sum);

        mockMvc.perform(post("/bookingservice/bookings")
                        .contentType("application/json")
                        .content(jsonContent))
                .andExpect(status().isCreated());

        sum+=bookingModel.getPrice();

        given(this.businessEntry.getSumByCurrency(bookingModel.getCurrency())).willReturn(sum);


        // Perform the GET request to get sum of currencies
        mockMvc.perform(get("/bookingservice/sum/{currency}", bookingModel.getCurrency()))
                .andExpect(status().isOk())
                .andExpect(content().string(String.valueOf(sum)));

    }



    @Test
    void getSumByCurrencyUnsuccessful() throws Exception{

        BookingModel bookingModel = createBookingModel();

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonContent = objectMapper.writeValueAsString(bookingModel);

        double sum = 0;

        mockMvc.perform(post("/bookingservice/bookings")
                        .contentType("application/json")
                        .content(jsonContent))
                .andExpect(status().isCreated());

        sum+=bookingModel.getPrice();

        given(this.businessEntry.getSumByCurrency(bookingModel.getCurrency())).willReturn(sum);

        mockMvc.perform(post("/bookingservice/bookings")
                        .contentType("application/json")
                        .content(jsonContent))
                .andExpect(status().isCreated());

        sum+=bookingModel.getPrice();

        given(this.businessEntry.getSumByCurrency(bookingModel.getCurrency())).willReturn(sum);

        double randomSum = 100.0;

        // Perform the GET request to get sum of currencies
        mockMvc.perform(get("/bookingservice/sum/{currency}", bookingModel.getCurrency()))
                .andExpect(status().isOk())
                .andExpect(content().string(not(String.valueOf(randomSum))));

    }




}