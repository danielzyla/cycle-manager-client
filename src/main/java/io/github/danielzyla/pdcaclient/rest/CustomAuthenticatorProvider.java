package io.github.danielzyla.pdcaclient.rest;

import io.github.danielzyla.pdcaclient.config.PropertyProvider;
import io.github.danielzyla.pdcaclient.dto.UserWriteDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class CustomAuthenticatorProvider implements Authenticator {

    private static final String LOGIN_PATH = "/login";
    private final RestTemplate restTemplate;

    public CustomAuthenticatorProvider() {
        this.restTemplate = new RestTemplate();
    }

    @Override
    public void authenticate(final UserWriteDto user, AuthenticationResultHandler authenticationResultHandler) {
        Thread authenticationThread = new Thread(() -> {
            try {
                ResponseEntity<String> authenticationResponse = restTemplate.postForEntity(
                        PropertyProvider.getRestAppUrl() + LOGIN_PATH,
                        user,
                        String.class
                );
                authenticationResultHandler.handle(authenticationResponse.getBody());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        authenticationThread.setDaemon(true);
        authenticationThread.start();
    }
}
