package ua.foxminded.javaspring.lenskyi.carservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.foxminded.javaspring.lenskyi.carservice.model.dto.http.response.AuthHttpResponse;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/auth")
public class Auth0Controller {

        @Value("${auth0.domain}")
        private String domain;
        @Value("${auth0.audience}")
        private String audience;
        @Value("${auth0.clientSecret}")
        private String secret;
        @Value("${auth0.clientId}")
        private String clientId;

        @GetMapping
        public ResponseEntity<String> getAccessToken() throws IOException {
                HttpResponse<String> response = Unirest.post("https://" + domain + "/oauth/token")
                        .header("content-type", "application/x-www-form-urlencoded")
                        .body("grant_type=client_credentials&client_id=" + clientId +
                                "&client_secret=" + secret + "&audience=" + audience)
                        .asString();
                ObjectMapper objectMapper = new ObjectMapper();
                AuthHttpResponse authHttpResponse = objectMapper.readValue(response.getBody(), AuthHttpResponse.class);
                return ResponseEntity
                        .status(HttpStatus.OK)
                        .body(authHttpResponse.getToken());
        }
}