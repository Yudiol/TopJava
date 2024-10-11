package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class InMemoryMealDao implements MealDao {

    private final AtomicInteger key = new AtomicInteger();
    private Map<Integer, Meal> mealMap = new ConcurrentHashMap<>();

    {
        mealMap = Stream.of(
                        new Meal(null, LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                        new Meal(null, LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                        new Meal(null, LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                        new Meal(null, LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                        new Meal(null, LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                        new Meal(null, LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                        new Meal(null, LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410))
                .map(this::create)
                .collect(Collectors.toMap(Meal::getId,
                        meal -> meal,
                        (a, b) -> a,
                        ConcurrentHashMap::new));
    }

    public Collection<Meal> findAll() {
        return mealMap.values();
    }

    public Meal findById(int id) {
        return mealMap.get(id);
    }

    public Meal create(Meal meal) {
        int key = this.key.incrementAndGet();
        meal.setId(key);
        mealMap.putIfAbsent(key, meal);
        return meal;
    }

    public Meal update(Meal updatedMeal) {
        return mealMap.computeIfPresent(updatedMeal.getId(), (a, b) -> b = updatedMeal);
    }

    public void delete(int id) {
        mealMap.remove(id);
    }
}
