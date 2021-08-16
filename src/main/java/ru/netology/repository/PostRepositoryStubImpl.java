package ru.netology.repository;

import org.springframework.stereotype.Repository;
import ru.netology.exception.NotFoundException;
import ru.netology.model.Post;

import java.util.*;

@Repository
public class PostRepositoryStubImpl implements PostRepository {
  private static Map<Long, Post> db = new HashMap<>();
  private static long allPostsCounter = 0;

  public List<Post> all() {
    return new ArrayList<>(db.values());
  }

  public Optional<Post> getById(long id) {
    if (db.keySet().contains(id)) {
      return Optional.of(db.get(id));
    }
    return Optional.empty();
  }

  public synchronized Post save(Post post){
    long postId = post.getId();
    if (postId == 0) {
      allPostsCounter++;
      db.put(allPostsCounter, post);
    }
    else {
      Optional<Post> postForChange = getById(postId);
      if (postForChange.isPresent() && db.keySet().contains(postId)) {
        db.put(postForChange.get().getId(), post);
      }
      else throw new NotFoundException("Невозможно изменить пост, id не найден");
    }
    return post;
  }

  public synchronized void removeById(long id) {
    Optional<Post> postForChange = getById(id);
    if (postForChange.isPresent() && db.keySet().contains(id)) {
      db.remove(id);
    }
    else throw new NotFoundException("id не найден");
  }
}