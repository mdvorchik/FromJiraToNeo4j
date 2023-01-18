package controller;

import com.atlassian.connect.spring.AtlassianHostRestClients;
import entity.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
class MyselfController {

    private final AtlassianHostRestClients atlassianHostRestClients;

    MyselfController(AtlassianHostRestClients atlassianHostRestClients) {
        this.atlassianHostRestClients = atlassianHostRestClients;
    }

    @GetMapping("/myself")
    User getMyself(@RequestParam String hostBaseUrl) {
        return atlassianHostRestClients
                .authenticatedAsAddon()
                .getForObject(hostBaseUrl + "/rest/api/2/myself", User.class);
    }
}