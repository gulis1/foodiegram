package main.domain.resource;


import lombok.Data;

import javax.annotation.Resource;

@Resource
@Data
public class ColaboradorResource {

    private  Integer idUser;
    private  String origin;
    private  String type;
    private String localiztion;
    private boolean vip;
    private Integer money;


}
