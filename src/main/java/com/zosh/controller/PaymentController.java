package com.zosh.controller;

import com.zosh.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

//	@PostMapping("/{orderId}/payment")
//	public ResponseEntity<PaymentResponse> generatePaymentLink(@PathVariable Long orderId) 
//			throws StripeException{
//		
//		PaymentResponse res = paymentService.generatePaymentLink(orderId);
//		
//		return new ResponseEntity<PaymentResponse>(res,HttpStatus.ACCEPTED);
//	}

}
