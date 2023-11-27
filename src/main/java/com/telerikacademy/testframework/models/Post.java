package com.telerikacademy.testframework.models;

import com.telerikacademy.testframework.utils.Helpers;
import com.telerikacademy.testframework.utils.Utils;
import com.telerikacademy.testframework.utils.Visibility;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Getter
@Setter
public class Post {

    private User author;
    private LocalDateTime creationDateTime;
    private String content;
    private Visibility visibility;
    private int likes;


    public Post(User author, String content, Visibility visibility) {
        setAuthor(author);
        setContent(content);
        setVisibility(visibility);
        setCreationDateTime(LocalDateTime.now(ZoneId.of(Utils.getConfigPropertyByKey("weare.timeZone"))));
        likes = 0;
    }
}
