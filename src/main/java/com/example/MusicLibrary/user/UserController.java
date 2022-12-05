package com.example.MusicLibrary.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(path = "api/v1/music/user")
public class UserController {
    public final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getAllUsers(){
        return userService.getUsers();
    }

    @GetMapping("{user_id}")
    public User getUserById(@PathVariable("user_id") Long id){
        return userService.getUserById(id);
    }

    @PostMapping
    public User createUser(@RequestBody User user){
        return userService.createUser(user);
    }

    @PutMapping(path = "{user_Id}")
    public void updateUser(
            @PathVariable("user_Id") Long user_Id,
            @RequestParam(required = false) String userName,
            @RequestParam(required = false) String password,
            @RequestParam(required = false) String role){
        userService.updateUser(user_Id, userName, password, role);
    }

    @DeleteMapping(path = "{user_Id}")
    public void deleteUser(@PathVariable("user_Id") Long user_Id){
        userService.deleteUser(user_Id);
    }
}