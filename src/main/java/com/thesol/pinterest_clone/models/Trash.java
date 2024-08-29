package com.thesol.pinterest_clone.models;

import java.util.ArrayList;
import java.util.List;

public class Trash {

    // Алгоритм получения постов в ленту для пользователя
    public List<Post> getFeedForUser(User user) {
        List<Post> feed = new ArrayList<>();
        for (User subscription : user.getFollowing()) {
            feed.addAll(subscription.getPosts());
        }
        return feed;
    }


}
