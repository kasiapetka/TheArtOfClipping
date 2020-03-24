package com.se.artofclipping.controllers;

import com.se.artofclipping.model.User;
import com.se.artofclipping.model.Visit;
import com.se.artofclipping.services.HairdresserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;

//@Todo also check if every authentitacion is needed
@Slf4j
@Controller
public class HairdresserController {

    HairdresserService hairdresserService;

    public HairdresserController(HairdresserService hairdresserService) {
        this.hairdresserService = hairdresserService;
    }

    @GetMapping("user/hairdresser/showvisits")
    public String showVisits(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User hairdresser = hairdresserService.findUserByEmail(auth.getName());
        List<Visit> hairdresserVisits = new ArrayList<>();

        hairdresserService.listVisits(hairdresser).iterator().forEachRemaining(hairdresserVisits::add);

        model.addAttribute("hairdresserVisits", hairdresserVisits);

        return "user/hairdresser/hairdresserShowVisits";
    }

    @GetMapping("user/hairdresser/modify")
    public String hairdresserModify(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = hairdresserService.findUserByEmail(auth.getName());
        model.addAttribute("user", user);
        return "user/hairdresser/hairdresserModifyProfile";
    }

    @GetMapping("user/hairdresser/changeNameView")
    public String hairdresserChangeNameView(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = hairdresserService.findUserByEmail(auth.getName());
        model.addAttribute("user", user);
        model.addAttribute("newUser", new User());
        return "user/hairdresser/hairdresserChangeName";
    }

    @PostMapping("user/hairdresser/changeName")
    public String hairdresserChangeName(@ModelAttribute User newUser, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = hairdresserService.findUserByEmail(auth.getName());

        hairdresserService.changeName(currentUser, newUser.getName());
        model.addAttribute("user", currentUser);
        return "user/hairdresser/hairdresserModifyProfile";
    }

    @GetMapping("user/hairdresser/changeSurnameView")
    public String hairdresserChangeSurnameView(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = hairdresserService.findUserByEmail(auth.getName());
        model.addAttribute("user", user);
        model.addAttribute("newUser", new User());
        return "user/hairdresser/hairdresserChangeSurname";
    }

    @PostMapping("user/hairdresser/changeSurname")
    public String hairdresserChangeSurname(@ModelAttribute User newUser, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = hairdresserService.findUserByEmail(auth.getName());
        hairdresserService.changeSurname(currentUser, newUser.getSurname());
        model.addAttribute("user", currentUser);
        return "user/hairdresser/hairdresserModifyProfile";
    }

    @GetMapping("user/hairdresser/changeEmailView")
    public String hairdresserChangeEmailView(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = hairdresserService.findUserByEmail(auth.getName());
        model.addAttribute("user", user);
        model.addAttribute("newUser", new User());
        return "user/hairdresser/hairdresserChangeEmail";
    }

    @PostMapping("user/hairdresser/changeEmail")
    public String hairdresserChangeEmail(@ModelAttribute User newUser, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = hairdresserService.findUserByEmail(auth.getName());

        User userExists = hairdresserService.findUserByEmail(newUser.getEmail());
        if (userExists != null) {
            model.addAttribute("user", currentUser);
            model.addAttribute("newUser", new User());
            return "user/hairdresser/hairdresserChangeEmail";
        }

        hairdresserService.changeEmail(currentUser, newUser.getPassword(), newUser.getEmail());

        if (!currentUser.getEmail().equals(newUser.getEmail())) {
            model.addAttribute("user", currentUser);
            model.addAttribute("newUser", new User());
            return "user/hairdresser/hairdresserChangeEmail";
        }
        Authentication result = new UsernamePasswordAuthenticationToken(currentUser.getEmail(), currentUser.getPassword());
        SecurityContextHolder.getContext().setAuthentication(result);

        model.addAttribute("user", currentUser);
        return "user/loginForm";
    }

    @GetMapping("user/hairdresser/changePasswordView")
    public String hairdresserChangePasswordView(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = hairdresserService.findUserByEmail(auth.getName());
        model.addAttribute("user", user);
        model.addAttribute("newUser", new User());
        return "user/hairdresser/hairdresserChangePassword";
    }

    @PostMapping("user/hairdresser/changePassword")
    public String hairdresserChangePassword(@ModelAttribute User newUser, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = hairdresserService.findUserByEmail(auth.getName());
        String oldPassword = newUser.getName();

        if (!hairdresserService.changePassword(currentUser, oldPassword, newUser.getPassword())) {
            model.addAttribute("user", currentUser);
            model.addAttribute("newUser", new User());
            return "user/hairdresser/hairdresserChangePassword";
        }

        Authentication result = new UsernamePasswordAuthenticationToken(currentUser.getEmail(), currentUser.getPassword());
        SecurityContextHolder.getContext().setAuthentication(result);

        model.addAttribute("user", currentUser);
        return "user/loginForm";
    }
}
