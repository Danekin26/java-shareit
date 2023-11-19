package ru.practicum.shareit.item.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Comment;

import java.util.List;

/*
    Репозиторий для взаимосвязи с базой данных сущности отзывов
*/
@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    /*
        Получить отзывы по id предмета
    */
    @Query("SELECT c " +
            "FROM Comment AS c " +
            "WHERE c.item.id = ?1")
    List<Comment> findByItemId(Long itemId);
}
