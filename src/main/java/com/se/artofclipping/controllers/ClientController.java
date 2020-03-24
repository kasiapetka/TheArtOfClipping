package com.se.artofclipping.controllers;

import com.se.artofclipping.model.User;
import com.se.artofclipping.model.Visit;
import com.se.artofclipping.services.ClientService;
import com.se.artofclipping.services.VisitService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;

//@TODO check if every authentication is needed
@Slf4j
@Controller
public class ClientController {

    ClientService clientService;
    VisitService visitService;

    public ClientController(ClientService clientService, VisitService visitService) {
        this.clientService = clientService;
        this.visitService = visitService;
    }

    @GetMapping("user/managevisits")
    public String manageVisits(Model model) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User client = clientService.findUserByEmail(auth.getName());
        List<Visit> userVisits = new ArrayList<>();

        clientService.listVisits(client).iterator().forEachRemaining(userVisits::add);

        model.addAttribute("userVisits", userVisits);

        return "user/client/clientManageVisits";
    }

    @GetMapping("/user/client/clientManageVisits/visit/{id}/delete")
    public String deleteVisit(@PathVariable String id, Model model) {
        Visit toDel = visitService.findById(Long.valueOf(id));
        visitService.deleteVisit(toDel);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User client = clientService.findUserByEmail(auth.getName());
        List<Visit> userVisits = new ArrayList<>();
        clientService.listVisits(client).iterator().forEachRemaining(userVisits::add);
        model.addAttribute("userVisits", userVisits);

        return "/user/client/clientManageVisits";
    }

    @GetMapping("user/client/deleteaccount")
    public String deleteAccountForm(Model model) {

        model.addAttribute("passwordUser", new User());

        return "user/client/clientDeleteAccount";
    }

    @PostMapping("/deleteclientaccount")
    public String deleteAccount(@ModelAttribute User passwordUser, Model model) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User client = clientService.findUserByEmail(auth.getName());

        if (clientService.validatePasswordAndEmail(passwordUser, client)) {
            clientService.deleteAccount(client);
            return "redirect:/logout";
        }

        model.addAttribute("passwordUser", new User());
        return "user/client/clientDeleteAccount";
    }

    @GetMapping("user/client/modify")
    public String clientModify(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = clientService.findUserByEmail(auth.getName());
        model.addAttribute("user", user);
        return "user/client/clientModifyProfile";
    }

    @GetMapping("user/client/changeNameView")
    public String clientChangeNameView(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = clientService.findUserByEmail(auth.getName());
        model.addAttribute("user", user);
        model.addAttribute("newUser", new User());
        return "user/client/clientChangeName";
    }

    @PostMapping("user/client/changeName")
    public String clientChangeName(@ModelAttribute User newUser, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = clientService.findUserByEmail(auth.getName());
        clientService.changeName(currentUser, newUser.getName());
        model.addAttribute("user", currentUser);
        return "user/client/clientModifyProfile";
    }

    @GetMapping("user/client/changeSurnameView")
    public String clientChangeSurnameView(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = clientService.findUserByEmail(auth.getName());
        model.addAttribute("user", user);
        model.addAttribute("newUser", new User());
        return "user/client/clientChangeSurname";
    }

    @PostMapping("user/client/changeSurname")
    public String clientChangeSurname(@ModelAttribute User newUser, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = clientService.findUserByEmail(auth.getName());
        clientService.changeSurname(currentUser, newUser.getSurname());
        model.addAttribute("user", currentUser);
        return "user/client/clientModifyProfile";
    }

    @GetMapping("user/client/changeEmailView")
    public String clientChangeEmailView(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = clientService.findUserByEmail(auth.getName());
        model.addAttribute("user", user);
        model.addAttribute("newUser", new User());
        return "user/client/clientChangeEmail";
    }

    @PostMapping("user/client/changeEmail")
    public String clientChangeEmail(@ModelAttribute User newUser, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = clientService.findUserByEmail(auth.getName());

        User userExists = clientService.findUserByEmail(newUser.getEmail());
        if (userExists != null) {
            model.addAttribute("user", currentUser);
            model.addAttribute("newUser", new User());
            return "user/client/clientChangeEmail";
        }

        clientService.changeEmail(currentUser, newUser.getPassword(), newUser.getEmail());

        if (!currentUser.getEmail().equals(newUser.getEmail())) {
            model.addAttribute("user", currentUser);
            model.addAttribute("newUser", new User());
            return "user/client/clientChangeEmail";
        }
        Authentication result = new UsernamePasswordAuthenticationToken(currentUser.getEmail(), currentUser.getPassword());
        SecurityContextHolder.getContext().setAuthentication(result);

        model.addAttribute("user", currentUser);
        return "user/loginForm";
    }

    @GetMapping("user/client/changePasswordView")
    public String clientChangePasswordView(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = clientService.findUserByEmail(auth.getName());
        model.addAttribute("user", user);
        model.addAttribute("newUser", new User());
        return "user/client/clientChangePassword";
    }

    @PostMapping("user/client/changePassword")
    public String clientChangePassword(@ModelAttribute User newUser, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = clientService.findUserByEmail(auth.getName());
        String oldPassword = newUser.getName();

        if (!clientService.changePassword(currentUser, oldPassword, newUser.getPassword())) {
            model.addAttribute("user", currentUser);
            model.addAttribute("newUser", new User());
            return "user/client/clientChangePassword";
        }

        Authentication result = new UsernamePasswordAuthenticationToken(currentUser.getEmail(), currentUser.getPassword());
        SecurityContextHolder.getContext().setAuthentication(result);

        model.addAttribute("user", currentUser);
        return "user/loginForm";
    }
}
