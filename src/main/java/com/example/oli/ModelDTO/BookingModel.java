package com.example.oli.ModelDTO;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.UUID;



public class BookingModel {

    private String bookingId;

    @NotNull(message = "Description is mandatory")
    @NotBlank(message = "Description cannot be empty")
    private String description;

    @NotNull(message = "Price is mandatory")
    @Positive(message = "Price must be a positive value")
    private double price;

    @NotNull(message = "Currency is mandatory")
    @NotBlank(message = "Currency cannot be empty")
    private String currency;

    @NotNull(message = "Subscription start date is mandatory")
    @Positive(message = "Subscription start date must be a positive value")
    private long subscription_start_date;

    @NotNull(message = "Email is mandatory")
    @NotEmpty(message = "Email cannot be empty")
    @Email(message = "Invalid email format")
    private String email;

    @NotNull(message = "Department is mandatory")
    @NotBlank(message = "Department cannot be empty")
    private String department;

    
    public BookingModel() {
        this.bookingId = UUID.randomUUID().toString();
    }
    

    public String getBookingId() {
        return bookingId;
    }
    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }


    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }


    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }


    public String getCurrency() {
        return currency;
    }
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    
    public long getSubscription_start_date() {
        return subscription_start_date;
    }


    public void setSubscription_start_date(long subscription_start_date) {
        this.subscription_start_date = subscription_start_date;
    }



    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }


    public String getDepartment() {
        return department;
    }
    public void setDepartment(String department) {
        this.department = department;
    }

    
    
    
}
