package ru.javawebinar.topjava.service;

import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFoundWithId;

@Service
public class MealService {

    private MealRepository repository;

    public MealService(MealRepository repository) {
        this.repository = repository;
    }

    public Meal save(Integer userId, Meal meal) {
        return repository.save(userId, meal);
    }

    public void update(Integer userId, Integer id, Meal meal) {
        checkNotFoundWithId(repository.save(userId, meal), id);
    }

    public void delete(int userId, int id) {
        checkNotFoundWithId(repository.delete(userId, id), id);
    }

    public Meal get(int userId, int id) {
        return checkNotFoundWithId(repository.get(userId, id), id);
    }

    public List<MealTo> getAll(int userId, int caloriesPerDay) {
        return MealsUtil.getTos(repository.getAll(userId), caloriesPerDay);
    }

    public List<MealTo> filter(LocalDate fromDate, LocalDate toDate, LocalTime fromTime, LocalTime toTime, int userId, int caloriesPerDay) {
        Collection<Meal> filteredMeals = repository.filter(
                userId, fromDate, toDate);
        return MealsUtil.getFilteredTos(filteredMeals, caloriesPerDay, fromTime, toTime);
    }
}