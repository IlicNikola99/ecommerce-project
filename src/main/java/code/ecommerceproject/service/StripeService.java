package code.ecommerceproject.service;

import code.ecommerceproject.entity.OrderedProduct;
import code.ecommerceproject.entity.User;
import code.ecommerceproject.exception.CartPaymentException;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class StripeService {

    @Value("${stripe.secret-key}")
    private String apiKey;

    @Value("${stripe.client-base-url}")
    private String clientBaseUrl;

    private final ProductService productService;

    @PostConstruct
    public void setApiKey() {
        Stripe.apiKey = apiKey;
    }

    public String createPayment(final User connectedUser,
                                final Set<OrderedProduct> orderedProducts) {
        SessionCreateParams.Builder sessionBuilder = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .putMetadata("user_public_id", connectedUser.getId().toString())
                .setCustomerEmail(connectedUser.getEmail())
                .setBillingAddressCollection(SessionCreateParams.BillingAddressCollection.REQUIRED)
                .setSuccessUrl(this.clientBaseUrl + "/cart/success?session_id={CHECKOUT_SESSION_ID}")
                .setCancelUrl(this.clientBaseUrl + "/cart/failure");

        for (OrderedProduct product : orderedProducts) {

            final SessionCreateParams.LineItem.PriceData.ProductData productData = SessionCreateParams.LineItem.PriceData.ProductData.builder()
                    .putMetadata("product_id", product.getProductId().toString())
                    .setName(product.getProductName())
                    .build();

            final SessionCreateParams.LineItem.PriceData linePriceData = SessionCreateParams.LineItem.PriceData.builder()
                    .setUnitAmountDecimal(BigDecimal.valueOf(product.getPrice() * 100))
                    .setProductData(productData)
                    .setCurrency("EUR")
                    .build();

            final SessionCreateParams.LineItem lineItem = SessionCreateParams.LineItem.builder()
                    .setQuantity(product.getQuantity())
                    .setPriceData(linePriceData)
                    .build();

            sessionBuilder.addLineItem(lineItem);
        }

        return createSession(sessionBuilder.build());
    }

    private String createSession(final SessionCreateParams sessionInformation) {
        try {
            final Session session = Session.create(sessionInformation);
            return session.getId();
        } catch (StripeException se) {
            throw new CartPaymentException("Error while creating Stripe session");
        }
    }
}