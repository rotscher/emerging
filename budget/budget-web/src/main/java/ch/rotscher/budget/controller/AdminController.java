package ch.rotscher.budget.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import ch.rotscher.budget.service.AccountTemplateService;
import ch.rotscher.budget.service.StatementService;

@Controller
public class AdminController {

	@Autowired AccountTemplateService accountTemplateService;
	@Autowired StatementService statementService;
	
    @RequestMapping(value = "/admin/accounts/create", method=RequestMethod.GET)
    public ModelAndView getAccounts() {
        try {
        	accountTemplateService.createAccounts();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return new ModelAndView(new RedirectView("/index.html", true));
    }
    
}