package ru.practicum.shareit.item.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

/*
    Репозиторий для взаимосвязи с базой данных сущности предмет
*/
@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    /*
        Получить все прдметы пользователя по id
    */
    List<Item> findAllByOwner_IdOrderByIdAsc(Long userId);

    /*
        Получить все доступные прдметы по поиску в названии предмета и описании
    */
    List<Item> findAllByAvailableAndDescriptionContainingIgnoreCaseOrNameContainingIgnoreCase(boolean available,
                                                                                              String description,
                                                                                              String name);
}
