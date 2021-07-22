package com.topolski.project.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

@Builder
@Getter @Setter
@EqualsAndHashCode(callSuper = false)
@Relation(collectionRelation = "cars")
public class Car extends RepresentationModel<Car> {
    private long id;
    private String mark;
    private String model;
    private Color color;
    private String yearOfProduction;
}
