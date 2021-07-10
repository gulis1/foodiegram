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

    @NotNull
    private MultipartFile image;
    @NotNull
    private String title;
    private String text;
    private String latitud;
    private String longitud;


    public Double getLatitud() {
        return latitud != null ? Double.parseDouble(latitud) : null;
    }
    public Double getLongitud() {
        return longitud != null ? Double.parseDouble(longitud) : null;
    }
}
