package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.util.Collection;

public interface MealDao {
    Collection<Meal> findAll();

    Meal findById(int id);

    Meal create(Meal meal);

    Meal update(Meal updatedMeal);

    void delete(int id);
}
