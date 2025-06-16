package net.engineeringdigest.journalApp.services;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;
import net.engineeringdigest.journalApp.cache.AppCache;
import net.engineeringdigest.journalApp.responseApi.LocationResponse;

@Service
@Slf4j
public class LocationService {

    @Autowired
    private AppCache appCache;

    @Autowired
    private RestTemplate restTemplate;

    private String getClientIP(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr(); //we will use it after deploying our project in load balances public servers
            // ip = "8.8.8.8";
        } else if (ip.contains(",")) {
            ip = ip.split(",")[0];
        }
        return ip;
    }

    private String getLocationFromIP(String ip) {
        try {
            String finalAPI = appCache.appCache.get(AppCache.keys.LOCATION_API.toString()).replace("<ip>", ip);
            ResponseEntity<LocationResponse> responseEntity = restTemplate.exchange(finalAPI, HttpMethod.GET, null,
                    LocationResponse.class);
            return responseEntity.getBody().getCity();
        } catch (Exception e) {
            log.info("UNKONWN LOCATION, using 'mumbai'as default location", e);
            return "mumbai" ;
        }
    }

    public String getLocation(HttpServletRequest request) {
        return getLocationFromIP(getClientIP(request));
    }

}
