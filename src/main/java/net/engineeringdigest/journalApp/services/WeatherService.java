package net.engineeringdigest.journalApp.services;

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
    private RestTemplate restTemplate;

    public WeatherResponse getWeather(String city) {
        String finalAPI = appCache.appCache.get(AppCache.keys.WEATHER_API.toString()).replace("<apikey>", apikey ).replace("<city>", city);
        ResponseEntity<WeatherResponse> responseEntity =  restTemplate.exchange(finalAPI, HttpMethod.GET, null, WeatherResponse.class);
        return responseEntity.getBody();
    }
}
