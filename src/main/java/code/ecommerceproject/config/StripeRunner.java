package code.ecommerceproject.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class StripeRunner implements ApplicationRunner {

    @Value("${stripe.secret-key}")
    private String secretKey;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (secretKey == null || secretKey.isEmpty()) {
            System.err.println("Stripe secret key is missing!");
            return;
        }

        try {
            ProcessBuilder processBuilder = new ProcessBuilder(
                    "stripe", "listen", "--forward-to", "http://localhost:8080/api/orders/webhook"
            );
            processBuilder.environment().put("STRIPE_API_KEY", secretKey);
            processBuilder.inheritIO();
            processBuilder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}