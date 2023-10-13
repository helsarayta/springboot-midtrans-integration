package com.heydieproject.midtransintegration.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.heydieproject.midtransintegration.model.Customer;
import com.heydieproject.midtransintegration.model.Product;
import com.heydieproject.midtransintegration.service.PaymentService;
import com.heydieproject.midtransintegration.service.WebHook;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/payment/")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;
    private final WebHook webHook;

    @GetMapping(value = "get-status", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getStatusByOrderId(@RequestParam("orderId") String orderId, @RequestParam("serverKey") String auth) throws JsonProcessingException {
        return ResponseEntity.ok(paymentService.getStatusOrderId(orderId, auth));
    }

    @PostMapping(value = "create/transfer", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createTransactionsBankTransfer(@RequestParam("amount") Integer amount,@RequestParam("bank") String bankName, @RequestParam("serverKey") String auth) throws JsonProcessingException {
        return ResponseEntity.ok(paymentService.createTransactionsBankTransfer(amount, bankName, auth));
    }
    @PostMapping(value = "create/ewallet", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createTransactionsEWallet(@RequestParam("amount") Integer amount, @RequestParam("serverKey") String auth) throws JsonProcessingException {
        return ResponseEntity.ok(paymentService.createTransactionsEWallet(amount, auth));
    }

    @PostMapping(value = "create/cc", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createCreditCardTransaction(@RequestParam("amount") Integer amount,
                                                              @RequestParam("serverKey") String authHeader,
                                                              @RequestParam("cardNumber") String cardNumber,
                                                              @RequestParam("cvv") String cvv,
                                                              @RequestParam("expMonth") String expMonth,
                                                              @RequestParam("expYear") String expYear,
                                                              @RequestParam("clientKey") String authClient) throws JsonProcessingException {
        return ResponseEntity.ok(paymentService.createCreditCardTransaction(amount, authHeader, cardNumber, cvv, expMonth, expYear, authClient));
    }

    @PostMapping(value = "create/otc", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createTransactionOverTheCounter(@RequestParam("keyServer") String authHeader,
                                                                  @RequestBody Object param
                                                                  ) throws JsonProcessingException {
        return ResponseEntity.ok(paymentService.createTransactionOverTheCounter(authHeader, param));
    }
    @PostMapping("webhook") //use ngrok and register to midtrans
    public ResponseEntity<String> retrieveData(@RequestBody String data) {
        return new ResponseEntity<>(webHook.retrieveResponse(data), HttpStatus.OK);
    }




}
