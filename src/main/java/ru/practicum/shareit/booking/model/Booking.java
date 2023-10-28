package ru.practicum.shareit.booking.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/*
    Объект брониварония в базе данных
 */
@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "bookings")
public class Booking implements Comparable<Booking> {
    @Id
    @Column(name = "id_booking")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // id бронирования

    @NotNull
    //@Future
    @Column(name = "start_booking")
    private LocalDateTime start; // начало бронирования

    @NotNull
    //@Future
    @Column(name = "end_booking")
    private LocalDateTime end; // конец бронирования

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_item_booking")
    private Item item; // вещь бронирования

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user_booker")
    private User booker; // пользователь взявший в аренду


    @Enumerated(EnumType.ORDINAL)
    @Column(name = "status_booking")
    private BookingStatus status; // статус бронирования

    @Override
    public int compareTo(Booking booking) {
        return booking.getEnd().compareTo(this.getEnd());
    }
}
