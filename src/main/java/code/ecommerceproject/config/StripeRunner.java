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
                    "stripe", "listen", "--api-key", secretKey, "--forward-to", "http://localhost:8080/api/orders/webhook"
            );
            processBuilder.inheritIO(); // Redirects output to console
            processBuilder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}