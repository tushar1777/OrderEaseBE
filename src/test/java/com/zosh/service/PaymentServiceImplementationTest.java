//package com.zosh.service;
//
//import static org.mockito.Mockito.*;
//import static org.junit.jupiter.api.Assertions.*;
//
//import com.stripe.Stripe;
//import com.stripe.exception.StripeException;
//import com.stripe.model.checkout.Session;
//import com.stripe.param.checkout.SessionCreateParams;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import com.zosh.model.Order;
//import com.zosh.model.PaymentResponse;
//
//public class PaymentServiceImplementationTest {
//
//    @Mock
//    private Stripe stripe;
//
//    @InjectMocks
//    private PaymentServiceImplementation paymentService;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void testGeneratePaymentLink() throws StripeException {
//        // Arrange
//        Order order = new Order();
//        order.setId(1L);
//        order.setTotalAmount(100L);
//
//        Session session = new Session();
//        session.setUrl("https://example.com/payment-link");
//
//        stripe.apiKey = "your_stripe_api_key";
//        when(Session.create(any(SessionCreateParams.class))).thenReturn(session);
//
//        // Act
//        PaymentResponse result = paymentService.generatePaymentLink(order);
//
//        // Assert
//        assertNotNull(result);
//        assertEquals("https://example.com/payment-link", result.getPayment_url());
//    }
//}
//
