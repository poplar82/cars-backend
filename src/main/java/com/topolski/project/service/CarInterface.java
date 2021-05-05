package com.topolski.project.service;

import com.topolski.project.model.Car;
import com.topolski.project.model.Color;

import java.util.List;
import java.util.Optional;

public interface CarInterface {
    List<Car> getAllCars();
    Optional<Car> getCarById(long id);
    List<Car> getCarsByColor(Color color);
    boolean addCar(Car car);
    public boolean modifyCar(Car car);
    boolean deleteCarById(long id);
}
