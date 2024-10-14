package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;
import ru.javawebinar.topjava.util.Store;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryUserRepository implements UserRepository {
    private final Map<Integer, User> users = new ConcurrentHashMap<>();
    private final static AtomicInteger userId = new AtomicInteger();

    {
        Store.users.forEach(this::save);
    }

    @Override
    public boolean delete(int id) {
        return users.remove(id) != null;
    }

    @Override
    public User save(User user) {
        if (user.isNew()) {
            user.setId(userId.incrementAndGet());
            return this.users.put(user.getId(), user);
        }
        return users.computeIfPresent(user.getId(), (id, oldUser) -> user);
    }

    @Override
    public User get(int id) {
        return users.get(id);
    }

    @Override
    public List<User> getAll() {
        return users.values().stream()
                .sorted((a, b) -> a.getName().compareTo(b.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public User getByEmail(String email) {
        return users.values().stream()
                .filter(user -> email.equalsIgnoreCase(user.getEmail()))
                .findFirst()
                .orElse(null);
    }
}