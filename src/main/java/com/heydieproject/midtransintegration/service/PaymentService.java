package com.heydieproject.midtransintegration.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {

    private final RestTemplate restTemplate;
    @Value("${url-base-sandbox}")
    public String urlBaseSandbox;
    @Value("${url-create-transactions}")
    public String urlCreateTransactions;

    public String getStatusOrderId(String orderId, String authHeader) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization",Base64.getEncoder().encodeToString(authHeader.getBytes(StandardCharsets.UTF_8)));
        Map<String, Object> body = restTemplate.exchange(urlBaseSandbox + orderId + "/status", HttpMethod.GET, new HttpEntity<>(headers), Map.class).getBody();

        log.info("Response ==> {}",body);
        return new ObjectMapper().writeValueAsString(body);
    }

    public String createTransactionsBankTransfer(Integer amount, String bankName, String authHeader) throws JsonProcessingException {
        Map<String, Object> charge = new HashMap<>();

        Map<String, Object> transactionDetail = new HashMap<>();
        transactionDetail.put("order_id", generateOrderId());
        transactionDetail.put("gross_amount", amount);
        Map<String, Object> bankTransfer = new HashMap<>();
        bankTransfer.put("bank", bankName);
        Map<String, Object> customExpired = new HashMap<>();
        customExpired.put("expiry_duration", 5);
        customExpired.put("unit", "minute");

        charge.put("payment_type","bank_transfer");
        charge.put("transaction_details", transactionDetail);
        charge.put("bank_transfer", bankTransfer);
        charge.put("custom_expiry", customExpired);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", Base64.getEncoder().encodeToString(authHeader.getBytes(StandardCharsets.UTF_8)));
        Map<String, Object> body = restTemplate.exchange(urlCreateTransactions, HttpMethod.POST, new HttpEntity<>(charge,headers), Map.class).getBody();

        return new ObjectMapper().writeValueAsString(body);
    }

    public String createTransactionsEWallet(Integer amount, String authHeader) throws JsonProcessingException {
        Map<String, Object> charge = new HashMap<>();

        Map<String, Object> transactionDetail = new HashMap<>();
        transactionDetail.put("order_id", generateOrderId());
        transactionDetail.put("gross_amount", amount);

        Map<String, Object> customExpired = new HashMap<>();
        customExpired.put("expiry_duration", 5);
        customExpired.put("unit", "minute");

        charge.put("payment_type","gopay");
        charge.put("transaction_details", transactionDetail);
        charge.put("custom_expiry", customExpired);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", Base64.getEncoder().encodeToString(authHeader.getBytes(StandardCharsets.UTF_8)));
        Map<String, Object> body = restTemplate.exchange(urlCreateTransactions, HttpMethod.POST, new HttpEntity<>(charge,headers), Map.class).getBody();

        return new ObjectMapper().writeValueAsString(body);
    }

    private String generateOrderId() {
        String order = "order-";
        int randomNumber = new Random().nextInt(999999);
        return order+randomNumber;
    }

    public void setExpirePayment(String orderId, String authHeader) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization",Base64.getEncoder().encodeToString(authHeader.getBytes(StandardCharsets.UTF_8)));
        restTemplate.exchange(urlBaseSandbox+orderId+"/expire", HttpMethod.POST, new HttpEntity<>(headers), Map.class).getBody();
    }

}
