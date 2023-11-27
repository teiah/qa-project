package factories;

import com.telerikacademy.testframework.models.Post;
import com.telerikacademy.testframework.models.User;
import com.telerikacademy.testframework.utils.Helpers;
import com.telerikacademy.testframework.utils.Visibility;

public class PostFactory {

    public static Post createPost(User author, Visibility visibility) {
        return new Post(
                author,
                Helpers.generatePostContent(),
                visibility
        );
    }
}
