package main.domain.resource;

import lombok.AllArgsConstructor;
import lombok.Data;

// Se usa para sacar solo de la DB el ID y la imagen de la publicacion.
@Data
@AllArgsConstructor
public class PostPreview {

    private final Integer id;
    private final String image;
    private final String media;
    private final String title;



}