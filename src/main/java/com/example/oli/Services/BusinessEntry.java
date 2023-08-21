package com.example.oli.Services;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import com.example.oli.Web3.Testnet;
import org.springframework.stereotype.Component;

import com.example.oli.ModelDTO.BookingModel;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;

@Component
public class BusinessEntry {
    private Map<String, BookingModel> bookings; // Map with (booking_id => booking) mapping

    public BusinessEntry() {
        bookings = new HashMap<>();
    }

    public void addBooking(BookingModel booking) // POST
    { 
        bookings.put(booking.getBookingId(), booking);
    }

    public void updateBooking(String bookingId, BookingModel updatedBooking) // PUT
    {        
            bookings.put(bookingId, updatedBooking);
        
    }

    public BookingModel getBookingById(String bookingId) //GET booking by booking_id
    {
        return bookings.getOrDefault(bookingId, null);
    }



    public List<String> getBookingIdsByDepartment(String department) //GET booking by department name
    {
        List<String> departmentBookings = new ArrayList<>();

        for (BookingModel booking : bookings.values()) 
        {
            if (booking.getDepartment().equals(department.toLowerCase()) || booking.getDepartment().equals(department.toUpperCase())) 
            {
                departmentBookings.add(booking.getBookingId());
            }
        }
        return departmentBookings;
    }


    public List<String> getUsedCurrencies() //GET booking by currency
    {
        Set<String> uniqueCurrencies = new HashSet<>(); //set to remove duplicate currencies

        for (BookingModel booking : bookings.values()) 
        {
            uniqueCurrencies.add(booking.getCurrency());
        }

        return new ArrayList<>(uniqueCurrencies);
    }


    public Double getSumByCurrency(String currency) //GET sum of all bookings for a currency
    {
        double sum = 0.0;

        for (BookingModel booking : bookings.values()) 
        {
            if (booking.getCurrency().equals(currency.toLowerCase()) || booking.getCurrency().equals(currency.toUpperCase())) 
            {
                sum += booking.getPrice();
            }
        }
        return sum;
    }


    public String getHash(String transactionHash) throws IOException
    {
        Testnet testnet = new Testnet();

        return testnet.getHash(transactionHash);
    }


    public String getDepartmentByBookingId(String bookingid)
    {
        for (BookingModel booking : bookings.values()) 
        {
            if (booking.getBookingId().equals(bookingid))
            {
                return booking.getDepartment();
            }
        }

        return "na";
    }


}
