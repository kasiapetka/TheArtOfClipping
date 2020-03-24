package com.se.artofclipping.controllers;

import com.se.artofclipping.model.Service;
import com.se.artofclipping.model.TempVisit;
import com.se.artofclipping.model.User;
import com.se.artofclipping.model.Visit;
import com.se.artofclipping.services.AdminService;
import com.se.artofclipping.services.ServiceService;
import com.se.artofclipping.services.VisitService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//@Todo this whole class needs to be rewritten :)
// How does it work for now?
// This class has tempVisit as session variable (should be just visit)
// After chosing service its passed via url parameter to showAvailable (great name)
// then app stays on the same page and let you chose correct date via url
// EVERY CORRECT VALUE IS INSTANTLY SIGNED TO TEMPVISIT
// then CalendarSitePost is returned. This function posts data about
// chosen hairdresser. When it gets hairdressers its create list of all possible times
// and remove these that are not allowed. When times are correctly removed
// they are passed to the model. After chosing time you are redirected to
// confirmation site. If you are logged you are allowed to make reservation. If youre
// not you will be redirected to login page.
@Slf4j
@Controller
@SessionAttributes("tempVisit")
public class VisitController {

    ServiceService serviceService;
    AdminService adminService;
    VisitService visitService;

    private TempVisit tempVisit;

    public VisitController(ServiceService serviceService, AdminService adminService,
                           VisitService visitService, TempVisit tempVisit) {
        this.serviceService = serviceService;
        this.adminService = adminService;
        this.visitService = visitService;
        this.tempVisit = tempVisit;
    }


    @GetMapping("rsvr/")
    public String showAvailable(@RequestParam String id,
                                @RequestParam String date,
                                Model model) {

        Service chosen = serviceService.findById(Long.parseLong(id));

        model.addAttribute("service", chosen);
        model.addAttribute("hairdresser", new User());

        if (id != null) {
            tempVisit.setService(serviceService.findById(Long.parseLong(id)));
        }

        //@TODO How to write unclean code pt1 at least select works
        //its added for select to work
        User optionUser = new User();
        optionUser.setName("Chose");
        optionUser.setSurname("Hairdresser");
        optionUser.setEmail("optionUser");

        List<User> hdsAndChoseOption = adminService.listHairdressers();
        hdsAndChoseOption.add(0, optionUser);

        if (!date.equals("")) {
            System.out.println(date);
            tempVisit.setDay(date);
            model.addAttribute("listHds", hdsAndChoseOption);
            model.addAttribute("timeVisit", new Visit());
            return "calendar/calendarSitePost";
        } else {
            model.addAttribute("listHds", new ArrayList<>());
        }

        return "calendar/calendarSite";
    }


    @PostMapping("rsvr/chosetime")
    public String time(@ModelAttribute User hairdresser,
                       @ModelAttribute Visit timeVisit,
                       Model model) {
        String sDate1 = tempVisit.getDay();
        Date date1;
        try {
            date1 = new SimpleDateFormat("dd-MM-yyyy").parse(sDate1);
        } catch (Exception e) {
            return "calendar/calendarSite";
        }

        String date = date1.toString();
        String tab[] = date.split(" ");

        String finalDate = tab[0] + " " + tab[1] + " " + tab[2] + " " + tab[5];
        model.addAttribute("date", finalDate);

        // User 'optionUser' is just another value in select for html
        // for some reason 'disable selected' didn't work
        if (hairdresser.getEmail().equals("optionUser")) {
            User optionUser = new User();
            optionUser.setName("Chose");
            optionUser.setSurname("Hairdresser");
            optionUser.setEmail("optionUser");
            tempVisit.setHairDresser(optionUser);
            List<User> hdsAndChoseOption = adminService.listHairdressers();
            hdsAndChoseOption.add(0, optionUser);
            model.addAttribute("listHds", hdsAndChoseOption);
            model.addAttribute("hairdresser", new User());
            model.addAttribute("timeVisit", new Visit());
            return "calendar/calendarSitePost";
        }

        //@TODO forgot to change it to bean...
        List<String> times = new ArrayList<>();
        times.add("10:00");
        times.add("10:30");
        times.add("11:00");
        times.add("11:30");
        times.add("12:00");
        times.add("12:30");
        times.add("13:00");
        times.add("13:30");
        times.add("14:00");
        times.add("14:30");
        times.add("15:00");
        times.add("15:30");
        times.add("16:00");
        times.add("16:30");
        times.add("17:00");
        times.add("17:30");

        List<Visit> availableVisits = visitService.findByDay(tempVisit.getDay());
        List<Visit> visitsToRemove = new ArrayList<>();

        // removing visits for another hairdressers
        for (Visit visit : availableVisits) {
            if (!visit.getHairDresser().getEmail().equals(hairdresser.getEmail())) {
                visitsToRemove.add(visit);
            }
        }

        availableVisits.removeAll(visitsToRemove);

        List<String> timesToRemove = new ArrayList<>();
        Integer currentVisitOffset;
        Integer chosenVisitOffset;

        //@Todo no words here :D
        for (int i = 0; i < times.size(); i++) {
            for (Visit visit : availableVisits) {
                if (times.get(i).equals(visit.getTime())) {
                    timesToRemove.add(times.get(i));

                    // removing times ahead for every visit longer than 30 minutes
                    currentVisitOffset = visit.getService().getDurationMinutes() / 30;
                    for (int j = 0; j < currentVisitOffset; j++) {
                        if (!(i + j > times.size())) {
                            timesToRemove.add(times.get(i + j));
                        }
                    }

                    // removing times before for current service time
                    chosenVisitOffset = tempVisit.getService().getDurationMinutes() / 30;
                    for (int j = 0; j < chosenVisitOffset; j++) {
                        if (!(i - j < 0)) {
                            timesToRemove.add(times.get(i - j));
                        }
                    }
                }
            }
        }

        // removing timest from the end for current service
        chosenVisitOffset = (tempVisit.getService().getDurationMinutes() / 30) - 1;

        for (int j = times.size() - chosenVisitOffset; j < times.size(); j++) {
            timesToRemove.add(times.get(j));
        }

        times.removeAll(timesToRemove);

        List<Visit> visitTimesOnly = new ArrayList<>();
        Visit visitTime;

        // just made to pass list of visits to model
        // for some reason thymeleaf has problem with pure Strings or Im doing it wrong :(
        for (int i = 0; i < times.size(); i++) {
            visitTime = new Visit();
            visitTime.setTime(times.get(i));
            visitTimesOnly.add(visitTime);
        }

        // visit that will store only time
        model.addAttribute("times", times);
        model.addAttribute("timeVisit", new Visit());

        User hds = adminService.findUserByEmail(hairdresser.getEmail());
        model.addAttribute("hairdresser", hds);

        tempVisit.setHairDresser(hds);

        //@TODO How to write unclean code pt3
        User optionUser = new User();
        optionUser.setName("Chose");
        optionUser.setSurname("Hairdresser");
        optionUser.setEmail("optionUser");

        List<User> hdsAndChoseOption = adminService.listHairdressers();
        hdsAndChoseOption.add(0, optionUser);
        model.addAttribute("listHds", hdsAndChoseOption);

        return "calendar/calendarSitePost";
    }

    @PostMapping("rsvr/timechosen")
    public String timeChosen(@ModelAttribute Visit timeVisit) {

        tempVisit.setTime(timeVisit.getTime());

        return "redirect:/confirmation";
    }

    @GetMapping("/confirmation")
    public String confirmation(Model model) {

        Visit newVisit = tempVisit.convertToVisit();

        model.addAttribute("visit", newVisit);

        return "calendar/confirmation";
    }

    //@Todo im not even mad at this point
    @GetMapping("/37shbdngh8b67sdnas86vb5")
    public String reserve() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = adminService.findUserByEmail(auth.getName());
        tempVisit.setClient(user);

        Visit newVisit = tempVisit.convertToVisit();
        visitService.addNewVisit(newVisit);

        tempVisit.setTime(null);
        tempVisit.setHairDresser(null);
        tempVisit.setService(null);
        tempVisit.setClient(null);
        tempVisit.setDay(null);

        //@TODO change to redirection to user's visits later
        // on our scheme its redirected to index so let it be
        return "redirect:/";
    }
}
