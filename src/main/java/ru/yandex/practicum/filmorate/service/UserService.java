package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public Collection<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public User createUser(User user) {
        return userStorage.createUser(user);
    }

    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }

    public void deleteUser(User user) {
        userStorage.deleteUser(user);
    }

    public User addFriend(Integer userId, Integer friendId) {
        if (userStorage.getUsers().containsKey(userId)) {
            if (userStorage.getUsers().containsKey(friendId)) {

                User user = userStorage.getUserById(userId);
                Set<Integer> userFriends = user.getFriends();
                userFriends.add(friendId);
                log.info("Пользователь c id " + friendId + " успешно записан " +
                        "в друзья к " + user + ".");

                User friendUser = userStorage.getUserById(friendId);
                Set<Integer> friendUserFriends = friendUser.getFriends();
                friendUserFriends.add(userId);

                log.info("Пользователь c id " + userId + " успешно записан " +
                        "в друзья к " + friendUser + ".");

                return user;
            } else {
                throw new ValidationException(HttpStatus.NOT_FOUND,
                        "Пользователя c id " + friendId + " нет в базе.");
            }
        } else {
            throw new ValidationException(HttpStatus.NOT_FOUND,
                    "Пользователя c id " + userId + " нет в базе.");
        }
    }

    public void deleteFriend(Integer userId, Integer friendId) {
        if (userStorage.getUsers().containsKey(userId)) {
            if (userStorage.getUsers().containsKey(friendId)) {

                User user = userStorage.getUserById(userId);
                Set<Integer> userFriends = user.getFriends();
                userFriends.remove(friendId);
                log.info("Пользователь c id " + friendId + " успешно удален " +
                        "из друзей " + user + ".");

                User userFriend = userStorage.getUserById(friendId);
                Set<Integer> userFriendFriends = userFriend.getFriends();
                userFriendFriends.remove(userId);
                log.info("Пользователь c id " + userId + " успешно удален " +
                        "из друзей " + userFriend + ".");

            } else {
                throw new ValidationException(HttpStatus.NOT_FOUND,
                        "Пользователя c id " + friendId + " нет в базе.");
            }
        } else {
            throw new ValidationException(HttpStatus.NOT_FOUND,
                    "Пользователя c id " + userId + " нет в базе.");
        }
    }

    public User getUser(Integer userId) {
        if (userStorage.getUsers().containsKey(userId)) {
            User user = userStorage.getUserById(userId);
            log.info("Пользователь c id " + userId + " найден в базе.");
            return user;
        } else {
            throw new ValidationException(HttpStatus.NOT_FOUND,
                    "Пользователя c id " + userId + " нет в базе.");
        }
    }

    public List<User> getUserFriends(Integer userId) {
        if (userStorage.getUsers().containsKey(userId)) {
            User user = userStorage.getUserById(userId);
            Set<Integer> userFriends = user.getFriends();

            List<User> userList = new ArrayList<>();
            for (Integer i: userFriends) {
                User userToAdd = userStorage.getUserById(i);
                userList.add(userToAdd);
            }

            log.info("Список пользователей выведен: " + userList);
            return userList;
        } else {
            throw new ValidationException(HttpStatus.NOT_FOUND,
                    "Пользователя c id " + userId + " нет в базе.");
        }
    }

    public List<User> getUserFriendsCommon(Integer userId, Integer otherId) {
        if (userStorage.getUsers().containsKey(userId)) {
            if (userStorage.getUsers().containsKey(otherId)) {
                User user = userStorage.getUserById(userId);
                Set<Integer> userFriends = user.getFriends();
                List<User> userList = new ArrayList<>();
                for (Integer i: userFriends) {
                    User userToAdd = userStorage.getUsers().get(i);
                    userList.add(userToAdd);
                }

                User otherUser = userStorage.getUserById(otherId);
                Set<Integer> otherUserFriends = otherUser.getFriends();
                List<User> otherUserFriendList = new ArrayList<>();
                for (Integer i: otherUserFriends) {
                    User userToAdd = userStorage.getUserById(i);
                    otherUserFriendList.add(userToAdd);
                }

                List<User> matches = userList.stream()
                        .filter(otherUserFriendList::contains)
                        .collect(Collectors.toList());

                log.info("Список пользователей выведен." + matches);
                return matches;
            } else {
                throw new ValidationException(HttpStatus.NOT_FOUND,
                        "Пользователя c id " + otherId + " нет в базе.");
            }
        } else {
            throw new ValidationException(HttpStatus.NOT_FOUND,
                    "Пользователя c id " + userId + " нет в базе.");
        }
    }
}
