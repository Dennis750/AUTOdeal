package ro.autodeal.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class UserClientService {

    private final RestTemplate restTemplate;
    private final String userMicroserviceUrl;

    public UserClientService(RestTemplate restTemplate,
                             @Value("${user.microservice.url}") String userMicroserviceUrl) {
        this.restTemplate = restTemplate;
        this.userMicroserviceUrl = userMicroserviceUrl;
    }

    public String getUserRole(String username) {
        String url = userMicroserviceUrl + "/api/users/" + username + "/role";
        return restTemplate.getForObject(url, String.class);
    }

    public boolean userExists(String username) {
        String url = userMicroserviceUrl + "/api/users/" + username + "/exists";
        Boolean result = restTemplate.getForObject(url, Boolean.class);
        return Boolean.TRUE.equals(result);
    }
}