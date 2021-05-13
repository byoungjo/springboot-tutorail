package multi.database.controller;

import lombok.extern.slf4j.Slf4j;
import multi.database.model.Employ;
import multi.database.service.WithMapperService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.servlet.ModelAndView;

import multi.database.service.WithDaoService;

@Slf4j
//@Controller
@RestController
public class MultiDatabaseController {
	private static final Logger logger = LoggerFactory.getLogger(MultiDatabaseController.class);
	
	@Autowired
	private WithDaoService withDaoService;

	@GetMapping("/")
	public String init(){
		log.info("출력");
		return "index";
	}

	//@RequestMapping(value="/withdao", method=RequestMethod.GET)
	@GetMapping("/withdao")
	public Employ home() {
		log.info(">>>>>> withdao");
		Employ employ =  withDaoService.moveDb1toDb2(1);
		return employ;
	}

	@Autowired
	private WithMapperService withMapperService;
	
	@RequestMapping(value="/withmapper", method=RequestMethod.GET)
	public Employ account() {
		Employ employ = withMapperService.moveDb1toDb2(1);
		return employ;
	}
}