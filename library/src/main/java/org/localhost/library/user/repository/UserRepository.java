package org.localhost.library.user.repository;

import org.localhost.library.user.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
    boolean existsByUserName(String userName);
}
