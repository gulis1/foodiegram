package main.domain.resource;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.annotation.Resource;

@Resource
@Data
@AllArgsConstructor
public class SponsorResource {

    private Integer id;
    private java.sql.Date endtime;
}
