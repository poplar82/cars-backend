package com.topolski.project.service;

import com.topolski.project.model.Car;
import com.topolski.project.model.Color;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CarService implements CarInterface {
    List<Car> cars = new ArrayList<>();

    @Override
    public List<Car> getAllCars() {
        return cars;
    }

    @Override
    public Optional<Car> getCarById(long id) {
        return cars.stream().filter(car -> car.getId() == id).findFirst();
    }

    @Override
    public List<Car> getCarsByColor(Color color) {
        return cars.stream().filter(car -> car.getColor().equals(color)).collect(Collectors.toList());
    }

    @Override
    public boolean addCar(Car car) {
        return cars.add(car);
    }

    @Override
    public boolean modifyCar(Car car) {
        cars.set(cars.indexOf(car),car);
        return cars.contains(car);
    }

    @Override
    public boolean deleteCarById(long id) {
        return cars.stream()
                .filter(car -> car.getId() == id)
                .findFirst()
                .map(car -> cars.remove(car))
                .orElse(false);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        cars.add(Car.builder().id(1).mark("Kia").model("Optima").color(Color.RED).build());
        cars.add(Car.builder().id(2).mark("Porsche").model("Cayenne").color(Color.BLACK).build());
        cars.add(Car.builder().id(3).mark("Audi").model("S8").color(Color.BLACK).build());
    }
}
