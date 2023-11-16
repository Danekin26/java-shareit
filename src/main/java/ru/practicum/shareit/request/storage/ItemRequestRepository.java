package ru.practicum.shareit.request.storage;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

/*
    Репозиторий для взаимосвязи с базой данных сущности запроса
*/
@Repository
public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {

    /*
        Получить все свои запросы
     */
    @Query("SELECT i " +
            "FROM ItemRequest AS i " +
            "WHERE i.requestor.id = ?1")
    List<ItemRequest> findAllByRequestor_Id(Long requestorId);

    /*
        Получить запросы других пользователей
     */
    List<ItemRequest> findAllByRequestor_IdNotOrderByCreatedDesc(Long requestorId, Pageable pageable);


}
