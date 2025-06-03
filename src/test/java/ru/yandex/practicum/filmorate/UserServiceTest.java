package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

public class UserServiceTest {
    UserStorage userStorage = new InMemoryUserStorage();
    UserService userService = new UserService(userStorage);

   @Test
    public void addToFriendsTest() {
       User user = new User();
       User user1 = new User();
       userService.createUser(user);
       userService.createUser(user1);
       userService.addToFriends(user.getId(), user1.getId());
       Assertions.assertEquals(1, userService.getUserById(user.getId()).getFriends().size());
       Assertions.assertEquals(1, userService.getUserById(user1.getId()).getFriends().size());
   }

   @Test
    public void removeFromFriendsTest() {
       User user = new User();
       User user1 = new User();
       userService.createUser(user);
       userService.createUser(user1);
       userService.addToFriends(user.getId(), user1.getId());
       userService.removeFromFriends(user.getId(), user1.getId());
       Assertions.assertEquals(0, userService.getUserById(user.getId()).getFriends().size());
       Assertions.assertEquals(0, userService.getUserById(user1.getId()).getFriends().size());
   }

   @Test
    public void getFriendsTest() {
       User user = new User();
       User user1 = new User();
       User user2 = new User();
       userService.createUser(user);
       userService.createUser(user1);
       userService.createUser(user2);
       userService.addToFriends(user.getId(), user1.getId());
       userService.addToFriends(user.getId(), user2.getId());
       Assertions.assertEquals(2, userService.getListOfFriends(user.getId()).size());
   }

   @Test
    public void getCommonFriendsTest() {
       User user = new User();
       User user1 = new User();
       User user2 = new User();
       userService.createUser(user);
       userService.createUser(user1);
       userService.createUser(user2);
       userService.addToFriends(user.getId(), user1.getId());
       userService.addToFriends(user.getId(), user2.getId());
       Assertions.assertEquals(1, userService.getListOfMutualFriends(user1.getId(), user2.getId()).size());
       Assertions.assertEquals(0, userService.getListOfMutualFriends(user.getId(), user1.getId()).size());
   }
}
