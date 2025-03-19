package pl.wsis.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import pl.wsis.domain.Role;
import pl.wsis.domain.User;
import pl.wsis.dto.UserDTO;
import pl.wsis.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
// Любой пользователь у которого есть canBlockUser(Permission) не может блокнуть сам себя
// Любой пользователь у которого есть canBlockUser(Permission) не может блокнуть пользователя с ролью ADMIN.
public class UserController {
    private final UserService userService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getAll() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

//    @PreAuthorize("hasAuthority('canBlockUser') or hasAnyAuthority('ADMIN', 'MANAGER')")
//    @PostMapping("/users/block")
//    public ResponseEntity<String> blockUser(@RequestParam String email){
//        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
//        User currentUser = userService.findByEmail(currentUserEmail);
//        User forBlock = userService.findByEmail(email);
//        if (currentUser.getEmail().equals(email)) {
//            return ResponseEntity.badRequest().body("Нельзя заблокировать самого себя");
//        }
//        if ("ADMIN".equals(forBlock.getRole().getName())) {
//            return ResponseEntity.badRequest().body("Нельзя заблокировать пользователя с ролью ADMIN");
//        }
//        userService.blockUser(forBlock);
//        return ResponseEntity.ok("Пользователь заблокирован");
//    }


    @PreAuthorize("hasAuthority('canBlockUser') or hasAnyAuthority('ADMIN', 'MANAGER')")
    @PostMapping("/users/block")
    public ResponseEntity<String> blockUser(@RequestParam String email){
        return ResponseEntity.ok(userService.blockUser(email));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    @PostMapping("/users/add-manager")
    public ResponseEntity<String> addManager(@RequestParam String email){
       return ResponseEntity.ok(userService.addManager(email));
    }
}
