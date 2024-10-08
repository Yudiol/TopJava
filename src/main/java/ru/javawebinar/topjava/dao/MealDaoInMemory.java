package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MealDaoInMemory implements MealDao {

    private final AtomicInteger key = new AtomicInteger(7);
    private final Map<Integer, Meal> mealMap = new ConcurrentHashMap<>();

    {
        mealMap.put(1, new Meal(1, LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500));
        mealMap.put(2, new Meal(2, LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000));
        mealMap.put(3, new Meal(3, LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500));
        mealMap.put(4, new Meal(4, LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100));
        mealMap.put(5, new Meal(5, LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000));
        mealMap.put(6, new Meal(6, LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500));
        mealMap.put(7, new Meal(7, LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410));

    }

    public Collection<Meal> findAll() {
        return mealMap.values();
    }

    public Meal findById(Integer id) {
        return mealMap.get(id);
    }

    public Meal create(Meal meal) {
        meal.setId(key.incrementAndGet());
        mealMap.put(key.get(), meal);
        return meal;
    }

    public Meal update(Meal updatedMeal) {
        if (mealMap.get(updatedMeal.getId()) != null) {
            mealMap.put(updatedMeal.getId(), updatedMeal);
            return updatedMeal;
        }
        return null;
    }


    public void delete(Integer id) {
        mealMap.remove(id);
    }
}
