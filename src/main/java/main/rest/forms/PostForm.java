package main.rest.forms;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostForm {

//    @NotNull
    private MultipartFile image;
    @NotNull
    private String title;
    private String text;
    private String latitude;
    private String longitude;


    public Double getLatitude() {
        return latitude != null ? Double.parseDouble(latitude) : null;
    }
    public Double getLongitude() {
        return longitude != null ? Double.parseDouble(longitude) : null;
    }
}
