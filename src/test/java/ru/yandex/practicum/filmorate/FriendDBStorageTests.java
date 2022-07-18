package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.FriendStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:test.sql")
public class FriendDBStorageTests {

    private User user;
    private User friend;
    private User commonFriend;
    private final UserStorage userStorage;
    private final FriendStorage friendStorage;

    @BeforeEach
    void initFields() {
        user = new User(1, "User@email.com", "UserLogin", "UserName",
                LocalDate.of(2005, 12, 20));
        friend = new User(2, "Friend@email.com", "FriendLogin", "FriendName",
                LocalDate.of(2003, 05, 15));
        commonFriend = new User(3, "CommonFriend@email.com", "CommonFriendLogin",
                "CommonFriendName", LocalDate.of(2000, 04, 11));

        userStorage.createUser(user);
        userStorage.createUser(friend);
        userStorage.createUser(commonFriend);
    }

    @Test
    @Order(6)
    public void testAddFriend() {
        friendStorage.addFriend(user.getId(), friend.getId());
        List<User> users = friendStorage.getUserFriends(1);
        assertEquals(1, users.size());
    }

    @Test
    @Order(7)
    public void testDeleteFriend() {
        friendStorage.addFriend(user.getId(), friend.getId());
        List<User> friends = friendStorage.getUserFriends(1);
        assertEquals(1, friends.size());

        friendStorage.deleteFriend(user.getId(), friend.getId());
        friends = friendStorage.getUserFriends(1);
        assertEquals(0, friends.size());
    }

    @Test
    @Order(8)
    public void testGetUserFriends() {
        friendStorage.addFriend(user.getId(), friend.getId());
        List<User> users = friendStorage.getUserFriends(1);
        assertEquals(1, users.size());
    }

    @Test
    @Order(9)
    public void testGetCommonFriends() {
        friendStorage.addFriend(user.getId(), commonFriend.getId());
        friendStorage.addFriend(friend.getId(), commonFriend.getId());
        List<User> users = friendStorage.getCommonFriends(user.getId(), friend.getId());
        assertEquals(1, users.size());
    }
}
