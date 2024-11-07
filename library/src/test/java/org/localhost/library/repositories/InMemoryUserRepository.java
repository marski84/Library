package org.localhost.library.repositories;

import org.localhost.library.user.UserRepository;
import org.localhost.library.user.model.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryUserRepository implements UserRepository {
    private final Map<Long, User> users = new HashMap<>();
    private final AtomicLong userId = new AtomicLong(1);

    @Override
    public User save(User entity) {
        if (entity.getId() == 0) {
            long userId = this.userId.getAndIncrement();
            entity.setId(userId);
            users.put(userId, entity);
            return users.get(userId);
        } else {
            System.out.println(entity);
            users.replace(entity.getId(), entity);
            return users.get(entity.getId());
        }
    }

    @Override
    public <S extends User> Iterable<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<User> findById(Long userId) {
        System.out.println(users.values().stream().filter(user -> user.getId() == userId).findFirst());
        return users.values().stream().filter(user -> user.getId() == userId).findFirst();
    }

    @Override
    public boolean existsById(Long aLong) {
        return false;
    }

    @Override
    public Iterable<User> findAll() {
        return users.values();
    }

    @Override
    public Iterable<User> findAllById(Iterable<Long> longs) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(Long aLong) {

    }

    @Override
    public void delete(User entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {

    }

    @Override
    public void deleteAll(Iterable<? extends User> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public boolean existsByUserName(String userName) {
        return users.values().stream().anyMatch(user -> user.getUserName().equals(userName));
    }
}
