package multi.database.controller;

import lombok.extern.slf4j.Slf4j;
import multi.database.model.Employ;
import multi.database.model.Message;
import multi.database.service.WithMapperService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
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

	@GetMapping("/success")
	public ResponseEntity<String> successMessage(){
		return new ResponseEntity<>("success", HttpStatus.OK);
	}

	@GetMapping(value = "/serverError", produces = "application/json")
	public ResponseEntity<Message> serverErrorMessage() {
		Message message = Message.builder()
				.message1("첫번째 메세지")
				.message2("두번째 메세지")
				.build();
		return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@GetMapping("/header")
	public ResponseEntity<String> header(){
		MultiValueMap<String, String> header = new LinkedMultiValueMap<>();
		header.add("AUTHCODE","XXXXXX");
		header.add("TOKEN","XXXX");

		return new ResponseEntity<>(header, HttpStatus.OK);
	}

	@GetMapping("/onlystatus")
	public ResponseEntity onlyStatus(){
		return new ResponseEntity(HttpStatus.OK);
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