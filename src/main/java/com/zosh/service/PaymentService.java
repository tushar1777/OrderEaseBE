package com.zosh.service;

import com.stripe.exception.StripeException;
import com.zosh.model.Order;
import com.zosh.model.PaymentResponse;

public interface PaymentService {

    PaymentResponse generatePaymentLink(Order order) throws StripeException;

}
