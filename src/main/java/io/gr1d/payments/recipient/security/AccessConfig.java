package io.gr1d.payments.recipient.security;

import io.gr1d.auth.keycloak.ConfigSecurity;
import io.gr1d.auth.keycloak.EndpointConfiguration;
import io.gr1d.core.healthcheck.HealthCheckController;
import io.gr1d.payments.recipient.controller.BankController;
import io.gr1d.payments.recipient.controller.RecipientController;
import org.springframework.stereotype.Component;

@Component
public class AccessConfig implements EndpointConfiguration {

    private static final String USER = "user";
    private static final String ADMIN = "admin";

    @Override
    public void configure(final ConfigSecurity config) throws Exception {
        config
                .allow(HealthCheckController.class, "completeHealthCheck", ADMIN)

                .allow(BankController.class, "list", USER, ADMIN)

                .allow(RecipientController.class, "get", USER, ADMIN)
                .allow(RecipientController.class, "list", USER, ADMIN)

                .allow(RecipientController.class, "create", ADMIN)
                .allow(RecipientController.class, "update", ADMIN)
                .allow(RecipientController.class, "remove", ADMIN);
    }


}
