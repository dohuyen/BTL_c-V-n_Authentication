package com.kma.ap1.huyendtt.controller;

import javax.validation.Valid;

import com.kma.ap1.huyendtt.config.SecurityConfig;
import com.kma.ap1.huyendtt.config.WebMvcConfig;
import com.kma.ap1.huyendtt.model.ChangePasswordRequest;
import com.kma.ap1.huyendtt.model.User;
import com.kma.ap1.huyendtt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @RequestMapping(value= {"/login"}, method=RequestMethod.GET)
    public ModelAndView login() {
        ModelAndView model = new ModelAndView();

        model.setViewName("user/login");    //resources/template/login.html
        return model;
    }

    @RequestMapping(value = {"/register"}, method = RequestMethod.GET)
    public ModelAndView register() {
        ModelAndView model = new ModelAndView();

        User user = new User();
        model.addObject("user", user);
        model.setViewName("user/register");  //resources/template/register.html

        return model;
    }

    @RequestMapping(value= {"/register"}, method=RequestMethod.POST)
    public ModelAndView registerUser(@Valid User user, BindingResult bindingResult, ModelMap modelMap) {
        ModelAndView model = new ModelAndView();
        User userExists = userService.findUserByEmail(user.getEmail());

        if(userExists != null) {
            bindingResult.rejectValue("email", "error.user", "This email already exists!");
        }
        if(bindingResult.hasErrors()) {
            model.setViewName("user/register");
            modelMap.addAttribute("bindingResult", bindingResult);

        } else {
            userService.saveUser(user);
            model.addObject("msg", "User has been registered successfully!");
            model.addObject("user", new User());
            model.setViewName("user/login");
        }

        return model;
    }

    @RequestMapping(value = {"/change_password"}, method = RequestMethod.GET)
    public ModelAndView getViewChangePassword() {
        ModelAndView model = new ModelAndView();
        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest();

        model.addObject("changePasswordRequest", changePasswordRequest);
        model.setViewName("user/change_password");  //resources/template/register.html

        return model;
    }

    @RequestMapping(value= {"/change-password"}, method=RequestMethod.POST)
    public ModelAndView changePassword(@Valid ChangePasswordRequest changePasswordRequest, BindingResult bindingResult) {
        ModelAndView model = new ModelAndView();
        User currentUser = userService.findUserByEmail(changePasswordRequest.getUsername());

        if (bindingResult.hasErrors()) {
            model.setViewName("user/change_password");
        } else {
            String currentPassword = currentUser.getPassword();

            userService.saveUser(user);
            model.addObject("msg", "User has been registered successfully!");
            model.setViewName("user/change-password");


            String dbPassword = currentUser.getPassword();

            if (null != currentPassword)
                if (passwordEncoder.matches(currentPassword, dbPassword)) {
                    if (newPassword != null && !newPassword.isEmpty() && !newPassword.equals("")) {
                        currentUser.setPassword(passwordEncoder.encode(newPassword));
                    }
                    currentUser.setEmail(email);
                } else {
                    return new ResponseEntity<>("Incorrect current password!", HttpStatus.BAD_REQUEST);
                }


            currentUser.setFirstName(firstName);
            currentUser.setLastName(lastName);
            currentUser.setUsername(username);


            userService.save(currentUser);


            return model;
        }
    }



    @RequestMapping(value= {"/home"}, method=RequestMethod.GET)
    public ModelAndView home() {
        ModelAndView model = new ModelAndView();

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        model.addObject("userName", user.getFirstName() + " " + user.getLastName());

        model.setViewName("home/home");  //resources/template/home.html
        return model;
    }










/*    @RequestMapping(value= {"/access_denied"}, method=RequestMethod.GET)
    public ModelAndView accessDenied() {
        ModelAndView model = new ModelAndView();
        model.setViewName("errors/access_denied");
        return model;
    }*/
}