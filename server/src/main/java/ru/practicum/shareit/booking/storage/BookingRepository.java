package ru.practicum.shareit.booking.storage;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;
import java.util.List;

/*
    Репозиторий для взаимосвязи с базой данных сущности бронирования
*/
@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    /*
        Получить все бронирования пользователя
    */
    @Query("SELECT b " +
            "FROM Booking AS b " +
            "WHERE b.booker.id = ?1 " +
            "ORDER BY b.start DESC")
    List<Booking> findAllBookingsByUserIdUser(Long userId, Pageable pageable);

    /*
        Получить все бронирования со статусом PAST пользователя
    */
    @Query("SELECT b " +
            "FROM Booking AS b " +
            "WHERE current_timestamp > b.end " +
            "AND b.booker.id = ?1 " +
            "ORDER BY b.start DESC")
    List<Booking> findUserBookingsByPast(Long userId, Pageable pageable);

    /*
        Получить все бронирования со статусом FUTURE пользователя
    */
    @Query("SELECT b " +
            "FROM Booking AS b " +
            "WHERE b.booker.id = ?1 " +
            "AND b.start > ?2 " +
            "ORDER BY b.start DESC")
    List<Booking> findUserBookingsByFuture(Long userId, LocalDateTime nowTime, Pageable pageable);

    /*
        Получить все бронирования со статусом CURRENT пользователя
    */
    @Query("SELECT b " +
            "FROM Booking AS b " +
            "WHERE b.booker.id = ?1 " +
            "AND current_timestamp BETWEEN b.start AND b.end " +
            "ORDER BY b.start DESC")
    List<Booking> findUserBookingsByCurrent(Long userId, Pageable pageable);

    /*
        Получить все бронирования со статусом WATTING пользователя
    */
    @Query("SELECT b " +
            "FROM Booking AS b " +
            "WHERE b.status = 0 " +
            "AND b.booker.id = ?1 " +
            "ORDER BY b.start DESC")
    List<Booking> findUserBookingsByWatting(Long userId, Pageable pageable);

    /*
        Получить все бронирования со статусом REJECTED пользователя
    */
    @Query("SELECT b " +
            "FROM Booking AS b " +
            "WHERE b.status = 2 " +
            "AND b.booker.id = ?1 " +
            "ORDER BY b.start DESC")
    List<Booking> findUserBookingsByRejected(Long userId, Pageable pageable);

    /*
        Получить все бронирования владельца
    */
    @Query("SELECT b " +
            "FROM Booking AS b " +
            "WHERE b.item.owner.id = ?1 " +
            "ORDER BY b.start DESC")
    List<Booking> findAllBookingsByOwnerIdOwner(Long ownerId, Pageable pageable);

    /*
        Получить все бронирования со статусом PAST владельца
    */
    @Query("SELECT b " +
            "FROM Booking AS b " +
            "WHERE current_timestamp > b.end " +
            "AND b.item.owner.id = ?1 " +
            "ORDER BY b.start DESC")
    List<Booking> findOwnerBookingsByPast(Long ownerId, Pageable pageable);

    /*
        Получить все бронирования со статусом FUTURE владельца
    */
    @Query("SELECT b " +
            "FROM Booking AS b " +
            "WHERE b.item.owner.id = ?1 " +
            "AND b.start > ?2 " +
            "ORDER BY b.start DESC")
    List<Booking> findOwnerBookingsByFuture(Long userId, LocalDateTime nowTime, Pageable pageable);

    /*
        Получить все бронирования со статусом CURRENT владельца
    */
    @Query("SELECT b " +
            "FROM Booking AS b " +
            "WHERE b.status = 2 " +
            "AND b.item.owner.id = ?1 " +
            "AND current_timestamp BETWEEN b.start AND b.end " +
            "ORDER BY b.start DESC")
    List<Booking> findOwnerBookingsByCurrent(Long userId, Pageable pageable);

    /*
        Получить все бронирования со статусом WATTING владельца
    */
    @Query("SELECT b " +
            "FROM Booking AS b " +
            "WHERE b.status = 0 " +
            "AND b.item.owner.id = ?1 " +
            "ORDER BY b.start DESC")
    List<Booking> findOwnerBookingsByWatting(Long userId, Pageable pageable);

    /*
        Получить все бронирования со статусом REJECTED владельца
    */
    @Query("SELECT b " +
            "FROM Booking AS b " +
            "WHERE b.status = 2 " +
            "AND b.item.owner.id = ?1 " +
            "ORDER BY b.start DESC")
    List<Booking> findOwnerBookingsByRejected(Long userId, Pageable pageable);

    /*
        Получить все бронирования по id предмета
    */
    @Query("SELECT b " +
            "FROM Booking AS b " +
            "WHERE b.item.id = ?1")
    List<Booking> findAllByItem(Long itemId);

    /*
        Получить все бронирования по id предмета и id арендатора
    */
    @Query("SELECT b " +
            "FROM Booking AS b " +
            "WHERE b.item.id = ?1 " +
            "AND b.booker.id = ?2 " +
            "AND b.status = 1")
    List<Booking> getBookingByBookerAndItem(Long idItem, Long idBooker);
}