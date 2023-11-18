package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exception.BookingUnavailableItemException;
import ru.practicum.shareit.exception.InvalidDataEnteredException;
import ru.practicum.shareit.exception.LackOfRequestedDataException;
import ru.practicum.shareit.item.dto.comment.CommentDto;
import ru.practicum.shareit.item.dto.item.ItemDto;
import ru.practicum.shareit.item.dto.item.ItemDtoIn;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.CommentRepository;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.storage.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.shareit.booking.dto.BookingDtopMapper.bookingToBookingShort;
import static ru.practicum.shareit.item.dto.comment.CommentDtoMapper.allComentToAllCommentDto;
import static ru.practicum.shareit.item.dto.comment.CommentDtoMapper.commentToCommentDto;
import static ru.practicum.shareit.item.dto.item.ItemDtoMapper.allTemByAllItemDto;
import static ru.practicum.shareit.item.dto.item.ItemDtoMapper.itemDtoInToItem;
import static ru.practicum.shareit.item.dto.item.ItemDtoMapper.itemToDto;

/*
    Сервис для управления предметами
 */
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final ItemRequestRepository itemRequestRepository;

    @Override
    @Transactional
    public ItemDto createItem(Long idOwner, ItemDtoIn item) {
        ItemRequest itemRequest = null;
        if (item.getRequestId() != null) {
            itemRequest = itemRequestRepository.findById(item.getRequestId()).orElseThrow(()
                    -> new LackOfRequestedDataException(String.format("Запроса с id %d не существует", item.getRequestId())));
        }
        User owner = userRepository.findById(idOwner).orElseThrow(()
                -> new LackOfRequestedDataException(String.format("Пользователя с id %d не существует", idOwner)));

        Item endItem = itemDtoInToItem(item, itemRequest);


        endItem.setOwner(owner);
        return itemToDto(itemRepository.save(endItem));
    }

    @Override
    @Transactional
    public ItemDto updateItem(Long idItem, Item item, Long idOfUserBeingEdited) {
        Item updatedItem = itemRepository.findById(idItem).orElseThrow(()
                -> new LackOfRequestedDataException(String.format("Предмета с id %d не существует", idItem)));
        User user = userRepository.findById(idOfUserBeingEdited).orElseThrow(()
                -> new LackOfRequestedDataException(String.format("Пользователя с id %d не существует", idOfUserBeingEdited)));
        if (!updatedItem.getOwner().getId().equals(user.getId())) {
            throw new InvalidDataEnteredException(String.format("У предмета с id = %d не совпадает id владельца = %d",
                    idItem, idOfUserBeingEdited));
        }
        updatedItem.setName(item.getName() == null ? updatedItem.getName() : item.getName());
        updatedItem.setDescription(item.getDescription() == null ? updatedItem.getDescription() : item.getDescription());
        updatedItem.setAvailable((item.getAvailable() == updatedItem.getAvailable()) || (item.getAvailable() == null)
                ? updatedItem.getAvailable() : item.getAvailable());
        return itemToDto(itemRepository.save(updatedItem));
    }

    @Override
    @Transactional
    public ItemDto getItemById(Long itemId, Long idUser) {
        Item item = itemRepository.findById(itemId).orElseThrow(()
                -> new LackOfRequestedDataException(String.format("Предмета с id %d не существует", itemId)));
        return assigningItemOfNextAndLastBooking(item, idUser);
    }

    @Override
    @Transactional
    public List<ItemDto> getItemByUser(Long userId) {
        userRepository.findById(userId).orElseThrow(()
                -> new LackOfRequestedDataException(String.format("Пользователя с id %d не существует", userId)));
        List<Item> allItem = itemRepository.findAllByOwner_IdOrderByIdAsc(userId);
        List<ItemDto> allItemDto = new ArrayList<>();
        for (Item item : allItem)
            allItemDto.add(assigningItemOfNextAndLastBooking(item, userId));
        return allItemDto;
    }

    @Override
    @Transactional
    public List<ItemDto> searchItem(String textQuery) {
        if (textQuery.isBlank()) return new ArrayList<>();
        return allTemByAllItemDto(itemRepository.findAllByAvailableAndDescriptionContainingIgnoreCaseOrNameContainingIgnoreCase(true, textQuery, textQuery));
    }

    @Override
    @Transactional
    public CommentDto createComment(Long itemId, Long userId, Comment comment) {
        Item item = itemRepository.findById(itemId).orElseThrow(()
                -> new LackOfRequestedDataException(String.format("Предмета с id %d не существует", itemId)));
        User user = userRepository.findById(userId).orElseThrow(()
                -> new LackOfRequestedDataException(String.format("Пользователя с id %d не существует", userId)));
        List<Booking> booking = bookingRepository.getBookingByBookerAndItem(item.getId(), user.getId());
        booking = booking.stream().filter(booking1 -> booking1.getStart().isBefore(comment.getCreated())).collect(Collectors.toList());
        if ((booking.isEmpty())) {
            throw new BookingUnavailableItemException(String.format("Пользователь %d не брал в аренду вещь %d", userId, itemId));
        }
        comment.setAuthor(user);
        comment.setItem(item);
        return commentToCommentDto(commentRepository.save(comment));
    }

    /*
        Присвоение предмету следующего и предыдущего бронирования
     */
    private ItemDto assigningItemOfNextAndLastBooking(Item item, Long idUser) {
        ItemDto itemDto = itemToDto(item);
        assigningCommentsByItem(itemDto);
        if (!item.getOwner().getId().equals(idUser)) return itemDto;

        List<Booking> bookings = bookingRepository.findAllByItem(itemDto.getId());
        LocalDateTime timeNow = LocalDateTime.now();

        if (!bookings.isEmpty()) {
            itemDto.setLastBooking(bookingToBookingShort(bookings.stream()
                    .filter(booking -> booking.getStart().isBefore(timeNow)
                            && booking.getStatus().equals(BookingStatus.APPROVED))
                    .min(Booking::compareTo)
                    .orElse(null)));
            itemDto.setNextBooking(bookingToBookingShort(bookings.stream()
                    .filter(booking -> booking.getStart().isAfter(timeNow)
                            && booking.getStatus().equals(BookingStatus.APPROVED))
                    .max(Booking::compareTo)
                    .orElse(null)));
        }
        return itemDto;
    }

    /*
        Присвоение отзывов предмету
     */
    private void assigningCommentsByItem(ItemDto item) {
        List<CommentDto> allComentsDto = allComentToAllCommentDto(commentRepository.findByItemId(item.getId()));
        if (allComentsDto.isEmpty()) {
            allComentsDto = new ArrayList<>();
        }
        item.setComments(allComentsDto);
    }
}