package ru.practicum.shareit.item.dto.comment;

import ru.practicum.shareit.item.model.Comment;

import java.util.ArrayList;
import java.util.List;

/*
    Маппер отзывов
*/
public class CommentDtoMapper {
    public static CommentDto commentToCommentDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .authorName(comment.getAuthor().getName())
                .item(comment.getItem())
                .text(comment.getText())
                .created(comment.getCreated())
                .build();
    }

    public static List<CommentDto> allComentToAllCommentDto(List<Comment> allComment) {
        List<CommentDto> allCommentDto = new ArrayList<>();
        for (Comment com : allComment) {
            allCommentDto.add(commentToCommentDto(com));
        }
        return allCommentDto;
    }
}
