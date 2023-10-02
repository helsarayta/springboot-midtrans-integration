package com.heydieproject.midtransintegration.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
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
    public ResponseEntity<String> getStatusByOrderId(@RequestParam("orderId") String orderId) throws JsonProcessingException {
        return ResponseEntity.ok(paymentService.getStatusOrderId(orderId));
    }

    @PostMapping(value = "create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createTransactionsBankTransfer(@RequestParam("amount") Integer amount,@RequestParam("bank") String bankName) throws JsonProcessingException {
        return ResponseEntity.ok(paymentService.createTransactionsBankTransfer(amount, bankName));
    }
    @PostMapping("webhook") //use ngrok and register to midtrans
    public ResponseEntity<String> retrieveData(@RequestBody String data) {
        return new ResponseEntity<>(webHook.retrieveResponse(data), HttpStatus.OK);
    }

}