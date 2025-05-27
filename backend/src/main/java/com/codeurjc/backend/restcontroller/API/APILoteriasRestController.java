package com.codeurjc.backend.restcontroller.API;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codeurjc.backend.model.Account;
import com.codeurjc.backend.model.Team;
import com.codeurjc.backend.model.Ticket;
import com.codeurjc.backend.model.API.BonolotoAPI;
import com.codeurjc.backend.model.API.EurodreamsAPI;
import com.codeurjc.backend.model.API.EuromillonesAPI;
import com.codeurjc.backend.model.API.GordoAPI;
import com.codeurjc.backend.model.API.LoteriaAPI;
import com.codeurjc.backend.model.API.LototurfAPI;
import com.codeurjc.backend.model.API.PrimitivaAPI;
import com.codeurjc.backend.model.API.QuinielaAPI;
import com.codeurjc.backend.model.API.QuinigolAPI;
import com.codeurjc.backend.model.API.QuintupleAPI;
import com.codeurjc.backend.service.AccountService;
import com.codeurjc.backend.service.TeamService;
import com.codeurjc.backend.service.TicketService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.bonigarcia.wdm.WebDriverManager;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/api/loteria")
public class APILoteriasRestController {

	@Autowired
    private TicketService ticketService;
	@Autowired
	private AccountService accountService;
	@Autowired
	private TeamService teamService;


    APILoteriasRestController(TicketService ticketService) {
        this.ticketService = ticketService;
    }


	/**
	* @throws InterruptedException 
	* @throws JsonProcessingException 
	* @throws JsonMappingException 
	*/


	/*******************************************/
	/******* API CALLS (teams - profile) *******/
	/*******************************************/

	@Operation(summary = "Reload pending tickets from a team")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Account information or not registered", 
			content = @Content(mediaType = "application/json")),
		@ApiResponse(responseCode = "403", description = "Forbidden or don't have permissions", 
			content = @Content)
	})
	@GetMapping("/teams/{teamId}")
	public ResponseEntity<Map<String, Object>> teams(HttpServletRequest request, @PathVariable Long teamId) throws JsonMappingException, JsonProcessingException, InterruptedException {
		
		Optional<Account> currentAccount = accountService.getByEmail(request.getUserPrincipal().getName());
		
		if (currentAccount.isPresent()) {

			Optional<Team> teamOpp = teamService.getById(teamId);

			if(teamOpp.isPresent()){

				checkAndChangeAllStatus(teamOpp.get().getTickets());
							
				return new ResponseEntity<>(null, HttpStatus.OK);
			} else {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}

		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}


	@Operation(summary = "Reload pending tickets from a team")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Account information or not registered", 
			content = @Content(mediaType = "application/json")),
		@ApiResponse(responseCode = "403", description = "Forbidden or don't have permissions", 
			content = @Content)
	})
	@GetMapping("/personal")
	public ResponseEntity<Map<String, Object>> profile(HttpServletRequest request) throws JsonMappingException, JsonProcessingException, InterruptedException {
		
		Optional<Account> currentAccount = accountService.getByEmail(request.getUserPrincipal().getName());
		
		if (currentAccount.isPresent()) {

			checkAndChangeAllStatus(currentAccount.get().getTickets());
						
			return new ResponseEntity<>(null, HttpStatus.OK);
			

		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}






	/***************************************/
	/******* CHECK AND CHANGE STATUS *******/
	/***************************************/

	//check if the ticket are winning or not winning and change the status if it needs
	private void checkAndChangeAllStatus(List<Ticket> lTickets) throws JsonMappingException, JsonProcessingException, InterruptedException{

		Map<String, Map<String, Ticket>> mTypeToDateToTicketPending = getMapsPendingTickets(lTickets);

		Map<String, List<String>> mDatesToType = new HashMap<String, List<String>>();
		for (Map.Entry<String, Map<String, Ticket>> entry : mTypeToDateToTicketPending.entrySet()) {
			
			String key = entry.getKey(); 									//get principal type key
			Map<String, Ticket> mDateTickets = entry.getValue(); 			//get the value of the key (map string,ticket)

			List<String> lDates = new ArrayList<>(mDateTickets.keySet());		//get tickets dates

			mDatesToType.put(key, lDates.stream().sorted(Comparator.reverseOrder()).collect(Collectors.toList()));
		}

		checkAndChangeStatusByType(lTickets, mDatesToType, mTypeToDateToTicketPending);
	}

	//depends the ticket types, change the statusprice and the statusname
	private void checkAndChangeStatusByType(List<Ticket> lTickets, Map<String, List<String>> mDatesByType, Map<String, Map<String, Ticket>> mTypeToDateToTicketPending) throws JsonMappingException, JsonProcessingException, InterruptedException{
		
		List<Ticket> lTicketsToUpdateByType = new ArrayList<Ticket>();
		List<Ticket> lTicketsToUpdateAll = new ArrayList<Ticket>();
		Helper helper = new Helper();

		//get the ticket results and with the map ticket search easier for to check the status
		for(String key: mDatesByType.keySet()){
			
			//get the result tickets by the type
			List<?> lTicketAPI =  getBetListString(key, mDatesByType.get(key).get(0), mDatesByType.get(key).get(mDatesByType.get(key).size() - 1));
			
			//check the status ticket by type
			switch (key){
				case "BONO": {
					lTicketsToUpdateByType = helper.checkAndChangeBonolotoStatus((List<BonolotoAPI>) lTicketAPI, mTypeToDateToTicketPending.get(key));
					break;
				}
				case "EDMS": {
					lTicketsToUpdateByType = helper.checkAndChangeEurodreamsStatus((List<EurodreamsAPI>) lTicketAPI, mTypeToDateToTicketPending.get(key));
					break;
				}
				case "EMIL": {
					lTicketsToUpdateByType = helper.checkAndChangeEuromillonesStatus((List<EuromillonesAPI>) lTicketAPI, mTypeToDateToTicketPending.get(key));
					break;
				}
				case "ELGR": {
					lTicketsToUpdateByType = helper.checkAndChangeGordoStatus((List<GordoAPI>) lTicketAPI, mTypeToDateToTicketPending.get(key));
					break;
				}
				case "LNAC": {
					lTicketsToUpdateByType = helper.checkAndChangeLoteriaStatus((List<LoteriaAPI>) lTicketAPI, mTypeToDateToTicketPending.get(key));
					break;
				}
				case "LOTU": {
					lTicketsToUpdateByType = helper.checkAndChangeLototurfStatus((List<LototurfAPI>) lTicketAPI, mTypeToDateToTicketPending.get(key));
					break;
				}
				case "LAPR": {
					lTicketsToUpdateByType = helper.checkAndChangePrimitivaStatus((List<PrimitivaAPI>) lTicketAPI, mTypeToDateToTicketPending.get(key));
					break;
				}
				case "LAQU": {
					lTicketsToUpdateByType = helper.checkAndChangeQuinielaStatus((List<QuinielaAPI>) lTicketAPI, mTypeToDateToTicketPending.get(key));
					break;
				}
				case "QGOL": {
					lTicketsToUpdateByType = helper.checkAndChangeQuinigolStatus((List<QuinigolAPI>) lTicketAPI, mTypeToDateToTicketPending.get(key));
					break;
				}
				default: {
					lTicketsToUpdateByType = helper.checkAndChangeQuintupleStatus((List<QuintupleAPI>) lTicketAPI, mTypeToDateToTicketPending.get(key));
					break;
				}
			}
			
			lTicketsToUpdateAll.addAll(lTicketsToUpdateByType);
			
			
		}

		ticketService.setTickets(lTicketsToUpdateAll);
	}


	/*******************************/
	/******* ADJUST THE DATA *******/
	/*******************************/

	//make a map for easier searching tickets by date
	private Map<String, Map<String, Ticket>> getMapsPendingTickets(List<Ticket> lTickets) throws JsonMappingException, JsonProcessingException, InterruptedException{
		
		Map<String, Map<String, Ticket>> mTypeToTicketPending = new HashMap<String, Map<String, Ticket>>();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
		String formatDate = "";

		//build the map with the pending tickets depends the tickettype
		for(Ticket ticket: lTickets){

			if(ticket.getStatusName().equals("Pending")){
				
				formatDate = ticket.getDate().format(formatter);

				switch (ticket.getTicketType().getStringTickectType()) {

					case "Bonoloto":
						mTypeToTicketPending.computeIfAbsent("BONO", k -> new HashMap<>()).put(formatDate, ticket);
					break;

					case "Eurodreams":
						mTypeToTicketPending.computeIfAbsent("EDMS", k -> new HashMap<>()).put(formatDate, ticket);
					break;

					case "Euromillones":
						mTypeToTicketPending.computeIfAbsent("EMIL", k -> new HashMap<>()).put(formatDate, ticket);
					break;

					case "El Gordo":
						mTypeToTicketPending.computeIfAbsent("ELGR", k -> new HashMap<>()).put(formatDate, ticket);
					break;

					case "Lotería Nacional":
						mTypeToTicketPending.computeIfAbsent("LNAC", k -> new HashMap<>()).put(formatDate, ticket);
					break;

					case "Lototurf":
						mTypeToTicketPending.computeIfAbsent("LOTU", k -> new HashMap<>()).put(formatDate, ticket);
					break;

					case "La Primitiva":
						mTypeToTicketPending.computeIfAbsent("LAPR", k -> new HashMap<>()).put(formatDate, ticket);
					break;

					case "La Quiniela":
						mTypeToTicketPending.computeIfAbsent("LAQU", k -> new HashMap<>()).put(formatDate, ticket);
					break;

					case "El Quinigol":
						mTypeToTicketPending.computeIfAbsent("QGOL", k -> new HashMap<>()).put(formatDate, ticket);
					break;

					case "Quíntuple plus":
						mTypeToTicketPending.computeIfAbsent("QUPL", k -> new HashMap<>()).put(formatDate, ticket);
					break;
				}

			}
		}

		return mTypeToTicketPending;
	}




	/*******************************/
	/******* SEARCH DATA WIN *******/
	/*******************************/

	//get de json from Loterias y Apuestas del Estado (SELENIUM) and get it with the correct object
	private List<?> getBetListString(String type, String firstDate, String lastDate) throws InterruptedException, JsonMappingException, JsonProcessingException{
		
		//create WebDriverManager to make it works (idk why) 
        WebDriverManager.chromedriver().setup();

        //create options to make it works (without the second line, it doesnt work xd, but idk why) 
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");

        //start the navigation session
        WebDriver driver = new ChromeDriver(options);

        //go to the web - ID BOLETOS: BONO, EDMS, EMIL, ELGR, LNAC, LOTU, LAPR, LAQU, QGOL, QUPL, TODOS
        driver.get("https://www.loteriasyapuestas.es/servicios/buscadorSorteos?game_id="+type+"&celebrados=false&fechaInicioInclusiva="+lastDate+"&fechaFinInclusiva="+firstDate);

        //wait for it needs to load
        //Thread.sleep(5000);

        //get html and close the connection
        String html = driver.getPageSource();
        driver.quit();

        //get the JSON from the hatml in the tag <pre>
        Document doc = Jsoup.parse(html);
        Element pre = doc.selectFirst("pre");
		String json = pre.text();

		if(!json.startsWith("[")){
			type = "";
		}

		//convert the string json to a object by type
		ObjectMapper objectMapper = new ObjectMapper();
		switch (type) {
			case "BONO":
				return objectMapper.readValue(json, new TypeReference<List<BonolotoAPI>>() {});
			case "EDMS":
				return objectMapper.readValue(json, new TypeReference<List<EurodreamsAPI>>() {});
			case "EMIL":
				return objectMapper.readValue(json, new TypeReference<List<EuromillonesAPI>>() {});
			case "ELGR":
				return objectMapper.readValue(json, new TypeReference<List<GordoAPI>>() {});
			case "LNAC":
				return objectMapper.readValue(json, new TypeReference<List<LoteriaAPI>>() {});
			case "LOTU":
				return objectMapper.readValue(json, new TypeReference<List<LototurfAPI>>() {});
			case "LAPR":
				return objectMapper.readValue(json, new TypeReference<List<PrimitivaAPI>>() {});
			case "LAQU":
				return objectMapper.readValue(json, new TypeReference<List<QuinielaAPI>>() {});
			case "QGOL":
				return objectMapper.readValue(json, new TypeReference<List<QuinigolAPI>>() {});
			case "QUPL":
				return objectMapper.readValue(json, new TypeReference<List<QuintupleAPI>>() {});
			default:
				return null;
		}
	}
}