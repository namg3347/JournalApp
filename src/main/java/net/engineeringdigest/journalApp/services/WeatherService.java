package net.engineeringdigest.journalApp.services;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import net.engineeringdigest.journalApp.cache.AppCache;
import net.engineeringdigest.journalApp.responseApi.WeatherResponse;

@Service
public class WeatherService {
    
    @Value("${weather.api.key}")
    private String apikey;

    @Autowired
    private AppCache appCache;

    @Autowired
    private LocationService locationService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RedisService redisService;

    public WeatherResponse getWeather(String city) {
        WeatherResponse response = redisService.get("weather_of_"+city,WeatherResponse.class);
        if(response!=null) {
            return response;
        }
        else {
            String finalAPI = appCache.appCache.get(AppCache.keys.WEATHER_API.toString()).replace("<apikey>", apikey ).replace("<city>", city);
            ResponseEntity<WeatherResponse> responseEntity =  restTemplate.exchange(finalAPI, HttpMethod.GET, null, WeatherResponse.class);
            WeatherResponse body = responseEntity.getBody();
            if(body !=null) {              
                redisService.set("weather_of_"+city, body, 300l);
            }
            return body;
        }
        
    }
}
