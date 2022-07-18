package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FriendStorage {
    User addFriend(Integer userId, Integer friendId);
    void deleteFriend(Integer userId, Integer friendId);
    List<User> getUserFriends(Integer userId);
    List<User> getCommonFriends(Integer userId, Integer otherId);
}
