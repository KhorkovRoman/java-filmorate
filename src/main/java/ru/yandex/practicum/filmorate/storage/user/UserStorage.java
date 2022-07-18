package ru.yandex.practicum.filmorate.storage.user;
import ru.yandex.practicum.filmorate.model.User;
import java.util.Collection;
import java.util.List;

public interface UserStorage {
    User createUser(User user);
    User getUserById(Integer userId);
    Collection<User> getAllUsers();
    User updateUser(User user);
    void deleteUser(User user);
}
