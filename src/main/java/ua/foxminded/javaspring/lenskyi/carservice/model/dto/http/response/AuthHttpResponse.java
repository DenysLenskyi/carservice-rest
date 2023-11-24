package ua.foxminded.javaspring.lenskyi.carservice.model.dto.http.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AuthHttpResponse {

        private String token;
        private String scope;
        private Long expiresIn;
        private String type;

        public AuthHttpResponse() {
        }

        @JsonProperty("access_token")
        public String getToken() {
                return token;
        }

        @JsonProperty("access_token")
        public void setToken(String token) {
                this.token = token;
        }

        public String getScope() {
                return scope;
        }

        public void setScope(String scope) {
                this.scope = scope;
        }

        @JsonProperty("expires_in")
        public Long getExpiresIn() {
                return expiresIn;
        }

        @JsonProperty("expires_in")
        public void setExpiresIn(Long expiresIn) {
                this.expiresIn = expiresIn;
        }

        @JsonProperty("token_type")
        public String getType() {
                return type;
        }

        @JsonProperty("token_type")
        public void setType(String type) {
                this.type = type;
        }
}
