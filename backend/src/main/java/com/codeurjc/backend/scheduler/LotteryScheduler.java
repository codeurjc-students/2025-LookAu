package com.codeurjc.backend.scheduler;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

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
import com.codeurjc.backend.service.LotteryService;
import com.codeurjc.backend.service.ScraperService;
import com.codeurjc.backend.service.TicketService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;


@Component
@Profile("!test")
public class LotteryScheduler  {

	@Autowired
    private TicketService ticketService;
	@Autowired
	private LotteryService lotteryService;
	@Autowired
	private ScraperService scraperService;

	private boolean running = false;


	/*************************/
	/*************************/
	/******* SCHEDULED *******/
	/*************************/
	/*************************/

	@Scheduled(fixedDelay = 60000)
	public synchronized void run() throws JsonMappingException, JsonProcessingException, InterruptedException{

		if (running) {
			System.out.println("Scheduler ya en ejecución. Saltando...");
			return;
		}
		running = true;

		try{

			//gets all pending tickets
			List<Ticket> lAllPendingTickets = ticketService.getAllPendingTicket();
			
			if(!lAllPendingTickets.isEmpty()){

				//agroup pending tickets by type
				Map<String, Map<String, Ticket>> mTypeToDateToTicketPending = getMapsPendingTickets(lAllPendingTickets);

				//by each type get all the dates for each ticket
				Map<String, List<String>> mTypeToDate = new HashMap<String, List<String>>();
				for (Map.Entry<String, Map<String, Ticket>> entry : mTypeToDateToTicketPending.entrySet()) {
					
					String key = entry.getKey(); 									//get principal type key
					Map<String, Ticket> mDateTickets = entry.getValue(); 			//get the value of the key (map string,ticket)

					List<String> lDates = new ArrayList<>(mDateTickets.keySet());		//get tickets dates

					mTypeToDate.put(key, lDates.stream().sorted(Comparator.reverseOrder()).collect(Collectors.toList()));
				}

				checkAndChangeStatusByType(lAllPendingTickets, mTypeToDate, mTypeToDateToTicketPending);
				
			}

		}catch(Exception e){
			System.err.println("Error in scheduled job: " + e.getMessage());
        	e.printStackTrace();
		} finally {
			running = false;
		}
	}


	/*************************/
	/*************************/
	/********* METHOS ********/
	/*************************/
	/*************************/

	/***************************************/
	/******* CHECK AND CHANGE STATUS *******/
	/***************************************/
	/* @throws Exception */

	//depends the ticket types, change the statusprice and the statusname
	@SuppressWarnings("unchecked")
	private void checkAndChangeStatusByType(List<Ticket> lTickets, Map<String, List<String>> mTypeToDate, Map<String, Map<String, Ticket>> mTypeToDateToTicketPending) throws Exception{
		
		List<Ticket> lTicketsToUpdateByType = new ArrayList<Ticket>();
		List<Ticket> lTicketsToUpdateAll = new ArrayList<Ticket>();

		//get the ticket results and with the map ticket search easier for to check the status
		for(String key: mTypeToDate.keySet()){
			
			//get the result tickets by the type
			List<?> lTicketAPI =  scraperService.getResults(key, mTypeToDate.get(key).get(0), mTypeToDate.get(key).get(mTypeToDate.get(key).size() - 1));
			
			//check the status ticket by type
			if(!lTicketAPI.isEmpty()){
				switch (key){
					case "BONO": {
						lTicketsToUpdateByType = lotteryService.checkAndChangeBonolotoStatus((List<BonolotoAPI>) lTicketAPI, mTypeToDateToTicketPending.get(key));
						break;
					}
					case "EDMS": {
						lTicketsToUpdateByType = lotteryService.checkAndChangeEurodreamsStatus((List<EurodreamsAPI>) lTicketAPI, mTypeToDateToTicketPending.get(key));
						break;
					}
					case "EMIL": {
						lTicketsToUpdateByType = lotteryService.checkAndChangeEuromillonesStatus((List<EuromillonesAPI>) lTicketAPI, mTypeToDateToTicketPending.get(key));
						break;
					}
					case "ELGR": {
						lTicketsToUpdateByType = lotteryService.checkAndChangeGordoStatus((List<GordoAPI>) lTicketAPI, mTypeToDateToTicketPending.get(key));
						break;
					}
					case "LNAC": {
						lTicketsToUpdateByType = lotteryService.checkAndChangeLoteriaStatus((List<LoteriaAPI>) lTicketAPI, mTypeToDateToTicketPending.get(key));
						break;
					}
					case "LOTU": {
						lTicketsToUpdateByType = lotteryService.checkAndChangeLototurfStatus((List<LototurfAPI>) lTicketAPI, mTypeToDateToTicketPending.get(key));
						break;
					}
					case "LAPR": {
						lTicketsToUpdateByType = lotteryService.checkAndChangePrimitivaStatus((List<PrimitivaAPI>) lTicketAPI, mTypeToDateToTicketPending.get(key));
						break;
					}
					case "LAQU": {
						lTicketsToUpdateByType = lotteryService.checkAndChangeQuinielaStatus((List<QuinielaAPI>) lTicketAPI, mTypeToDateToTicketPending.get(key));
						break;
					}
					case "QGOL": {
						lTicketsToUpdateByType = lotteryService.checkAndChangeQuinigolStatus((List<QuinigolAPI>) lTicketAPI, mTypeToDateToTicketPending.get(key));
						break;
					}
					default: {
						lTicketsToUpdateByType = lotteryService.checkAndChangeQuintupleStatus((List<QuintupleAPI>) lTicketAPI, mTypeToDateToTicketPending.get(key));
						break;
					}
				}

				lTicketsToUpdateAll.addAll(lTicketsToUpdateByType);
			}
		}

		ticketService.setTickets(lTicketsToUpdateAll);
	}


	/*******************************/
	/******* ADJUST THE DATA *******/
	/*******************************/

	//make a map for easier searching tickets by date
	public Map<String, Map<String, Ticket>> getMapsPendingTickets(List<Ticket> lTickets) throws JsonMappingException, JsonProcessingException, InterruptedException{
		
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
}