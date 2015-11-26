package com.witl.kalimba.webplayer.webservice;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.springframework.beans.factory.annotation.Autowired;

import com.witl.kalimba.webplayer.common.Payment;
import com.witl.kalimba.webplayer.common.Transaction;
import com.witl.kalimba.webplayer.dao.PaymentDao;
import com.witl.kalimba.webplayer.dao.TransactionDao;

@Path("/Report")
public class PlayerWebService {
	@Autowired
	private PaymentDao paymentDao;
	@Autowired
	private TransactionDao transactionDao;

	private String tokenId;
	private String listOfSongs;
	private Payment payment;
	private String PnrID;

	
	

	@GET
	@Path("/getDownloadValidation")
	@Produces("application/json")
	public String getDownloadValidation(@QueryParam("PnrID") String PnrID,@QueryParam("tnsId") String tnsId)
	{
		transactionDao = new TransactionDao();
	Transaction downloadDao=  transactionDao.getById(PnrID);
	  tnsId=downloadDao.getTnsId();
	 String approval=downloadDao.getCcDapproval();
	    return approval;
	}
	
	/*private void getEmployees()
	{
	    final String uri = "http://localhost:8080/KalimbaWebPlayer/download";
	     
	    RestTemplate restTemplate = new RestTemplate();
	     
	    HttpHeaders headers = new HttpHeaders();
	    headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
	    HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
	     
	    ResponseEntity<String> result = restTemplate.exchange(uri, HttpMethod.POST, entity, String.class);
	     
	   
	}*/
	
	
	
	}
