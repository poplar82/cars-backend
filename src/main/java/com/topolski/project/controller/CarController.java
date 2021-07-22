package com.topolski.project.controller;

import com.topolski.project.model.Car;
import com.topolski.project.model.Color;
import com.topolski.project.service.CarService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequestMapping(value = "/cars")
@CrossOrigin
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
        if (cars.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
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

    @GetMapping("/color/{color}")
    public ResponseEntity<CollectionModel<Car>> getCarsByColor(@PathVariable String color) {
        List<Car> cars = carService.getCarsByColor(Color.valueOf(color.toUpperCase()));
        cars.forEach(car ->
                car.addIf(!car.hasLinks(), () -> linkTo(CarController.class)
                        .slash(car.getId()).withRel("carLink")));
        if (cars.isEmpty()) {
         return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(
                CollectionModel.of(cars,
                        linkTo(CarController.class).withSelfRel()), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<String> addCar(@RequestBody Car car) {
        if (carService.addCar(car)) {
            return new ResponseEntity<>("Car has been added", HttpStatus.CREATED);
        }
        return new ResponseEntity<>("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PutMapping
    public ResponseEntity<String> modCar(@RequestBody Car newCar) {
        Optional<Car> carOptional = carService.getCarById(newCar.getId());
        if (carOptional.isPresent()) {
            carService.deleteCarById(carOptional.get().getId());
            carService.addCar(newCar);
            return new ResponseEntity<>("Modified", HttpStatus.OK);
        }
        return new ResponseEntity<>("Something went wrong", HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCarById(@PathVariable long id) {
        if (carService.deleteCarById(id)) {
            return new ResponseEntity<>("Deleted.",HttpStatus.OK);
        }
        return new ResponseEntity<>("Car is not found.", HttpStatus.NOT_FOUND);
    }
}
