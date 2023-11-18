package ru.practicum.shareit.request.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

/*
    Объект сущности для связи с БД запроса
 */
@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "requests")
public class ItemRequest {
    @Id
    @Column(name = "id_requests")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // id запроса

    @Column
    @NotNull
    @NotEmpty
    @NotBlank
    private String description; // описание запроса требуемой вещи

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requestor_id")
    private User requestor; // пользователь, создавший запрос

    @Column
    private LocalDateTime created = LocalDateTime.now(); // дата создания

    @OneToMany
    @JoinColumn(name = "request_id")
    private List<Item> items;
}
