package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.Store;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private final Map<Integer, Map<Integer, Meal>> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger();

    {
        Store.meals0.forEach(meal -> save(1, meal));
        Store.meals1.forEach(meal -> save(2, meal));
    }

    @Override
    public Meal save(int userId, Meal meal) {
        Map<Integer, Meal> meals = repository.getOrDefault(userId, new ConcurrentHashMap<>());
        if (meals.isEmpty()) {
            repository.put(userId, meals);
        }
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            meals.put(meal.getId(), meal);
            return meal;
        }
        return meals.computeIfPresent(meal.getId(), (id, m) -> meal);
    }

    @Override
    public boolean delete(int userId, int id) {
        Map<Integer, Meal> meals = repository.getOrDefault(userId, null);
        if (!meals.isEmpty()) {
            return meals.remove(id) != null;
        }
        return false;
    }

    @Override
    public Meal get(int userId, int id) {
        Map<Integer, Meal> meals = repository.getOrDefault(userId, null);
        if (!meals.isEmpty()) {
            return meals.values().stream()
                    .filter(meal -> Objects.equals(meal.getId(), id))
                    .findFirst()
                    .orElse(null);
        }
        return null;
    }

    @Override
    public List<Meal> getAll(int userId) {
        return repository.getOrDefault(userId, new ConcurrentHashMap<>()).values()
                .stream()
                .sorted((a, b) -> b.getDateTime().compareTo(a.getDateTime()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Meal> filter(int userId, LocalDate from, LocalDate to) {
        return repository.getOrDefault(userId, new ConcurrentHashMap<>()).values().stream()
                .filter(meal -> meal.getDateTime().toLocalDate().compareTo(from) >= 0 && meal.getDateTime().toLocalDate().compareTo(to) <= 0)
                .sorted((a, b) -> b.getDateTime().compareTo(a.getDateTime()))
                .collect(Collectors.toList());
    }
}

