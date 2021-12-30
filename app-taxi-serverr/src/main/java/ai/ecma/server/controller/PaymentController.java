package ai.ecma.server.controller;

import ai.ecma.server.entity.User;
import ai.ecma.server.payload.PaymentDto;
import ai.ecma.server.payload.Result;
import ai.ecma.server.security.CurrentUser;
import ai.ecma.server.service.PaymentService;
import ai.ecma.server.utils.AppConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;

/**
 * BY BAXROMJON on 05.11.2020
 */

@RestController
@RequestMapping("/api/payment")
public class PaymentController {
    @Autowired
    PaymentService paymentService;


    @PreAuthorize(value = "hasAnyRole('ROLE_PAYNET','ROLE_PAYME','ROLE_OSON','ROLE_UPAY','ROLE_CLICK')")
    @PostMapping
    public HttpEntity<?> addPayment(@RequestBody PaymentDto paymentDto, @CurrentUser User user) {
        Result result = paymentService.addPayment(paymentDto, user);
        return ResponseEntity.status(result.isSuccess() ? 201 : 409).body(result);
    }

    @PreAuthorize(value = "hasAnyRole('ROLE_SUPER_ADMIN')")
    @GetMapping
    public HttpEntity<?> getPayments(@RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                     @RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size,
                                     @RequestParam(defaultValue = AppConstants.SORT_TYPE_ASC) boolean asc,
                                     @RequestParam(defaultValue = AppConstants.SORT_TYPE_CREATED_AT) String sortType,
                                     @RequestParam(defaultValue = AppConstants.DEFAULT_BEGIN_DATE) Timestamp begin,
                                     @RequestParam(defaultValue = AppConstants.DEFAULT_END_DATE) Timestamp end) {
        return ResponseEntity.ok(paymentService.getPayments(page, size, asc, sortType, begin, end));
    }
}
