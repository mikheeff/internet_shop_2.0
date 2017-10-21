package com.internetshop.controller;

import com.internetshop.Exceptions.EmailExistException;
import com.internetshop.Exceptions.PasswordWrongException;
import com.internetshop.model.Client;
import com.internetshop.model.PasswordField;
import com.internetshop.service.api.ClientService;
import com.internetshop.service.api.GoodsService;
import com.internetshop.service.api.OrderService;
import com.internetshop.service.impl.ClientServiceImpl;
import com.internetshop.service.impl.UserDetailsServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
@RequestMapping("/clients")
public class ClientController {

    Logger logger = LoggerFactory.getLogger("com.shop");

    @Autowired
    private ClientService clientService;

    @Autowired
    private GoodsService goodsService;
    @Autowired
    private OrderService orderService;

    @Autowired
    private HttpSession session;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String getAllUsers(ModelMap modelMap) {
        modelMap.put("listClient", clientService.getAllClients());
        return "client_page";
    }
    @RequestMapping(value ="/identification",method = RequestMethod.GET)
    public String identifyUser(ModelMap modelMap,
                               @RequestParam(value = "error", required = false) String error,
                               @RequestParam(value = "logout", required = false) String logout,
                               @RequestParam(value = "regError", required = false) String regError) {
        modelMap.put("newClient", new Client());
        if (error != null) {
            modelMap.put("error", "Invalid email or password!");
        }
        if (regError != null) {
            modelMap.put("regError", "Invalid params!");
//            modelMap.put(session.getAttribute()) //todo хорошо бы сделать, чтобы при невалидных параметрах ввёденные данные не терялись
        }

        if (logout != null) {
            modelMap.put("msg", "You've been logged out successfully.");
        }
        return "register";
    }


    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public String editClient(ModelMap modelMap, HttpServletRequest httpServletRequest,
                             @RequestParam(value = "errorMatch", required = false) String errorMatch,
                             @RequestParam(value = "msg", required = false) String msg,
                             @RequestParam(value = "error", required = false) String error,
                             @RequestParam(value = "errorInvalidPass", required = false) String errorInvalidPass) {
        session.setAttribute("client",clientService.getUserByEmail( httpServletRequest.getUserPrincipal().getName()));
        modelMap.put("client",session.getAttribute("client"));
        if(modelMap.get("client") == null) {
            return "redirect:/clients/identification";
        }
        modelMap.put("listCategory",goodsService.getAllCategories());
        Client client = (Client)session.getAttribute("client");
        if(client.getRole().getName().equals("ROLE_CLIENT")) {
            modelMap.put("clientOrdersList", orderService.getAllOrdersByClientId(client.getId()));
        } else {
            modelMap.put("clientOrdersList", orderService.getAllOrders());
        }

        if (errorMatch != null) {
            modelMap.put("errorMatch", "Entered New passwords doesn't match!");
        }
        if (error != null) {
            modelMap.put("error", "Entered characters are not allowed!");
        }
        if (errorInvalidPass != null) {
            modelMap.put("errorInvalidPass", "Password is not valid!");
        }
        if (msg != null) {
            modelMap.put("msg", "You have been changed password successfully!");
        }
        modelMap.put("passwordField", new PasswordField());
        return "profile";
    }

    @RequestMapping(value = "/success", method = RequestMethod.POST)
    public String addClient(@ModelAttribute (value = "client")@Valid Client client, BindingResult bindingResult,ModelMap modelMap) {
        if(bindingResult.hasErrors()) {
            return "redirect:/clients/identification?regError";
        }
        try {
            this.clientService.addClient(client);
        } catch (EmailExistException e) {
            logger.error("Error", e);
            modelMap.put("newClient",client);
            modelMap.put("regError","Email already exist!");
            return "register";
        }
        return "registr_success";
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public String editClient(@ModelAttribute (value = "client") @Valid Client client, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "redirect:/clients/profile";
        }
        this.clientService.updateUser(client);
        session.setAttribute("client",clientService.getUserByEmail(client.getEmail()));
        return "redirect:/clients/profile";
    }

    @RequestMapping(value = "/profile/edit/password", method = RequestMethod.POST)
    public String changePassword(@ModelAttribute (value = "passwordField") @Valid PasswordField passwordField,BindingResult bindingResult){
        if(bindingResult.hasErrors()) {
            return "redirect:/clients/profile?error";
        }
        Client client = (Client)session.getAttribute("client");
        if (passwordField.getNewPasswordFirst().equals(passwordField.getNewPasswordSecond())) {
            try {
                clientService.changePassword(passwordField, client);
            } catch (PasswordWrongException e) {
                return "redirect:/clients/profile?errorInvalidPass";
            }
        }
        else {
            return "redirect:/clients/profile?errorMatch";
        }
        session.setAttribute("client",clientService.getClientById(client.getId()));
        return "redirect:/clients/profile?msg";

    }

}
