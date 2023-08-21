package com.example.oli.Controller;

import com.example.oli.Helper.HandleDepartments;
import com.example.oli.Helper.HelperFunctions;
import com.example.oli.ModelDTO.BookingModel;
import com.example.oli.Services.BusinessEntry;
import com.example.oli.Web3.Testnet;

import jakarta.validation.Valid;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;


import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@ComponentScan
@RestController
@RequestMapping("/bookingservice")
public class BusinessController {


    private BusinessEntry businessEntry;
    private Map<String, String> txToHashMapping;

    private List<String> departments;
    private List<List<String>> hierarchy_departments;


    public BusinessController(BusinessEntry businessEntry) {
        this.businessEntry = businessEntry;
        this.txToHashMapping = new HashMap<>();
        this.departments = new ArrayList<>();
        this.hierarchy_departments = new ArrayList<>();
    }


    private void adddepartments(String dept)
    {
        departments.add(dept);
    }


    @GetMapping("/")
    public String dashboard()
    {
        return "Web3 Booking v:1.1";
    }

    @PostMapping("/bookings")
    public ResponseEntity<String> createBooking( @RequestBody @Valid BookingModel bookingmodel) 
    {
        businessEntry.addBooking(bookingmodel);       

        // Code to create hash data and put it into the registry on chain
        HelperFunctions helperFunctions = new HelperFunctions();

        String hashvalue = helperFunctions.hashing(bookingmodel);
        
        Testnet testnet = new Testnet(hashvalue);
        String tx  =testnet.sendhash();
        txToHashMapping.put(tx, hashvalue);

        // Mimic sending email
        helperFunctions.sendEmail(hashvalue, tx, "create");

        adddepartments(bookingmodel.getDepartment());

        String responseMessage = String.format("Booking created successfully with id %s", bookingmodel.getBookingId());
        return ResponseEntity.status(HttpStatus.CREATED).body(responseMessage);    
    }


    @PutMapping("/bookings/{booking_id}")
    public ResponseEntity<String> updateBooking(@PathVariable("booking_id") String bookingId, @RequestBody BookingModel updatedBooking) 
    {
        BookingModel existingBooking = businessEntry.getBookingById(bookingId); //get existing booking

        HelperFunctions helperFunctions = new HelperFunctions();
        

        if (existingBooking == null) //create new if existing booking does not exist
        {
            businessEntry.addBooking(updatedBooking);
            String responseMessage = String.format("Booking created successfully with id %s", updatedBooking.getBookingId());

            // Code to create hash data and put it into the registry on chain
            String hashvalue = helperFunctions.hashing(updatedBooking);

            Testnet testnet = new Testnet(hashvalue);
            String tx = testnet.sendhash();
            txToHashMapping.put(tx, hashvalue);

            adddepartments(updatedBooking.getDepartment());
            // Mimic sending email
            helperFunctions.sendEmail(hashvalue, tx, "create on not exist");
            return ResponseEntity.status(HttpStatus.CREATED).body(responseMessage); 
        }

        // Update booking details if exists
        existingBooking.setDescription(updatedBooking.getDescription());
        existingBooking.setPrice(updatedBooking.getPrice());
        existingBooking.setCurrency(updatedBooking.getCurrency());
        existingBooking.setSubscription_start_date(updatedBooking.getSubscription_start_date());
        existingBooking.setEmail(updatedBooking.getEmail());
        existingBooking.setDepartment(updatedBooking.getDepartment());

        // Update the booking in the registry
        businessEntry.updateBooking(bookingId, existingBooking);
       

        // Code to create hash data and put it into the registry on chain
        String hashvalue = helperFunctions.hashing(existingBooking);

        Testnet testnet = new Testnet(hashvalue);
        String tx = testnet.sendhash();
        txToHashMapping.put(tx, hashvalue);

        // Mimic sending email
        helperFunctions.sendEmail(hashvalue, tx, "update");

        adddepartments(existingBooking.getDepartment());
        String responseMessage = String.format("Booking updated successfully with id %s", bookingId);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseMessage);
    }



    @GetMapping("/bookings/{booking_id}")
    public ResponseEntity<BookingModel> getBookingById(@PathVariable("booking_id") String bookingId) 
    {
        BookingModel bookingmodel = businessEntry.getBookingById(bookingId);

        if (bookingmodel == null) 
        {          
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(bookingmodel);
            
        }

        return ResponseEntity.ok(bookingmodel);
    }


    @GetMapping("/bookings/department/{department}")
    public ResponseEntity<List<String>> getBookingIdsByDepartment(@PathVariable("department") String department) {
        List<String> bookingIds = businessEntry.getBookingIdsByDepartment(department);

        if (bookingIds.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(bookingIds);
        }

        return ResponseEntity.ok(bookingIds);
    }


    @GetMapping("/bookings/currencies")
    public ResponseEntity<List<String>> getUsedCurrencies() 
    {
        List<String> currencies = businessEntry.getUsedCurrencies();

        if (currencies.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(currencies);
        }

        return ResponseEntity.ok(currencies);
    }


    @GetMapping("/sum/{currency}")
    public ResponseEntity<Double> getSumByCurrency(@PathVariable("currency") String currency) 
    {
        Double sum = businessEntry.getSumByCurrency(currency);

        if (sum == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(sum);
        }

        return ResponseEntity.ok(sum);
    }


    @GetMapping("/bookings/proof/{transaction_id}")
    public ResponseEntity<List<Object>> getBookingProof(@PathVariable("transaction_id") String transactionId)
    {
        String hashvalue = txToHashMapping.get(transactionId);
        boolean check = false;

        if (hashvalue == null) {
            List<Object> result = new ArrayList<>();
            result.add(hashvalue);
            result.add(check);

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        }
        try {
            String hash = businessEntry.getHash(transactionId);

            if (hash.substring(2).equals(hashvalue))
            {
                check = true;
            }
        }
        catch (Exception e)
        {
            List<Object> result = new ArrayList<>();
            result.add(hashvalue);
            result.add(check);

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
        List<Object> result = new ArrayList<>();
        result.add(hashvalue);
        result.add(check);

        return ResponseEntity.ok(result);
    }


    @GetMapping("/bookingservice/departments/{booking_id}")
    public ResponseEntity<List<Object>> getDepartmentByBookingId(@PathVariable("booking_id") String bookingId)
    {
        String department = businessEntry.getDepartmentByBookingId(bookingId);

        List<Object> result =  new ArrayList<>();

        if (department.equals("na"))
        {

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        }
        HandleDepartments handleDepartments = new HandleDepartments(departments, hierarchy_departments);

        result = handleDepartments.doBusiness(department);


        return ResponseEntity.ok(result);
    }

   
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationException(MethodArgumentNotValidException ex)
    {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach((error) ->
        {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();

            errors.put(fieldName, errorMessage);
        }
        );

        return errors;
    }
    
}
