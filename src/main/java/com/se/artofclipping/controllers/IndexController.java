package com.se.artofclipping.controllers;

import com.se.artofclipping.services.ServiceService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    ServiceService serviceService;

    public IndexController(ServiceService serviceService) {
        this.serviceService = serviceService;
    }

    @GetMapping({"", "/", "/index"})
    public String home(Model model) {
        model.addAttribute("servicesF", serviceService.listServiceByTypeAndavailability('F', true));
        model.addAttribute("servicesM", serviceService.listServiceByTypeAndavailability('M', true));
        return "index";
    }

    //@TODO make this into static resources
    @GetMapping({"/about"})
    public String about() {
        return "about";
    }
}
