package ru.practicum.shareit.user.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
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
    @Column(name = "email")
    private String email; // email пользователя
}
