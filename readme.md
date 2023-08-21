
## Project Goal:
This project aims to create a RESTful web service using Spring Boot to address the challenges faced by the Sales team. The service will efficiently manage booking objects, store them in memory, and provide relevant information to enhance customer interactions and product acquisition.

Preamble:
As a crucial part of the multidisciplinary development elite team, this software solution is being developed to tackle the issues encountered by the Sales team. The primary requirement is to implement a RESTful web service capable of storing and retrieving booking information. A booking represents a customer's request to obtain one of the offered products.

The implementation is based on the Spring Boot framework, version 3.0.2.

In the next sections we will talk about different classes we implemented.

## Classes

### BookingModel

The BookingModel class represents a booking request from a customer to acquire a product. This class contains various attributes that capture important information about the booking. Additionally, it enforces data validation rules using Java's Bean Validation annotations to ensure the integrity of the data.

#### Attributes and Data Validation
1. bookingId: A unique identifier generated as a UUID when a new booking instance is created.

2. description: A mandatory field that provides a description of the booking request. It cannot be blank and must have a non-null value.

3. price: A mandatory positive value indicating the price associated with the booking request. It must be a numeric value greater than zero.

4. currency: A mandatory field specifying the currency in which the price is expressed. It cannot be blank and must have a non-null value.

5. subscription_start_date: A mandatory positive value indicating the start date of the subscription associated with the booking request. 

6. email: A mandatory field that holds the customer's email address. It must be a valid email format and cannot be empty or null.

7. department: A mandatory field indicating the department related to the booking request. It cannot be blank and must have a non-null value.


### BusinessEntry
The BusinessEntry class is a core component of the project that handles various business logic operations related to bookings. It encapsulates methods to manage and retrieve booking information, compute statistics, and interact with a blockchain network for hash retrieval. 


#### Methods
###### addBooking(BookingModel booking)
Adds a new booking to the system.

###### updateBooking(String bookingId, BookingModel updatedBooking)
Updates the information of an existing booking.

###### getBookingById(String bookingId)
Retrieves booking details by a given booking ID.

###### getBookingIdsByDepartment(String department)
Retrieves a list of booking IDs associated with a specific department.

###### getUsedCurrencies()
Retrieves a list of unique currencies used in the bookings.

###### getSumByCurrency(String currency)
Calculates and returns the sum of all bookings' prices in a given currency.

###### getHash(String transactionHash)
Retrieves the hash associated with a transaction hash from the blockchain network.

###### getDepartmentByBookingId(String bookingId)
Retrieves the department associated with a booking by its ID. Based on the department, we execute to doBusiness() code.


### Business Controller
The BusinessController class is responsible for handling various HTTP endpoints related to bookings and business operations. It interacts with the BusinessEntry service.

#### Endpoints

###### Dashboard Endpoint
URL: /bookingservice/ (HTTP Method: GET)

Description: Returns a simple greeting message indicating the version of the web service.

###### Create Booking Endpoint
URL: /bookingservice/bookings (HTTP Method: POST)

Description: Creates a new booking using the provided booking details. Generates a unique booking ID, calculates a hash value, and simulates sending an email. Returns a response indicating the success or failure of the creation.


###### Update Booking Endpoint 
URL: /bookingservice/bookings/{booking_id} (HTTP Method: PUT)

Description: Updates an existing booking with new details. Calculates a hash value, updates the booking in the registry, and simulates sending an email. If bookingId does not exist, then create a new booking.


###### Get Booking by ID Endpoint 
URL: /bookingservice/bookings/{booking_id} (HTTP Method: GET)

Description: Retrieves booking details by its ID. Returns the booking details if found, or a NOT FOUND response.


###### Get Booking IDs by Department Endpoint
URL: /bookingservice/bookings/department/{department} (HTTP Method: GET)

Description: Retrieves a list of booking IDs associated with a specific department. Returns the list of IDs if found, or a NOT FOUND response.


###### Get Used Currencies Endpoint
URL: /bookingservice/bookings/currencies (HTTP Method: GET)

Description: Retrieves a list of unique currency codes used in bookings. Returns the list of currencies if found, or a NOT FOUND response.

###### Get Sum by Currency Endpoint
URL: /bookingservice/sum/{currency} (HTTP Method: GET)

Description: Calculates the sum of all booking prices in a specific currency. Returns the sum if bookings exist in that currency, or a NOT FOUND response.


###### Get Booking Proof Endpoint
URL: /bookingservice/bookings/proof/{transaction_id} (HTTP Method: GET)

Description: Retrieves booking proof by matching transaction ID with hash value. Returns proof details if found, or a NOT FOUND or BAD REQUEST response based on validation results. Also checks (using getHash()) if the hash value is same as the hash retrieved from the testnet.


###### Get Department by Booking ID Endpoint
URL: /bookingservice/departments/{booking_id} (HTTP Method: GET)

Description: Retrieves department details associated with a booking ID. Returns department information if found, or a NOT FOUND response. Use doBusiness() based on the received department.


## Connecting with blockchain Testnetwork
The blockchain connectivity in this project is established through the Ethereum Goerli test network using Infura endpoints.

### Getting Hash from Transaction
To retrieve a hash from a specific transaction on the blockchain, the method getHash(String transactionHash) is used. 

Here, if the transaction and receipt exist, extract the hash from the transaction's input data. Return the extracted hash or "na" if no hash data is found.

### Sending Hash to the Blockchain
To send a hash to the blockchain and obtain a transaction hash, the method sendhash() is used. Here we have hardcoded the parameters such as gas price, gas limit, recipient address, and value.

Transaction manager is created to handle transaction signing.

## doBusiness
The doBusiness method takes the retrieved department based on bookingId and do 2 things:
1. Creates an Hierarchy of the departments.
2. For the given department, gives an statistical analysis (in percentage) of how many bookings are based on this department.

For example, this is one output for doBusiness for department "warm123 dept":

[
    25,
    [
        [
            "admin"
        ],
        [
            "cooler dept",
            "cool dept"
        ],
        [
            "warm123 dept"
        ]
    ]
]

## Testing
We have implemented unit testing for:
1. To check if booking successful
2. Booking failed for different reasons (e.g. - invalid email)
3. Update booking
4. Get booking by id
5. Get booking id by department
6. Get used currencies
7. Get sum of price based on currencies

## Recommendation for the next step
1. Implement authentication system
2. Sorting and pagination for a scalable solution
3. Implement frontend and database
4. Implement with Ethereum mainnet
5. Deployment with Docker/ cloud platforms such as AWS
6. Security reviewing

## Time required
Code: 7 hours. Testing: 2 hours. Documentation: 2 hours