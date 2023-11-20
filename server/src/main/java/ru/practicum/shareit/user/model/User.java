package ru.practicum.shareit.user.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

/*
    Объект пользователей в базе данных
 */
@Data
@Builder
@EqualsAndHashCode(of = {"userId"})
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @Column(name = "id_user")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // id пользователя

    @NotNull
    @Column(name = "name_user")
    private String name; // имя пользователя

    @NotNull
    @Email(message = "Электронная почта не может быть пустой и должна содержать символ @")
    @Column(name = "email", unique = true)

    private String email; // email пользователя
}
