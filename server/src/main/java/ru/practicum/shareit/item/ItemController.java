package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.comment.CommentDto;
import ru.practicum.shareit.item.dto.item.ItemDto;
import ru.practicum.shareit.item.dto.item.ItemDtoIn;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.service.ItemServiceImpl;

import java.util.List;

/*
    Контроллер для управления запросами предметов
 */
@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemServiceImpl service;

    /*
        Создать новый предмет
     */
    @PostMapping
    public ItemDto createItem(@RequestHeader("X-Sharer-User-Id") Long userId, @RequestBody ItemDtoIn item) {
        log.info("Выполняется POST-запрос. Создание нового предмета.");
        return service.createItem(userId, item);
    }

    /*
        Обновить информацию предмета
     */
    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@PathVariable Long itemId, @RequestBody ItemDtoIn item, @RequestHeader("X-Sharer-User-Id") Long idUser) {
        log.info("Выполняется PATCH-запрос. Обновление существующего предмета.");
        return service.updateItem(itemId, item, idUser);
    }

    /*
        Получить предмет по id
     */
    @GetMapping("/{itemId}")
    public ItemDto getItemById(@PathVariable Long itemId, @RequestHeader("X-Sharer-User-Id") Long idUser) {
        log.info("Выполняется GET-запрос. Получение предмета по id.");
        return service.getItemById(itemId, idUser);
    }

    /*
        Получить все предметы пользователя
    */
    @GetMapping
    public List<ItemDto> getItemByUser(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Выполняется GET-запрос. Получение предметов пользователя.");
        return service.getItemByUser(userId);
    }

    /*
        Найти предмет по названию или описанию
     */
    @GetMapping("/search")
    public List<ItemDto> searchItem(@RequestParam("text") String textQuery) {
        log.info("Выполняется GET-запрос. Получение предмета по отрезку фразы.");
        return service.searchItem(textQuery);
    }

    /*
        Создать новый отзыв
     */
    @PostMapping("/{itemId}/comment")
    public CommentDto createComment(@PathVariable Long itemId, @RequestHeader("X-Sharer-User-Id") Long userId,
                                    @RequestBody Comment comment) {
        log.info("Выполняется POST-запрос. Добавить комментарий.");
        return service.createComment(itemId, userId, comment);
    }
}