package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.FriendStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;

@Slf4j
@Service
public class FriendService {

    private final UserStorage userStorage;
    private final FriendStorage friendStorage;

    @Autowired
    public FriendService(UserStorage userStorage, FriendStorage friendStorage) {
        this.userStorage = userStorage;
        this.friendStorage = friendStorage;
    }

    public User addFriend(Integer userId, Integer friendId) {
        validateUser(userId);
        validateUser(friendId);
        return friendStorage.addFriend(userId, friendId);
    }

    public void deleteFriend(Integer userId, Integer friendId) {
        validateUser(userId);
        validateUser(friendId);
        friendStorage.deleteFriend(userId, friendId);
    }

    public List<User> getUserFriends(Integer userId) {
        validateUser(userId);
        return friendStorage.getUserFriends(userId);
    }

    public List<User> getCommonFriends(Integer userId, Integer otherId) {
        validateUser(userId);
        validateUser(otherId);
        return friendStorage.getCommonFriends(userId, otherId);
    }

    public void validateUser(Integer userId) {
        User user = userStorage.getUserById(userId);
        if (user == null) {
            throw new ValidationException(HttpStatus.NOT_FOUND,
                    "Пользователя c id " + userId + " нет в базе.");
        }
    }
}
