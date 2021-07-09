package main.domain.resource;

import lombok.Data;

import javax.annotation.Resource;
import java.sql.Date;

@Resource
@Data

public class PostResource {

    private Integer id;
    private Integer iduser;
    private String text;
    private String image;
    private String ciudad;
    private String pais;
    private Date fecha;
    private String media;
    private Integer numerototalval;

}
