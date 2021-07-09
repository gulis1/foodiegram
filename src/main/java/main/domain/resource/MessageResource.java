package main.domain.resource;

import lombok.Data;

import javax.annotation.Resource;

@Resource
@Data
public class MessageResource {

    private Integer id;
    private Integer iduser1;
    private Integer iduser2;
    private String text;
    
}
