package net.engineeringdigest.journalApp.responseApi;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WeatherResponse {
    public Current current;

    @Getter
    @Setter
    public class Current {
        @JsonProperty("weather_descriptions")
        public List<String> weather_descriptions;
        @JsonProperty("air_quality")
        public AirQuality airQuality;
        public int feelslike;

        @Getter
        @Setter
        public class AirQuality {
            @JsonProperty("pm2_5")
            public String pm25;
        }
    }

}
