package com.summitworks.ngo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.summitworks.ngo.authentication.AmazonClient;
import com.summitworks.ngo.entities.Event;
import com.summitworks.ngo.entities.User;
import com.summitworks.ngo.repo.EventRepoistory;

@Controller
public class EventController {
	@Autowired
	EventRepoistory eventRepo;
	@Autowired
	AmazonClient amazonClient;

	@GetMapping("/EventManagement")
	public String homePage(Model model) {
		List<Event> listEvents = eventRepo.findAll();
		// model.addAttribute("appName", appName);
		model.addAttribute("listEvents", listEvents);
		return "eventHome";
	}

	@RequestMapping("/addEvent")
	public String showAddEventPage(Model model) {
		System.out.println("add event method");

		Event event = new Event();
		model.addAttribute("event", event);

		return "addEvent";
	}

	@RequestMapping("/saveEvent")
	public String saveEvent(@ModelAttribute("event") Event event, @RequestPart(value = "file") MultipartFile file) {
		System.out.println("file " + file);
		System.out.println("Sve of controller");
		System.out.println("id after edit" + event.getId());
		if (event.getId() == null || file!=null ) {
			String fileLocation = this.amazonClient.uploadFile(file);
			event.setImageUrl(fileLocation);
		}
		eventRepo.save(event);

		return "redirect:/EventManagement";
	}

	@RequestMapping("/editEvent/{id}")
	public ModelAndView showEditEventPage(@PathVariable(name = "id") Long id) {
		ModelAndView mav = new ModelAndView("editEvent");
		Event event = eventRepo.getOne(id);
		mav.addObject("event", event);

		return mav;
	}
	@RequestMapping(value = "/editSaveEvent/{id}", method = RequestMethod.POST)
	public String saveEditEvent(Event event){
		System.out.println("id after edit"+event.getId());
	    eventRepo.save(event);
	    return "redirect:/EventManagement" ;
	}
	@RequestMapping("/deleteEvent/{id}")
	public String deleteProduct(@PathVariable(name = "id") Long id) {
		eventRepo.deleteById(id);
		return "redirect:/EventManagement";
	}

	@GetMapping("/")
	public String userViewHomePage(Model model) {
		List<Event> listEvents = eventRepo.findAll();
		// model.addAttribute("appName", appName);
		model.addAttribute("listEvents", listEvents);
		return "userViewAllEvents";
	}

	@RequestMapping("/EventDetail/{id}")
	public ModelAndView eventDetail(@PathVariable(name = "id") Long id) {
		ModelAndView mav = new ModelAndView("showEventDetail");
		Event event = eventRepo.getOne(id);
		System.out.println(event.isAllowRegistration());
		mav.addObject("event", event);

		return mav;
	}
}