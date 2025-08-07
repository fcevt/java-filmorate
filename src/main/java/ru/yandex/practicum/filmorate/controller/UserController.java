package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        log.info("Create user: {}", user);
        return userService.createUser(user);
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        log.info("Update user: {}", user);
        return userService.updateUser(user);
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable long id) {
        log.info("Get user by id: {}", id);
        return userService.getUserById(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable long id, @PathVariable long friendId) {
        log.info("Add friend with id: {}, friendId: {}", id, friendId);
        userService.addToFriends(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable long id, @PathVariable long friendId) {
        log.info("Delete friend with id: {}, friendId: {}", id, friendId);
        userService.removeFromFriends(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriends(@PathVariable long id) {
        log.info("Get friends by id: {}", id);
        return userService.getListOfFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getFriendsCommon(@PathVariable long otherId, @PathVariable long id) {
        log.info("Get common friends by id: {}, otherId: {}", id, otherId);
        return userService.getListOfMutualFriends(id, otherId);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable int id) {
        log.info("Delete user by id: {}", id);
        userService.deleteUserById(id);
    }

    @GetMapping("/{id}/feed")
    public List<Event> getFeed(@PathVariable long id) {
        log.info("Get feed by userId: {}", id);
        return userService.getEventsByUserId(id);
    }

    @GetMapping("/{id}/recommendations")
    public List<Film> getRecommendations(@PathVariable long id) {
        log.info("Get recommendations for userId: {}", id);
        return userService.getRecommendations(id);
    }
}
