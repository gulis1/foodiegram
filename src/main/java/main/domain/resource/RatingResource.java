package main.domain.resource;

import lombok.Data;

import javax.annotation.Resource;

@Resource
@Data
public class RatingResource {

    private Integer idpubli;
    private Integer iduser;
    private Integer punt;
    
}
