package main.domain.resource;


import lombok.Data;

import javax.annotation.Resource;

@Resource
@Data
public class RestaurantResource {

    private Integer idUser;
    private String type;
    private String pais;
    private String ciudad;
    private String calle;
    private Boolean vip;
}
