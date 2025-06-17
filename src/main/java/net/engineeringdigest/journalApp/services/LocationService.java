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

    @Autowired
    private RedisService redisService;

    private String getClientIP(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() ) {
            //ip = request.getRemoteAddr(); //we will use it after deploying our project in load balances public servers
            ip = "8.8.8.8";
        } else if (ip.contains(",")) {
            ip = ip.split(",")[0];
        }
        return ip;
    }

    private LocationResponse getLocationFromIP(String ip) {
        try {
            String finalAPI = appCache.appCache.get(AppCache.keys.LOCATION_API.toString()).replace("<ip>", ip);
            ResponseEntity<LocationResponse> responseEntity = restTemplate.exchange(finalAPI, HttpMethod.GET, null,
                    LocationResponse.class);
            return responseEntity.getBody();
        } catch (Exception e) {
            log.info("UNKONWN LOCATION, using 'mumbai'as default location", e);
            LocationResponse locationResponse = new LocationResponse();
            locationResponse.setCity("mumbai");
            return locationResponse;
        }
    }

    public String getLocation(HttpServletRequest request) {
        LocationResponse locationResponse = redisService.get("location", LocationResponse.class);
        if(locationResponse!=null) {
            return locationResponse.getCity();
        }
        else {
            LocationResponse loc = getLocationFromIP(getClientIP(request));
            redisService.set("location", loc, 300l);
            return loc.getCity();
        }
    }

}
