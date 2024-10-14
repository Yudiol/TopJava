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
import java.util.Objects;

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

    public void delete(int userId, int id) {
        checkNotFoundWithId(repository.delete(userId, id), id);
    }

    public Meal get(int userId, int id) {
        return checkNotFoundWithId(repository.get(userId, id), id);
    }

    public Collection<MealTo> getAll(int userId, int caloriesPerDay) {
        return MealsUtil.getTos(repository.getAll(userId), caloriesPerDay);
    }

    public List<MealTo> filter(String fromDate, String toDate, String fromTime, String toTime, int userId, int caloriesPerDay) {
        Collection<Meal> filteredMeals = repository.filter(
                userId,
                Objects.equals(fromDate, "") ? LocalDate.MIN : LocalDate.parse(fromDate),
                Objects.equals(toDate, "") ? LocalDate.MAX : LocalDate.parse(toDate)
        );

        return MealsUtil.getFilteredTos(filteredMeals, caloriesPerDay,
                Objects.equals(fromTime, "") ? LocalTime.MIN : LocalTime.parse(fromTime),
                Objects.equals(toTime, "") ? LocalTime.MAX : LocalTime.parse(toTime));
    }
}