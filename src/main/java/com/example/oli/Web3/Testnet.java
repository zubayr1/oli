package com.example.oli.Web3;


import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.RawTransactionManager;
import org.web3j.utils.Convert;
import java.io.IOException;
import org.web3j.protocol.core.methods.response.Transaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.math.BigDecimal;
import java.math.BigInteger;

import io.github.cdimascio.dotenv.Dotenv;

public class Testnet {
    private String hash;

    public Testnet()
    {

    }
    public Testnet(String hash)
    {   
        this.hash = hash;
    }


    public String getHash(String transactionHash) throws IOException {
        Dotenv dotenv = Dotenv.configure().load();

        Web3j web3j = Web3j.build(new HttpService(dotenv.get("INFURA")));

        Transaction transaction = web3j.ethGetTransactionByHash(transactionHash).send().getTransaction().orElse(null);

        if (transaction != null) {
            TransactionReceipt receipt = web3j.ethGetTransactionReceipt(transactionHash).send().getTransactionReceipt().orElse(null);

            if (receipt != null) {
                String inputData = transaction.getInput();
                return inputData;  // This is the hash stored in the transaction data
            }
        }

        return "na";  // Transaction not found or no hash data
    }

    public String sendhash()  {
        Dotenv dotenv = Dotenv.configure().load();

        Web3j web3j = Web3j.build(new HttpService(dotenv.get("INFURA")));
        
        try{
            String privateKey = dotenv.get("PRIVATE_KEY");

            Credentials credentials = Credentials.create(privateKey);


            String address = dotenv.get("ADDRESS");


            // Create a transaction manager that signs and sends transactions
            RawTransactionManager txManager = new RawTransactionManager(web3j, credentials);

            // Convert your int values to BigInteger values
            BigInteger gasPrice = BigInteger.valueOf(50);
            BigInteger gasLimit = BigInteger.valueOf(210000);       

            // Convert your double value to a wei value
            BigInteger value = Convert.toWei(BigDecimal.valueOf(0), Convert.Unit.ETHER).toBigInteger();

            // Send data to the blockchain using the txManager
            EthSendTransaction ethSendTransaction = txManager.sendTransaction(gasPrice, 
            gasLimit, address, hash, value);

            // Get the transaction hash from the response
            String txHash = ethSendTransaction.getTransactionHash();

            return txHash;
        
        }
        catch(Exception e)
        {
            return "na";
        }
    
    }
}
