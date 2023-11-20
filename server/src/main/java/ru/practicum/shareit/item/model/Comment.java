package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/*
    Сущность отзыва для связи с базой данных
*/
@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_comment")
    private Long id;

    @NotNull
    @NotEmpty
    @Column(name = "text_comment")
    private String text;

    @JoinColumn(name = "id_item_comment")
    @ManyToOne(fetch = FetchType.LAZY)
    private Item item;

    @JoinColumn(name = "id_author_comment")
    @ManyToOne(fetch = FetchType.LAZY)
    private User author;

    @Column(name = "date_created_comment")
    private LocalDateTime created = LocalDateTime.now();
}
