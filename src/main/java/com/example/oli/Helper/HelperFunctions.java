package com.example.oli.Helper;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


import com.example.oli.ModelDTO.BookingModel;

public class HelperFunctions {    
    public String hashing(BookingModel bookingmodel) // Mock method for hashing and storing data
    {  
        String input = bookingmodel.getBookingId()+ bookingmodel.getDescription() + bookingmodel.getPrice()
            + bookingmodel.getCurrency() + bookingmodel.getSubscription_start_date() + bookingmodel.getEmail() + bookingmodel.getDescription();

        try {
            String sha256Hash = computeSha256Hash(input);
            return sha256Hash;
        } catch (Exception e) {
            e.printStackTrace();
            return "na";
        }
    }


    private String computeSha256Hash(String input) throws NoSuchAlgorithmException
    { // using sha256 for hashing
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        byte[] hashBytes = messageDigest.digest(input.getBytes());
        
        StringBuilder hexString = new StringBuilder();
        for (byte hashByte : hashBytes) {
            String hex = Integer.toHexString(0xff & hashByte);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }


    
    public void sendEmail(String hash, String txvalue, String subject) // Demo funciton for sending email
    {
        // mailing with given parameters
        System.out.println("Sending email subject: " + subject);
        System.out.println("hash result is: " + hash+ " transaction number is: "+ txvalue);
       
    }


}
