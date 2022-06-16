package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public Collection<User> findAll() {
        return  userService.getUserStorage().getAllUsers();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        return userService.getUserStorage().createUser(user);
    }

    @PutMapping
    public User put(@Valid @RequestBody User user) {
        return userService.getUserStorage().updateUser(user);
    }

    @DeleteMapping
    public void deleteUser(@Valid @RequestBody User user) {
        userService.getUserStorage().deleteUser(user);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public User addFriend(@PathVariable Integer id, @PathVariable Integer friendId) {
        return userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable Integer id, @PathVariable Integer friendId) {
        userService.deleteFriend(id, friendId);
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable Integer id) {
        return userService.getUser(id);
    }

    @GetMapping("/{id}/friends")
    public List<User> getUserFriends(@PathVariable Integer id) {
        return userService.getUserFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getUserFriends(@PathVariable Integer id, @PathVariable Integer otherId) {
        return userService.getUserFriendsCommon(id, otherId);
    }
}