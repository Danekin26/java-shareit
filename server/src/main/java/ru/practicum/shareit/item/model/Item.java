package ru.practicum.shareit.item.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/*
    Сущность предмета для связи с базой данных
*/
@Data
@Entity
@Builder
@Table(name = "items")
@EqualsAndHashCode(of = {"itemId"})
@AllArgsConstructor
@NoArgsConstructor
public class Item {
    @Id
    @Column(name = "id_item")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // id вещи

    @NotNull
    @NotEmpty
    @Column(name = "name_item")
    private String name; // имя вещи

    @NotNull
    @NotEmpty
    @Column(name = "description")
    private String description; // описание вещи

    @NotNull
    @Column(name = "available")
    private Boolean available; // статус доступности вещи

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_owner")
    @JsonIgnore
    private User owner; // владелец вещи

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_request")
    private ItemRequest request; // ссылка на созданную вещь по запросу
}
