package main.domain.resource;

import lombok.Data;

import javax.annotation.Resource;

@Resource
@Data
public class FollowResource {

    private Integer iduser1;
    private Integer iduser2;

}
