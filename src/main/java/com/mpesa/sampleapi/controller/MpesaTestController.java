package com.mpesa.sampleapi.controller;

import com.mpesa.sampleapi.config.ConfigProperties;
import com.mpesa.sampleapi.dto.StkPushApiRequest;
import io.github.openpaydev.mpesa.MpesaClient;
import io.github.openpaydev.mpesa.core.exceptions.MpesaException;
import io.github.openpaydev.mpesa.core.models.StkCallback;
import io.github.openpaydev.mpesa.core.models.StkPushRequest;
import io.github.openpaydev.mpesa.core.models.StkPushResponse;
import io.github.openpaydev.mpesa.core.models.StkStatusQueryResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
public class MpesaTestController {

    private static final Logger log = LoggerFactory.getLogger(MpesaTestController.class);

    private final MpesaClient mpesaClient;
    private final ConfigProperties configProperties;

    public MpesaTestController(MpesaClient mpesaClient, ConfigProperties configProperties) {
        this.mpesaClient = mpesaClient;
        this.configProperties = configProperties;
    }

    @PostMapping("/stkpush")
    // CORRECTED: Throws the specific MpesaException and uses the clean class name for the response.
    public ResponseEntity<StkPushResponse> stkPush(@RequestBody StkPushApiRequest apiRequest) throws MpesaException {
        // CORRECTED: Removed the typo "StkPushRequest8".
        StkPushRequest request = StkPushRequest.newPayBillRequest(
                apiRequest.getAmount(),
                apiRequest.getPhone(),
                apiRequest.getAccountReference(),
                "SDK Test Transaction",
                configProperties.getCallbackUrl()
        );

        StkPushResponse response = mpesaClient.stkPush(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/callback")
    // CORRECTED: The StkCallback type is now correctly imported.
    public ResponseEntity<String> mpesaCallback(@RequestBody StkCallback stkCallback) {
        log.info("Received M-Pesa Callback: {}", stkCallback);

        StkCallback.StkCallbackData data = stkCallback.getBody().getStkCallback();

        if (data.getResultCode() == 0) {
            log.info("✅ Payment Successful!");
            log.info("   CheckoutRequestID: {}", data.getCheckoutRequestID());

            if (data.getCallbackMetadata() != null && data.getCallbackMetadata().getItems() != null) {
                for (StkCallback.CallbackItem item : data.getCallbackMetadata().getItems()) {
                    if (item.getName() != null) {
                        log.info("   {}: {}", item.getName(), item.getValue());
                    }
                }
            }
        } else {
            log.warn("❌ Payment Failed or Canceled.");
            log.warn("   Result Code: {}", data.getResultCode());
            log.warn("   Result Description: {}", data.getResultDesc());
        }

        return ResponseEntity.ok("Callback processed successfully.");
    }

    @GetMapping("/status/{checkoutRequestID}")
    // CORRECTED: Throws the specific MpesaException.
    public ResponseEntity<StkStatusQueryResponse> getTransactionStatus(@PathVariable String checkoutRequestID) throws MpesaException {
        StkStatusQueryResponse response = mpesaClient.queryStkStatus(checkoutRequestID);
        return ResponseEntity.ok(response);
    }
}