package main.application.service;

import com.google.gson.Gson;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;



import java.util.Map;



@Service
public class RestService {

    @Autowired
    RestTemplate template;
    Gson gson;


    public RestService() {

        this.gson = new Gson();
    }

    @CircuitBreaker(name="geoData", fallbackMethod = "fallback")
    public Map<String, Object> getGeoData(Double lat, Double lon) {
        String latitude = lat.toString().replace(",", ".");
        String longitude = lon.toString().replace(",", ".");

        String query = String.format("https://nominatim.openstreetmap.org/reverse?format=json&lat=%s8&lon=%s&accept-language=en", latitude, longitude);

        Map<String, Object> data = (Map<String, Object>) getJSON(query).get("address");

        if (!data.containsKey("city")) {

            if (data.containsKey("village")) {
                data.put("city", data.get("village"));
                data.remove("village");
            }

            if (data.containsKey("town")) {
                data.put("city", data.get("town"));
                data.remove("town");
            }
        }

        if (data.containsKey("road")) {
            data.put("street", data.get("road"));
            data.remove("road");
        }

        return data;
    }

    public Map<String, Object> fallback(Double lat, Double lon) {
        return  null;
    }

    private Map<String, Object> getJSON(String url) {
        return gson.fromJson(this.template.getForObject(url, String.class), Map.class);
    }
}