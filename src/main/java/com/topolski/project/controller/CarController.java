package com.topolski.project.controller;

import com.topolski.project.model.Car;
import com.topolski.project.service.CarService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequestMapping(value = "/cars")
public class CarController {
    private final CarService carService;

    public CarController(CarService carService) {
        this.carService = carService;
    }

    @GetMapping
    public ResponseEntity<CollectionModel<Car>> getAllCars() {
        List<Car> cars = carService.getAllCars();
        cars.forEach(car ->
                car.addIf(!car.hasLinks(), () -> linkTo(CarController.class)
                        .slash(car.getId()).withRel("carLink")));
        return new ResponseEntity<>(
                CollectionModel.of(cars,
                        linkTo(CarController.class).withSelfRel()), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Car>> getCarById(@PathVariable long id) {
        Optional<Car> carOptional = carService.getCarById(id);
        carOptional.map(car -> car.addIf(!car.hasLinks(),
                () -> linkTo(CarController.class)
                        .slash(id).withSelfRel()));
        return carOptional.map(car ->
                new ResponseEntity<>(
                        EntityModel
                                .of(carOptional.get(),
                                        linkTo(CarController.class).withSelfRel()),
                        HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
