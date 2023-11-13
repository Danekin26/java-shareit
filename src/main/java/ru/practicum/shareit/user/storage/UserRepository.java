package ru.practicum.shareit.user.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;

/*
    Репозиторий для взаимосвязи с базой данных сущности пользователь
*/
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
