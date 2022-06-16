package ru.yandex.practicum.filmorate.storage.user;
import ru.yandex.practicum.filmorate.model.User;
import java.util.Collection;
import java.util.Map;

public interface UserStorage {

    Map<Integer, User> getUsers();

    Collection<User> getAllUsers();

    User createUser(User user);

    User updateUser(User user);

    void deleteUser(User user);

}
