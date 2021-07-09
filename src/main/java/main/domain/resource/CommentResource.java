package main.domain.resource;

import lombok.Data;

import javax.annotation.Resource;

@Resource
@Data
public class CommentResource {

    private Integer id;
    private Integer idpubli;
    private Integer iduser;
    private String text;
    private String user;
    private String pfp;


}
