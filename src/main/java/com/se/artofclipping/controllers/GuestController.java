package com.se.artofclipping.controllers;

import com.se.artofclipping.model.TempVisit;
import com.se.artofclipping.model.User;
import com.se.artofclipping.services.GuestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@Slf4j
public class GuestController {

    private GuestService guestService;
    private TempVisit tempVisit;

    public GuestController(GuestService guestService, TempVisit tempVisit) {
        this.guestService = guestService;
        this.tempVisit = tempVisit;
    }

    //@TODO change this name to smething relevant
    @GetMapping("user")
    public String goTo(Model model) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = guestService.findUserByEmail(auth.getName());
        model.addAttribute("user", user);

        if (guestService.checkIfAdmin(auth.getName())) {
            return "user/admin/adminpage";
        }

        if (guestService.checkIfHairdresser(auth.getName())) {
            return "user/hairdresser/hairdresserPage";
        }

        //@TODO
        // if any of these viarables will be null it will get us to standard user profile
        if (tempVisit.getDay() == null || tempVisit.getTime() == null || tempVisit.getService() == null
                || tempVisit.getService() == null) {

            return "user/client/clientPage";
        }

        return "redirect:/confirmation";

    }

    @GetMapping("login")
    public String login() {
        return "user/loginForm";
    }

    @GetMapping("register")
    public String newUser(Model model) {
        model.addAttribute("user", new User());

        return "user/registerForm";
    }

    @PostMapping("registration")
    public String createNewUser(@ModelAttribute User user, BindingResult bindingResult,
                                Model model) {
        log.debug(user.getEmail());
        User userExists = guestService.findUserByEmail(user.getEmail());
        if (userExists != null) {
            bindingResult
                    .rejectValue("email", "error.user",
                            "There is already a user registered with the email provided");
        }
        if (bindingResult.hasErrors()) {
            return "user/registerForm";
        } else {
            guestService.saveUser(user);
            model.addAttribute("user", new User());
            return "user/loginForm";
        }
    }
}
