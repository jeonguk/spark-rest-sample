package dao;

import com.jeonguk.model.Post;

public class PostRepository  extends HibernateAbstractDao<Post, Integer> {

    @Override
    public Class<?> getClazz() {
        return Post.class;
    }

}