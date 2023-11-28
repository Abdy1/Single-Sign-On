package com.cbo.sso.controllers;

import com.cbo.sso.models.Time;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
public class TimeController {
    @RequestMapping(value="/getDate", method= RequestMethod.GET)
    //@PreAuthorize("hasAnyRole('SUPER_ADMIN','ICMS_BRANCH_MANAGER', 'ICMS_BRANCH', 'ICMS_DISTRICT', 'ADMIN_IC')")
    public @ResponseBody Time getDate() {
        Time time = new Time();
        LocalDate date = LocalDate.now(); // get the current date
//        System.out.println(date);
        time.setTime(date.format(DateTimeFormatter.ofPattern("M/d/yyyy"))); // format the date as a string
        return time;
    }

    @RequestMapping(value="/getDateTime", method= RequestMethod.GET)
    //@PreAuthorize("hasAnyRole('SUPER_ADMIN','ICMS_BRANCH_MANAGER', 'ICMS_BRANCH', 'ICMS_DISTRICT', 'ADMIN_IC')")
    public @ResponseBody Time getDateTime() {
        Time time = new Time();
        LocalDateTime dateTime = LocalDateTime.now(); // get the current date and time
//        System.out.println(dateTime);
        time.setTime(dateTime.format(DateTimeFormatter.ofPattern("M/d/yyyy h:mm a"))); // format the date and time as a string
        return time;
    }
}
