package com.codeurjc.backend.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.codeurjc.backend.model.Ticket;
import com.codeurjc.backend.model.API.BonolotoAPI;
import com.codeurjc.backend.model.API.EurodreamsAPI;
import com.codeurjc.backend.model.API.EuromillonesAPI;
import com.codeurjc.backend.model.API.GordoAPI;
import com.codeurjc.backend.model.API.LoteriaAPI;
import com.codeurjc.backend.model.API.LoteriaAPI.Premio;
import com.codeurjc.backend.model.API.LototurfAPI;
import com.codeurjc.backend.model.API.PrimitivaAPI;
import com.codeurjc.backend.model.API.QuinielaAPI;
import com.codeurjc.backend.model.API.QuinigolAPI;
import com.codeurjc.backend.model.API.QuintupleAPI;
import com.codeurjc.backend.model.types.Bonoloto;
import com.codeurjc.backend.model.types.Eurodreams;
import com.codeurjc.backend.model.types.Euromillones;
import com.codeurjc.backend.model.types.Gordo;
import com.codeurjc.backend.model.types.Loteria;
import com.codeurjc.backend.model.types.Lototurf;
import com.codeurjc.backend.model.types.Primitiva;
import com.codeurjc.backend.model.types.Quiniela;
import com.codeurjc.backend.model.types.Quinigol;
import com.codeurjc.backend.model.types.Quintuple;


@Service
public class LotteryService {

    public LotteryService(){}

    //bonoloto
	public List<Ticket> checkAndChangeBonolotoStatus(List<BonolotoAPI> lBetTypeResults, Map<String, Ticket> mDateToTicket){

		List<Ticket> lTicketsToUpdate = new ArrayList<Ticket>();
		Map<String, BonolotoAPI> mDateToBonolotoapi = new HashMap<String, BonolotoAPI>();
		String formatDate = "";
        String lastNewDate = "";

		//get the tickets result by date
        if (lBetTypeResults != null){
            for(BonolotoAPI bonolotoAPI: lBetTypeResults){
                formatDate = bonolotoAPI.getFecha_sorteo().substring(0, 10).replace("-", "");
                mDateToBonolotoapi.put(formatDate, bonolotoAPI);
            }

            List<String> datesAPI = new ArrayList<String>(mDateToBonolotoapi.keySet());
            lastNewDate = datesAPI.get(0);
        }


		Bonoloto bonoloto;
		Ticket ticket;
		Boolean save;
		//check sthe status or if the date are wrong
		for(String key: mDateToTicket.keySet()){

			ticket = mDateToTicket.get(key);
			bonoloto = (Bonoloto) mDateToTicket.get(key).getTicketType();
			save = false;

			//if the ticket date exists in the map results
			if(mDateToBonolotoapi.containsKey(key) && mDateToBonolotoapi.get(key).getCombinacion()!=null){

				save = true;
				
				BonolotoAPI bonolotoAPI = mDateToBonolotoapi.get(key);

				//get the winner numbers
				List<Integer> winNumbers = new ArrayList<>();	//0-5: numbers	6:C  7:R
				Matcher matcher = Pattern.compile("\\d+").matcher(bonolotoAPI.getCombinacion());
				while (matcher.find()) {
					winNumbers.add(Integer.parseInt(matcher.group()));
				}


				/** CHECK THE RESULTS **/
				//get a list the same numbers for coincidence
				List<Integer> sameNumbers = new ArrayList<>(winNumbers.subList(0, 6));
				sameNumbers.retainAll(bonoloto.getNumList());

				//category 1
				if(sameNumbers.size()==6){
					ticket.setStatusName("Winning");
					ticket.setStatusPrice(Double.valueOf(bonolotoAPI.getEscrutinio().get(0).getPremio().replace(",", ".")));

				//category 2
				}else if(sameNumbers.size()==5 && bonoloto.getNumList().contains(winNumbers.get(6))){
					ticket.setStatusName("Winning");
					ticket.setStatusPrice(Double.valueOf(bonolotoAPI.getEscrutinio().get(1).getPremio().replace(",", ".")));
				
				//category 3
				}else if(sameNumbers.size()==5){
					ticket.setStatusName("Winning");
					ticket.setStatusPrice(Double.valueOf(bonolotoAPI.getEscrutinio().get(2).getPremio().replace(",", ".")));
				
				//category 4
				}else if(sameNumbers.size()==4){
					ticket.setStatusName("Winning");
					ticket.setStatusPrice(Double.valueOf(bonolotoAPI.getEscrutinio().get(3).getPremio().replace(",", ".")));

				//category 5
				}else if(sameNumbers.size()==3){
					ticket.setStatusName("Winning");
					ticket.setStatusPrice(Double.valueOf(bonolotoAPI.getEscrutinio().get(4).getPremio().replace(",", ".")));

				//category 6
				}else if(bonoloto.getNumList().contains(winNumbers.get(7))){
					ticket.setStatusName("Winning");
					ticket.setStatusPrice(Double.valueOf(bonolotoAPI.getEscrutinio().get(5).getPremio().replace(",", ".")));
				}else{
					ticket.setStatusName("Not Winning");
					ticket.setStatusPrice(ticket.getPaidByPice());
				}

			//if the ticket date doesnt exists in the map results
			}else{

				//the ticket date doesnt belong to a result; else statement = the result isnt ready yet
				if(Integer.valueOf(lastNewDate)<Integer.valueOf(key)){
					save = true;
					ticket.setStatusName("Wrong Date");
				}	

				ticket.setStatusPrice(Double.valueOf(0.0));
			}

			if(save) lTicketsToUpdate.add(ticket);
			
		}

		return lTicketsToUpdate;
	}

	//eurodreams
	public List<Ticket> checkAndChangeEurodreamsStatus(List<EurodreamsAPI> lBetTypeResults, Map<String, Ticket> mDateToTicket){

		List<Ticket> lTicketsToUpdate = new ArrayList<Ticket>();
		Map<String, EurodreamsAPI> mDateToEurodreamsapi = new HashMap<String, EurodreamsAPI>();
		String formatDate = "";
        String lastNewDate = "";

		//get the tickets result by date
        if (lBetTypeResults != null){
            for(EurodreamsAPI eurodreamsAPI: lBetTypeResults){
                formatDate = eurodreamsAPI.getFecha_sorteo().substring(0, 10).replace("-", "");
                mDateToEurodreamsapi.put(formatDate, eurodreamsAPI);
            }

            List<String> datesAPI = new ArrayList<String>(mDateToEurodreamsapi.keySet());
            lastNewDate = datesAPI.get(0);
        }
		
		Eurodreams eurodreams;
		Ticket ticket;
		Boolean save;
		//check sthe status or if the date are wrong
		for(String key: mDateToTicket.keySet()){

			ticket = mDateToTicket.get(key);
			eurodreams = (Eurodreams) mDateToTicket.get(key).getTicketType();
			save = false;

			//if the ticket date exists in the map results
			if(mDateToEurodreamsapi.containsKey(key) && mDateToEurodreamsapi.get(key).getCombinacion()!=null){

				save = true;
				
				EurodreamsAPI eurodreamsAPI = mDateToEurodreamsapi.get(key);

				//get the winner numbers
				List<Integer> winNumbers = new ArrayList<>();	//0-5: numbers	6: dream
				Matcher matcher = Pattern.compile("\\d+").matcher(eurodreamsAPI.getCombinacion());
				while (matcher.find()) {
					winNumbers.add(Integer.parseInt(matcher.group()));
				}


				/** CHECK THE RESULTS **/
				//get a list the same numbers for coincidence
				List<Integer> sameNumbers = new ArrayList<>(winNumbers.subList(0, 6));
				sameNumbers.retainAll(eurodreams.getNumList());

				//category 1
				if(sameNumbers.size()==6 && eurodreams.getNumList().contains(winNumbers.get(6))){
					ticket.setStatusName("Winning");
					ticket.setStatusPrice(Double.valueOf(eurodreamsAPI.getEscrutinio().get(0).getPremio().replace(",", ".")));

				//category 2
				}else if(sameNumbers.size()==6){
					ticket.setStatusName("Winning");
					ticket.setStatusPrice(Double.valueOf(eurodreamsAPI.getEscrutinio().get(1).getPremio().replace(",", ".")));
				
				//category 3
				}else if(sameNumbers.size()==5){
					ticket.setStatusName("Winning");
					ticket.setStatusPrice(Double.valueOf(eurodreamsAPI.getEscrutinio().get(2).getPremio().replace(",", ".")));
				
				//category 4
				}else if(sameNumbers.size()==4){
					ticket.setStatusName("Winning");
					ticket.setStatusPrice(Double.valueOf(eurodreamsAPI.getEscrutinio().get(3).getPremio().replace(",", ".")));

				//category 5
				}else if(sameNumbers.size()==3){
					ticket.setStatusName("Winning");
					ticket.setStatusPrice(Double.valueOf(eurodreamsAPI.getEscrutinio().get(4).getPremio().replace(",", ".")));

				//category 6
				}else if(sameNumbers.size()==2){
					ticket.setStatusName("Winning");
					ticket.setStatusPrice(Double.valueOf(eurodreamsAPI.getEscrutinio().get(5).getPremio().replace(",", ".")));
				}else{
					ticket.setStatusName("Not Winning");
					ticket.setStatusPrice(ticket.getPaidByPice());
				}

			//if the ticket date doesnt exists in the map results
			}else{

				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
				String today = LocalDate.now().format(formatter);

				//the ticket date doesnt belong to a result; else statement = the result isnt ready yet
				if(lastNewDate.equals("") && !(Integer.valueOf(key)>Integer.valueOf(today))){
					save = true;
					ticket.setStatusName("Wrong Date");
				}	

				ticket.setStatusPrice(Double.valueOf(0.0));
			}

			if(save) lTicketsToUpdate.add(ticket);
			
		}

		return lTicketsToUpdate;
	}



    //euromillones
	public List<Ticket> checkAndChangeEuromillonesStatus(List<EuromillonesAPI> lBetTypeResults, Map<String, Ticket> mDateToTicket){

		List<Ticket> lTicketsToUpdate = new ArrayList<Ticket>();
		Map<String, EuromillonesAPI> mDateToEuromillonesapi = new HashMap<String, EuromillonesAPI>();
		String formatDate = "";
        String lastNewDate = "";

		//get the tickets result by date
        if (lBetTypeResults != null){
            for(EuromillonesAPI euromillonesAPI: lBetTypeResults){
                formatDate = euromillonesAPI.getFecha_sorteo().substring(0, 10).replace("-", "");
                mDateToEuromillonesapi.put(formatDate, euromillonesAPI);
            }

            List<String> datesAPI = new ArrayList<String>(mDateToEuromillonesapi.keySet());
            lastNewDate = datesAPI.get(0);
        }
		
		Euromillones euromillones;
		Ticket ticket;
		Boolean save;
		//check sthe status or if the date are wrong
		for(String key: mDateToTicket.keySet()){

			ticket = mDateToTicket.get(key);
			euromillones = (Euromillones) mDateToTicket.get(key).getTicketType();
			save = false;

			//if the ticket date exists in the map results
			if(mDateToEuromillonesapi.containsKey(key) && mDateToEuromillonesapi.get(key).getCombinacion()!=null){

				save = true;
				EuromillonesAPI euromillonesAPI = mDateToEuromillonesapi.get(key);

				//get the winner numbers
				List<Integer> winNumbers = new ArrayList<>();	//0-4: numbers	5-6: dream
				Matcher matcher = Pattern.compile("\\d+").matcher(euromillonesAPI.getCombinacion());
				while (matcher.find()) {
					winNumbers.add(Integer.parseInt(matcher.group()));
				}


				/** CHECK THE RESULTS **/
				//get a list the same numbers for coincidence
				List<Integer> sameNumbers = new ArrayList<>(winNumbers.subList(0, 5));
				sameNumbers.retainAll(euromillones.getNumList());

				//category 1
				if(sameNumbers.size()==5 && euromillones.getStarList().contains(winNumbers.get(5)) && euromillones.getStarList().contains(winNumbers.get(6))){
					ticket.setStatusName("Winning");
					ticket.setStatusPrice(Double.valueOf(euromillonesAPI.getEscrutinio().get(0).getPremio().replace(",", ".")));

				//category 2
				}else if(sameNumbers.size()==5 && (euromillones.getStarList().contains(winNumbers.get(5)) || euromillones.getStarList().contains(winNumbers.get(6)))){
					ticket.setStatusName("Winning");
					ticket.setStatusPrice(Double.valueOf(euromillonesAPI.getEscrutinio().get(1).getPremio().replace(",", ".")));
				
				//category 3
				}else if(sameNumbers.size()==5){
					ticket.setStatusName("Winning");
					ticket.setStatusPrice(Double.valueOf(euromillonesAPI.getEscrutinio().get(2).getPremio().replace(",", ".")));
				
				//category 4
				}else if(sameNumbers.size()==4 && euromillones.getStarList().contains(winNumbers.get(5)) && euromillones.getStarList().contains(winNumbers.get(6))){
					ticket.setStatusName("Winning");
					ticket.setStatusPrice(Double.valueOf(euromillonesAPI.getEscrutinio().get(3).getPremio().replace(",", ".")));

				//category 5
				}else if(sameNumbers.size()==4 && (euromillones.getStarList().contains(winNumbers.get(5)) || euromillones.getStarList().contains(winNumbers.get(6)))){
					ticket.setStatusName("Winning");
					ticket.setStatusPrice(Double.valueOf(euromillonesAPI.getEscrutinio().get(4).getPremio().replace(",", ".")));

				//category 6
				}else if(sameNumbers.size()==3 && euromillones.getStarList().contains(winNumbers.get(5)) && euromillones.getStarList().contains(winNumbers.get(6))){
					ticket.setStatusName("Winning");
					ticket.setStatusPrice(Double.valueOf(euromillonesAPI.getEscrutinio().get(5).getPremio().replace(",", ".")));
				
                //category 7
				}else if(sameNumbers.size()==4){
					ticket.setStatusName("Winning");
					ticket.setStatusPrice(Double.valueOf(euromillonesAPI.getEscrutinio().get(6).getPremio().replace(",", ".")));
				
				//category 8
				}else if(sameNumbers.size()==2 && euromillones.getStarList().contains(winNumbers.get(5)) && euromillones.getStarList().contains(winNumbers.get(6))){
                    ticket.setStatusName("Winning");
					ticket.setStatusPrice(Double.valueOf(euromillonesAPI.getEscrutinio().get(7).getPremio().replace(",", ".")));
				
                //category 9
                }else if(sameNumbers.size()==3 && (euromillones.getStarList().contains(winNumbers.get(5)) || euromillones.getStarList().contains(winNumbers.get(6)))){
                    ticket.setStatusName("Winning");
					ticket.setStatusPrice(Double.valueOf(euromillonesAPI.getEscrutinio().get(8).getPremio().replace(",", ".")));
				
                //category 10
                }else if(sameNumbers.size()==3){
					ticket.setStatusName("Winning");
					ticket.setStatusPrice(Double.valueOf(euromillonesAPI.getEscrutinio().get(9).getPremio().replace(",", ".")));
				
				//category 11
				}else if(sameNumbers.size()==1 && euromillones.getStarList().contains(winNumbers.get(5)) && euromillones.getStarList().contains(winNumbers.get(6))){
                    ticket.setStatusName("Winning");
					ticket.setStatusPrice(Double.valueOf(euromillonesAPI.getEscrutinio().get(10).getPremio().replace(",", ".")));
				
                //category 12
                }else if(sameNumbers.size()==2 && (euromillones.getStarList().contains(winNumbers.get(5)) || euromillones.getStarList().contains(winNumbers.get(6)))){
                    ticket.setStatusName("Winning");
					ticket.setStatusPrice(Double.valueOf(euromillonesAPI.getEscrutinio().get(11).getPremio().replace(",", ".")));
				
                //category 13
                }else if(sameNumbers.size()==2){
                    ticket.setStatusName("Winning");
					ticket.setStatusPrice(Double.valueOf(euromillonesAPI.getEscrutinio().get(12).getPremio().replace(",", ".")));
                
                }else{
					ticket.setStatusName("Not Winning");
					ticket.setStatusPrice(ticket.getPaidByPice());
				}

			//if the ticket date doesnt exists in the map results
			}else{

				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
				String today = LocalDate.now().format(formatter);

				//the ticket date doesnt belong to a result; else statement = the result isnt ready yet
				if(lastNewDate.equals("") && !(Integer.valueOf(key)>Integer.valueOf(today))){
					save = true;
					ticket.setStatusName("Wrong Date");
				}	

				ticket.setStatusPrice(Double.valueOf(0.0));
			}

			if(save) lTicketsToUpdate.add(ticket);
			
		}

		return lTicketsToUpdate;
	}


    //gordo
	public List<Ticket> checkAndChangeGordoStatus(List<GordoAPI> lBetTypeResults, Map<String, Ticket> mDateToTicket){

		List<Ticket> lTicketsToUpdate = new ArrayList<Ticket>();
		Map<String, GordoAPI> mDateToGordoapi = new HashMap<String, GordoAPI>();
		String formatDate = "";
        String lastNewDate = "";

		//get the tickets result by date
        if (lBetTypeResults != null){
            for(GordoAPI gordoAPI: lBetTypeResults){
                formatDate = gordoAPI.getFecha_sorteo().substring(0, 10).replace("-", "");
                mDateToGordoapi.put(formatDate, gordoAPI);
            }

            List<String> datesAPI = new ArrayList<String>(mDateToGordoapi.keySet());
            lastNewDate = datesAPI.get(0);
        }
		
		Gordo gordo;
		Ticket ticket;
		Boolean save;
		//check sthe status or if the date are wrong
		for(String key: mDateToTicket.keySet()){

			ticket = mDateToTicket.get(key);
			gordo = (Gordo) mDateToTicket.get(key).getTicketType();
			save = false;

			//if the ticket date exists in the map results
			if(mDateToGordoapi.containsKey(key) && mDateToGordoapi.get(key).getCombinacion()!=null){

				save = true;
				GordoAPI gordoAPI = mDateToGordoapi.get(key);

				//get the winner numbers
				List<Integer> winNumbers = new ArrayList<>();	//0-4: numbers	5: key
				Matcher matcher = Pattern.compile("\\d+").matcher(gordoAPI.getCombinacion());
				while (matcher.find()) {
					winNumbers.add(Integer.parseInt(matcher.group()));
				}


				/** CHECK THE RESULTS **/
				//get a list the same numbers for coincidence
				List<Integer> sameNumbers = new ArrayList<>(winNumbers.subList(0, 5));
				sameNumbers.retainAll(gordo.getNumList());

				//category 1
				if(sameNumbers.size()==5 && gordo.getKey()==winNumbers.get(5)){
					ticket.setStatusName("Winning");
					ticket.setStatusPrice(Double.valueOf(gordoAPI.getEscrutinio().get(0).getPremio().replace(",", ".")));

				//category 2
				}else if(sameNumbers.size()==5){
					ticket.setStatusName("Winning");
					ticket.setStatusPrice(Double.valueOf(gordoAPI.getEscrutinio().get(1).getPremio().replace(",", ".")));
				
				//category 3
				}else if(sameNumbers.size()==4 && gordo.getKey()==winNumbers.get(5)){
					ticket.setStatusName("Winning");
					ticket.setStatusPrice(Double.valueOf(gordoAPI.getEscrutinio().get(2).getPremio().replace(",", ".")));
				
				//category 4
				}else if(sameNumbers.size()==4){
					ticket.setStatusName("Winning");
					ticket.setStatusPrice(Double.valueOf(gordoAPI.getEscrutinio().get(3).getPremio().replace(",", ".")));

				//category 5
				}else if(sameNumbers.size()==3 && gordo.getKey()==winNumbers.get(5)){
					ticket.setStatusName("Winning");
					ticket.setStatusPrice(Double.valueOf(gordoAPI.getEscrutinio().get(4).getPremio().replace(",", ".")));

				//category 6
				}else if(sameNumbers.size()==3){
					ticket.setStatusName("Winning");
					ticket.setStatusPrice(Double.valueOf(gordoAPI.getEscrutinio().get(5).getPremio().replace(",", ".")));
				
                //category 7
				}else if(sameNumbers.size()==2 && gordo.getKey()==winNumbers.get(5)){
					ticket.setStatusName("Winning");
					ticket.setStatusPrice(Double.valueOf(gordoAPI.getEscrutinio().get(6).getPremio().replace(",", ".")));
				
				//category 8
				}else if(sameNumbers.size()==2){
                    ticket.setStatusName("Winning");
					ticket.setStatusPrice(Double.valueOf(gordoAPI.getEscrutinio().get(7).getPremio().replace(",", ".")));
				
                //category 9
                }else if(gordo.getKey()==winNumbers.get(5)){
                    ticket.setStatusName("Winning");
					ticket.setStatusPrice(Double.valueOf(gordoAPI.getEscrutinio().get(8).getPremio().replace(",", ".")));
				
                }else{
					ticket.setStatusName("Not Winning");
					ticket.setStatusPrice(ticket.getPaidByPice());
				}

			//if the ticket date doesnt exists in the map results
			}else{

				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
				String today = LocalDate.now().format(formatter);

				//the ticket date doesnt belong to a result; else statement = the result isnt ready yet
				if(lastNewDate.equals("") && !(Integer.valueOf(key)>Integer.valueOf(today))){
					save = true;
					ticket.setStatusName("Wrong Date");
				}	

				ticket.setStatusPrice(Double.valueOf(0.0));
			}

			if(save) lTicketsToUpdate.add(ticket);
			
		}

		return lTicketsToUpdate;
	}




    //loteria
	public List<Ticket> checkAndChangeLoteriaStatus(List<LoteriaAPI> lBetTypeResults, Map<String, Ticket> mDateToTicket){

		List<Ticket> lTicketsToUpdate = new ArrayList<Ticket>();
		Map<String, LoteriaAPI> mDateToLoteriaapi = new HashMap<String, LoteriaAPI>();
		String formatDate = "";
        String lastNewDate = "";

		//get the tickets result by date
        if (lBetTypeResults != null){
            for(LoteriaAPI loteriaAPI: lBetTypeResults){
                formatDate = loteriaAPI.getFecha_sorteo().substring(0, 10).replace("-", "");
                mDateToLoteriaapi.put(formatDate, loteriaAPI);
            }

            List<String> datesAPI = new ArrayList<String>(mDateToLoteriaapi.keySet());
            lastNewDate = datesAPI.get(0);
        }
		
		Loteria loteria;
		Ticket ticket;
		Boolean save;
		//check sthe status or if the date are wrong
		for(String key: mDateToTicket.keySet()){

			ticket = mDateToTicket.get(key);
			loteria = (Loteria) mDateToTicket.get(key).getTicketType();
			save = false;

			//if the ticket date exists in the map results
			if(mDateToLoteriaapi.containsKey(key) && mDateToLoteriaapi.get(key).getPrimerPermio().getDecimo()!=null){

				save = true;
				LoteriaAPI loteriaAPI = mDateToLoteriaapi.get(key);

				/** CHECK THE RESULTS **/
                //primer premio
                /*if(loteriaAPI.getPrimerPermio().getDecimo().equals(loteria.getNumber())){
                    ticket.setStatusName("Winning");
					ticket.setStatusPrice(Double.valueOf(loteriaAPI.getPrimerPermio().getPrize()));
                
                //segundo premio
                }else */if(loteriaAPI.getSegundoPremio().getDecimo().equals(loteria.getNumber())){
                    ticket.setStatusName("Winning");
					ticket.setStatusPrice(Double.valueOf(loteriaAPI.getSegundoPremio().getPrize()));
                
                //extracciones
                }else{

                    Boolean isExtraccionCuatroCifras = false;
                    Boolean isExtraccionTresCifras = false;
                    Boolean isExtraccionDosCifras = false;
                    Boolean isReintegro = false;

                    for(Premio premio: loteriaAPI.getExtraccionesDeCuatroCifras()){
                        if(premio.getDecimo() == loteria.getNumber()%10000) isExtraccionCuatroCifras = true;
                    }
                    for(Premio premio: loteriaAPI.getExtraccionesDeTresCifras()){
                        if(premio.getDecimo() == loteria.getNumber()%1000) isExtraccionTresCifras = true;
                    }
                    for(Premio premio: loteriaAPI.getExtraccionesDeDosCifras()){
                        if(premio.getDecimo() == loteria.getNumber()%100) isExtraccionDosCifras = true;
                    }
                    for(Premio premio: loteriaAPI.getReintegros()){
                        if(premio.getDecimo() == loteria.getNumber()%10) isExtraccionDosCifras = true;
                    }

                    //extracciones pcuatro cifras
                    if(isExtraccionCuatroCifras){
                        ticket.setStatusName("Winning");
                        ticket.setStatusPrice(Double.valueOf(loteriaAPI.getExtraccionesDeCuatroCifras().get(0).getPrize()));
                    
                    //extracciones tres cifras
                    }else if(isExtraccionTresCifras){
                        ticket.setStatusName("Winning");
                        ticket.setStatusPrice(Double.valueOf(loteriaAPI.getExtraccionesDeTresCifras().get(0).getPrize()));
                    
                    //extracciones dos cifras
                    }else if(isExtraccionDosCifras){
                        ticket.setStatusName("Winning");
                        ticket.setStatusPrice(Double.valueOf(loteriaAPI.getExtraccionesDeDosCifras().get(0).getPrize()));
                    
                    //extracciones tres cifras
                    }else if(isReintegro){
                        ticket.setStatusName("Winning");
                        ticket.setStatusPrice(Double.valueOf(loteriaAPI.getReintegros().get(0).getPrize()));
                    
                    }else{
                        ticket.setStatusName("Not Winning");
                        ticket.setStatusPrice(ticket.getPaidByPice());
                    }

                }
                

			//if the ticket date doesnt exists in the map results
			}else{

				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
				String today = LocalDate.now().format(formatter);

				//the ticket date doesnt belong to a result; else statement = the result isnt ready yet
				if(lastNewDate.equals("") && !(Integer.valueOf(key)>Integer.valueOf(today))){
					save = true;
					ticket.setStatusName("Wrong Date");
				}	

				ticket.setStatusPrice(Double.valueOf(0.0));
			}

			if(save) lTicketsToUpdate.add(ticket);
			
		}

		return lTicketsToUpdate;
	}


	//lototurf
	public List<Ticket> checkAndChangeLototurfStatus(List<LototurfAPI> lBetTypeResults, Map<String, Ticket> mDateToTicket){

		List<Ticket> lTicketsToUpdate = new ArrayList<Ticket>();
		Map<String, LototurfAPI> mDateToLototurfapi = new HashMap<String, LototurfAPI>();
		String formatDate = "";
        String lastNewDate = "";

		//get the tickets result by date
        if (lBetTypeResults != null){
            for(LototurfAPI lototurfAPI: lBetTypeResults){
                formatDate = lototurfAPI.getFecha_sorteo().substring(0, 10).replace("-", "");
                mDateToLototurfapi.put(formatDate, lototurfAPI);
            }

            List<String> datesAPI = new ArrayList<String>(mDateToLototurfapi.keySet());
            lastNewDate = datesAPI.get(0);
        }
		
		Lototurf lototurf;
		Ticket ticket;
		Boolean save;
		//check sthe status or if the date are wrong
		for(String key: mDateToTicket.keySet()){

			ticket = mDateToTicket.get(key);
			lototurf = (Lototurf) mDateToTicket.get(key).getTicketType();
			save = false;

			//if the ticket date exists in the map results
			if(mDateToLototurfapi.containsKey(key) && mDateToLototurfapi.get(key).getCombinacion()!=null){

				save = true;
				LototurfAPI lototurfAPI = mDateToLototurfapi.get(key);

				//get the winner numbers
				List<Integer> winNumbers = new ArrayList<>();	//0-5: numbers 	6: horse  7: reimbursement
				Matcher matcher = Pattern.compile("\\d+").matcher(lototurfAPI.getCombinacion());
				while (matcher.find()) {
					winNumbers.add(Integer.parseInt(matcher.group()));
				}


				/** CHECK THE RESULTS **/
				//get a list the same numbers for coincidence
				List<Integer> sameNumbers = new ArrayList<>(winNumbers.subList(0, 6));
				sameNumbers.retainAll(lototurf.getNumList());

				//category 1
				if(sameNumbers.size()==6 && lototurf.getHorse()==winNumbers.get(6)){
					ticket.setStatusName("Winning");
					ticket.setStatusPrice(Double.valueOf(lototurfAPI.getEscrutinio().get(0).getPremio().replace(",", ".")));

				//category 2
				}else if(sameNumbers.size()==6){
					ticket.setStatusName("Winning");
					ticket.setStatusPrice(Double.valueOf(lototurfAPI.getEscrutinio().get(1).getPremio().replace(",", ".")));
				
				//category 3
				}else if(sameNumbers.size()==5 && lototurf.getHorse()==winNumbers.get(6)){
					ticket.setStatusName("Winning");
					ticket.setStatusPrice(Double.valueOf(lototurfAPI.getEscrutinio().get(2).getPremio().replace(",", ".")));
				
				//category 4
				}else if(sameNumbers.size()==5){
					ticket.setStatusName("Winning");
					ticket.setStatusPrice(Double.valueOf(lototurfAPI.getEscrutinio().get(3).getPremio().replace(",", ".")));

				//category 5
				}else if(sameNumbers.size()==4 && lototurf.getHorse()==winNumbers.get(6)){
					ticket.setStatusName("Winning");
					ticket.setStatusPrice(Double.valueOf(lototurfAPI.getEscrutinio().get(4).getPremio().replace(",", ".")));

				//category 6
				}else if(sameNumbers.size()==4){
					ticket.setStatusName("Winning");
					ticket.setStatusPrice(Double.valueOf(lototurfAPI.getEscrutinio().get(5).getPremio().replace(",", ".")));
				
                //category 7
				}else if(sameNumbers.size()==3 && lototurf.getHorse()==winNumbers.get(6)){
					ticket.setStatusName("Winning");
					ticket.setStatusPrice(Double.valueOf(lototurfAPI.getEscrutinio().get(6).getPremio().replace(",", ".")));
				
				//category 8
				}else if(lototurf.getNumList().contains(winNumbers.get(7))){
                    ticket.setStatusName("Winning");
					ticket.setStatusPrice(Double.valueOf(lototurfAPI.getEscrutinio().get(7).getPremio().replace(",", ".")));
				
                }else{
					ticket.setStatusName("Not Winning");
					ticket.setStatusPrice(ticket.getPaidByPice());
				}

			//if the ticket date doesnt exists in the map results
			}else{

				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
				String today = LocalDate.now().format(formatter);

				//the ticket date doesnt belong to a result; else statement = the result isnt ready yet
				if(lastNewDate.equals("") && !(Integer.valueOf(key)>Integer.valueOf(today))){
					save = true;
					ticket.setStatusName("Wrong Date");
				}		

				ticket.setStatusPrice(Double.valueOf(0.0));
			}

			if(save) lTicketsToUpdate.add(ticket);
			
		}

		return lTicketsToUpdate;
	}


	//primitiva
	public List<Ticket> checkAndChangePrimitivaStatus(List<PrimitivaAPI> lBetTypeResults, Map<String, Ticket> mDateToTicket){

		List<Ticket> lTicketsToUpdate = new ArrayList<Ticket>();
		Map<String, PrimitivaAPI> mDateToPrimitivaapi = new HashMap<String, PrimitivaAPI>();
		String formatDate = "";
        String lastNewDate = "";

		//get the tickets result by date
        if (lBetTypeResults != null){
            for(PrimitivaAPI primitivaAPI: lBetTypeResults){
                formatDate = primitivaAPI.getFecha_sorteo().substring(0, 10).replace("-", "");
                mDateToPrimitivaapi.put(formatDate, primitivaAPI);
            }

            List<String> datesAPI = new ArrayList<String>(mDateToPrimitivaapi.keySet());
            lastNewDate = datesAPI.get(0);
        }
		
		Primitiva primitiva;
		Ticket ticket;
		Boolean save;
		//check sthe status or if the date are wrong
		for(String key: mDateToTicket.keySet()){

			ticket = mDateToTicket.get(key);
			primitiva = (Primitiva) mDateToTicket.get(key).getTicketType();
			save = false;

			//if the ticket date exists in the map results
			if(mDateToPrimitivaapi.containsKey(key) && mDateToPrimitivaapi.get(key).getCombinacion()!=null){

				save = true;
				PrimitivaAPI primitivaAPI = mDateToPrimitivaapi.get(key);

				//get the winner numbers
				List<Integer> winNumbers = new ArrayList<>();	//0-5: numbers 	6: horse  7: reimbursement
				Matcher matcher = Pattern.compile("\\d+").matcher(primitivaAPI.getCombinacion());
				while (matcher.find()) {
					winNumbers.add(Integer.parseInt(matcher.group()));
				}


				/** CHECK THE RESULTS **/
				//get a list the same numbers for coincidence
				List<Integer> sameNumbers = new ArrayList<>(winNumbers.subList(0, 6));
				sameNumbers.retainAll(primitiva.getNumList());

				//category 1
				if(sameNumbers.size()==6 && primitiva.getReimbursement()==winNumbers.get(6)){
					ticket.setStatusName("Winning");
					ticket.setStatusPrice(Double.valueOf(primitivaAPI.getEscrutinio().get(0).getPremio().replace(",", ".")));

				//category 2
				}else if(sameNumbers.size()==6){
					ticket.setStatusName("Winning");
					ticket.setStatusPrice(Double.valueOf(primitivaAPI.getEscrutinio().get(1).getPremio().replace(",", ".")));
				
				//category 3
				}else if(sameNumbers.size()==5 && primitiva.getNumList().contains(winNumbers.get(7))){
					ticket.setStatusName("Winning");
					ticket.setStatusPrice(Double.valueOf(primitivaAPI.getEscrutinio().get(2).getPremio().replace(",", ".")));
				
				//category 4
				}else if(sameNumbers.size()==5){
					ticket.setStatusName("Winning");
					ticket.setStatusPrice(Double.valueOf(primitivaAPI.getEscrutinio().get(3).getPremio().replace(",", ".")));

				//category 5
				}else if(sameNumbers.size()==4){
					ticket.setStatusName("Winning");
					ticket.setStatusPrice(Double.valueOf(primitivaAPI.getEscrutinio().get(4).getPremio().replace(",", ".")));

				//category 6
				}else if(sameNumbers.size()==3){
					ticket.setStatusName("Winning");
					ticket.setStatusPrice(Double.valueOf(primitivaAPI.getEscrutinio().get(5).getPremio().replace(",", ".")));
			
				//category 7
				}else if(primitiva.getNumList().contains(winNumbers.get(7))){
                    ticket.setStatusName("Winning");
					ticket.setStatusPrice(Double.valueOf(primitivaAPI.getEscrutinio().get(6).getPremio().replace(",", ".")));
				
                }else{
					ticket.setStatusName("Not Winning");
					ticket.setStatusPrice(ticket.getPaidByPice());
				}

			//if the ticket date doesnt exists in the map results
			}else{

				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
				String today = LocalDate.now().format(formatter);

				//the ticket date doesnt belong to a result; else statement = the result isnt ready yet
				if(lastNewDate.equals("") && !(Integer.valueOf(key)>Integer.valueOf(today))){
					save = true;
					ticket.setStatusName("Wrong Date");
				}	

				ticket.setStatusPrice(Double.valueOf(0.0));
			}

			if(save) lTicketsToUpdate.add(ticket);
			
		}

		return lTicketsToUpdate;
	}




	//quiniela
	public List<Ticket> checkAndChangeQuinielaStatus(List<QuinielaAPI> lBetTypeResults, Map<String, Ticket> mDateToTicket){

		List<Ticket> lTicketsToUpdate = new ArrayList<Ticket>();
		Map<String, QuinielaAPI> mDateToQuinielaapi = new HashMap<String, QuinielaAPI>();
		String formatDate = "";
        String lastNewDate = "";

		//get the tickets result by date
        if (lBetTypeResults != null){
            for(QuinielaAPI quinielaAPI: lBetTypeResults){
                formatDate = quinielaAPI.getFecha_sorteo().substring(0, 10).replace("-", "");
                mDateToQuinielaapi.put(formatDate, quinielaAPI);
            }

            List<String> datesAPI = new ArrayList<String>(mDateToQuinielaapi.keySet());
            lastNewDate = datesAPI.get(0);
        }
		
		Quiniela quiniela;
		Ticket ticket;
		Boolean save;
		//check sthe status or if the date are wrong
		for(String key: mDateToTicket.keySet()){

			ticket = mDateToTicket.get(key);
			quiniela = (Quiniela) mDateToTicket.get(key).getTicketType();
			save = false;

			//inicialite the ticket
			ticket.setStatusName("Not Winning");
			ticket.setStatusPrice(ticket.getPaidByPice());

			//if the ticket date exists in the map results
			if(mDateToQuinielaapi.containsKey(key)){

				save = true;
				QuinielaAPI quinielaAPI = mDateToQuinielaapi.get(key);
				ticket.setStatusPrice(0.0);

				//get the winner numbers
				List<String> winNumbers = Arrays.stream(quinielaAPI.getCombinacion().split("-")).map(String::trim).collect(Collectors.toList());
				
				
				/** CHECK THE RESULTS **/
				int coincidences = 0;
				boolean isPleno15 = false;
				
				for(List<String> quinielaBet: quiniela.getAllCombinateBet()){

					//check the numbers
					for (int i = 0; i < quinielaBet.size()-1; i++) {
						if (quinielaBet.get(i).equals(winNumbers.get(i))) {
							coincidences++;
						}
					}

					//check pleno al 15
					if(coincidences>=10 && winNumbers.get(14).equals(quinielaBet.get(14))){
						isPleno15 = true;
					}


					//category 1
					if(isPleno15){
						ticket.setStatusPrice(Double.valueOf(quinielaAPI.getEscrutinio().get(0).getPremio().replace(",", ".")) + ticket.getStatusPrice());
					}
					
					//category 2
					if(coincidences==14){
						ticket.setStatusName("Winning");
						ticket.setStatusPrice(Double.valueOf(quinielaAPI.getEscrutinio().get(1).getPremio().replace(",", ".")) + ticket.getStatusPrice());
					
					//category 3
					}else if(coincidences==13){
						ticket.setStatusName("Winning");
						ticket.setStatusPrice(Double.valueOf(quinielaAPI.getEscrutinio().get(2).getPremio().replace(",", ".")) + ticket.getStatusPrice());
					
					//category 4
					}else if(coincidences==12){
						ticket.setStatusName("Winning");
						ticket.setStatusPrice(Double.valueOf(quinielaAPI.getEscrutinio().get(3).getPremio().replace(",", ".")) + ticket.getStatusPrice());

					//category 5
					}else if(coincidences==11){
						ticket.setStatusName("Winning");
						ticket.setStatusPrice(Double.valueOf(quinielaAPI.getEscrutinio().get(4).getPremio().replace(",", ".")) + ticket.getStatusPrice());

					//category 6
					}else if(coincidences==10){
						ticket.setStatusName("Winning");
						ticket.setStatusPrice(Double.valueOf(quinielaAPI.getEscrutinio().get(5).getPremio().replace(",", ".")) + ticket.getStatusPrice());
					}

				}

			//if the ticket date doesnt exists in the map results
			}else{

				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
				String today = LocalDate.now().format(formatter);

				//the ticket date doesnt belong to a result; else statement = the result isnt ready yet
				if(lastNewDate.equals("") && !(Integer.valueOf(key)>Integer.valueOf(today))){
					save = true;
					ticket.setStatusName("Wrong Date");
				}else{
					ticket.setStatusName("Pending");
				}	

				ticket.setStatusPrice(Double.valueOf(0.0));
				
			}

			if(save) lTicketsToUpdate.add(ticket);
			
		}

		return lTicketsToUpdate;
	}


	//quinigol
	public List<Ticket> checkAndChangeQuinigolStatus(List<QuinigolAPI> lBetTypeResults, Map<String, Ticket> mDateToTicket){

		List<Ticket> lTicketsToUpdate = new ArrayList<Ticket>();
		Map<String, QuinigolAPI> mDateToQuinigolapi = new HashMap<String, QuinigolAPI>();
		String formatDate = "";
        String lastNewDate = "";

		//get the tickets result by date
        if (lBetTypeResults != null){
            for(QuinigolAPI quinigolAPI: lBetTypeResults){
                formatDate = quinigolAPI.getFecha_sorteo().substring(0, 10).replace("-", "");
                mDateToQuinigolapi.put(formatDate, quinigolAPI);
            }

            List<String> datesAPI = new ArrayList<String>(mDateToQuinigolapi.keySet());
            lastNewDate = datesAPI.get(0);
        }
		
		Quinigol quinigol;
		Ticket ticket;
		Boolean save;
		//check sthe status or if the date are wrong
		for(String key: mDateToTicket.keySet()){

			ticket = mDateToTicket.get(key);
			quinigol = (Quinigol) mDateToTicket.get(key).getTicketType();
			save = false;

			//if the ticket date exists in the map results
			if(mDateToQuinigolapi.containsKey(key) && mDateToQuinigolapi.get(key).getCombinacion()!=null){

				save = true;
				QuinigolAPI quinigolAPI = mDateToQuinigolapi.get(key);

				//get the winner numbers
				List<String> winNumbers = Arrays.stream(quinigolAPI.getCombinacion().split("-")).map(String::trim).collect(Collectors.toList());
				
				
				/** CHECK THE RESULTS **/
				int coincidences = 0;

				List<String> lMatch = quinigol.getCombinatedBet();
				for (int i = 0; i < lMatch.size()-1; i++) {
					if (lMatch.get(i).equals(winNumbers.get(i))) {
						coincidences++;
					}
				}
				
				//category 1
				if(coincidences==6){
					ticket.setStatusName("Winning");
					ticket.setStatusPrice(Double.valueOf(quinigolAPI.getEscrutinio().get(0).getPremio().replace(",", ".")));
				
				//category 2
				}else if(coincidences==5){
					ticket.setStatusName("Winning");
					ticket.setStatusPrice(Double.valueOf(quinigolAPI.getEscrutinio().get(1).getPremio().replace(",", ".")));
				
				//category 3
				}else if(coincidences==4){
					ticket.setStatusName("Winning");
					ticket.setStatusPrice(Double.valueOf(quinigolAPI.getEscrutinio().get(2).getPremio().replace(",", ".")) );

				//category 4
				}else if(coincidences==3){
					ticket.setStatusName("Winning");
					ticket.setStatusPrice(Double.valueOf(quinigolAPI.getEscrutinio().get(3).getPremio().replace(",", ".")));

				//category 5
				}else if(coincidences==2){
					ticket.setStatusName("Winning");
					ticket.setStatusPrice(Double.valueOf(quinigolAPI.getEscrutinio().get(4).getPremio().replace(",", ".")));
				
				}else{
					ticket.setStatusName("Not Winning");
					ticket.setStatusPrice(ticket.getPaidByPice());
				}

			//if the ticket date doesnt exists in the map results
			}else{

				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
				String today = LocalDate.now().format(formatter);

				//the ticket date doesnt belong to a result; else statement = the result isnt ready yet
				if(lastNewDate.equals("") && !(Integer.valueOf(key)>Integer.valueOf(today))){
					save = true;
					ticket.setStatusName("Wrong Date");
				}	

				ticket.setStatusPrice(Double.valueOf(0.0));
			}

			if(save) lTicketsToUpdate.add(ticket);
			
		}

		return lTicketsToUpdate;
	}


	
	//quintuple
	public List<Ticket> checkAndChangeQuintupleStatus(List<QuintupleAPI> lBetTypeResults, Map<String, Ticket> mDateToTicket){

		List<Ticket> lTicketsToUpdate = new ArrayList<Ticket>();
		Map<String, QuintupleAPI> mDateToQuintupleapi = new HashMap<String, QuintupleAPI>();
		String formatDate = "";
        String lastNewDate = "";

		//get the tickets result by date
        if (lBetTypeResults != null){
            for(QuintupleAPI quintupleAPI: lBetTypeResults){
                formatDate = quintupleAPI.getFecha_sorteo().substring(0, 10).replace("-", "");
                mDateToQuintupleapi.put(formatDate, quintupleAPI);
            }

            List<String> datesAPI = new ArrayList<String>(mDateToQuintupleapi.keySet());
            lastNewDate = datesAPI.get(0);
        }
		
		Quintuple quintuple;
		Ticket ticket;
		Boolean save;
		//check sthe status or if the date are wrong
		for(String key: mDateToTicket.keySet()){

			ticket = mDateToTicket.get(key);
			quintuple = (Quintuple) mDateToTicket.get(key).getTicketType();
			save = false;

			//if the ticket date exists in the map results
			if(mDateToQuintupleapi.containsKey(key) && mDateToQuintupleapi.get(key).getCombinacion()!=null){

				save = true;
				
				QuintupleAPI quintupleAPI = mDateToQuintupleapi.get(key);

				//get the winner numbers
				List<String> winNumbers = Arrays.stream(quintupleAPI.getCombinacion().split("-")).map(String::trim).collect(Collectors.toList());
				
				
				/** CHECK THE RESULTS **/
				int coincidences = 0;

				List<String> lMatch = quintuple.getNumList() .stream().map(String::valueOf).collect(Collectors.toList());
				for (int i = 0; i < lMatch.size(); i++) {
					if (lMatch.get(i).equals(winNumbers.get(i))) {
						coincidences++;
					}
				}

				//category 1
				if(coincidences==5 && quintuple.getNum6().equals(winNumbers.get(4))){
					ticket.setStatusName("Winning");
					ticket.setStatusPrice(Double.valueOf(quintupleAPI.getEscrutinio().get(0).getPremio().replace(",", ".")) + Double.valueOf(quintupleAPI.getEscrutinio().get(1).getPremio().replace(",", ".")));

				//category 2
				}else if(coincidences==5){
					ticket.setStatusName("Winning");
					ticket.setStatusPrice(Double.valueOf(quintupleAPI.getEscrutinio().get(2).getPremio().replace(",", ".")));
				
				//category 3
				}else if(coincidences==4 && quintuple.getNum6().equals(winNumbers.get(4))){
					ticket.setStatusName("Winning");
					ticket.setStatusPrice(Double.valueOf(quintupleAPI.getEscrutinio().get(3).getPremio().replace(",", ".")));
				
				//category 4
				}else if(coincidences==4){
					ticket.setStatusName("Winning");
					ticket.setStatusPrice(Double.valueOf(quintupleAPI.getEscrutinio().get(4).getPremio().replace(",", ".")));

				}else{
					ticket.setStatusName("Not Winning");
					ticket.setStatusPrice(ticket.getPaidByPice());
				}

			//if the ticket date doesnt exists in the map results
			}else{

				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
				String today = LocalDate.now().format(formatter);

				//the ticket date doesnt belong to a result; else statement = the result isnt ready yet
				if(lastNewDate.equals("") && !(Integer.valueOf(key)>Integer.valueOf(today))){
					save = true;
					ticket.setStatusName("Wrong Date");
				}	

				ticket.setStatusPrice(Double.valueOf(0.0));
			}

			if(save) lTicketsToUpdate.add(ticket);
			
		}

		return lTicketsToUpdate;
	}

    
    
}
