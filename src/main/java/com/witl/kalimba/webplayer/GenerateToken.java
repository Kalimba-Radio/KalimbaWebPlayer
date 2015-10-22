package com.witl.kalimba.webplayer;

import java.io.IOException;
import java.io.StringReader;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

@Controller
@ComponentScan({ "com.witl.kalimba.webplayer" })
public class GenerateToken {

	@Autowired
	private PaymentDao paymentDao;

	private String tokenId;
	private String listOfSongs;

	@RequestMapping(value = "/getToken", method = RequestMethod.GET)
	@ResponseBody
	public String getToken(HttpServletRequest request,
			HttpServletResponse response, HttpSession session)
			throws SAXException, IOException, ParserConfigurationException,
			URISyntaxException {

		String items = (String) request.getParameter("itemsdetails");
		listOfSongs = items;

		System.out.println(request.getParameter("totalPrice"));
		String price = request.getParameter("totalPrice");
		float pricefloat = Float.parseFloat(price);
		pricefloat = (float) Math.floor(pricefloat * 100) / 100;
		String newprice = Float.toString(pricefloat);
		System.out.println(pricefloat);
	   
		request.getParameter("");
		request.getParameter("");

		// http cleient call
		String xmlString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+ "<API3G>"
				+ "<CompanyToken>68B90B5E-25F6-4146-8AB1-C7A3A0C41A7F</CompanyToken>"
				+ "<Request>createToken</Request>"
				+ "<Transaction>"
				+ "<PaymentAmount>"
				+ newprice
				+ "</PaymentAmount>"
				+ "<PaymentCurrency>ZMK</PaymentCurrency>"
				+ "<CompanyRef>49FKEOA</CompanyRef>"
				+ "<RedirectURL>www.kalimbaradio.com/transaction</RedirectURL>"
				+ "<BackURL>www.kalimbaradio.com/getTransaction</BackURL>"
				+ "<CompanyRefUnique>0</CompanyRefUnique>"
				+ "<PTL>5</PTL>"
				+ "</Transaction>"
				+ "<Services>"
				+ "<Service>"
				+ "<ServiceType>949</ServiceType>"
				+ "<ServiceDescription>Kalimba Radio music purchase</ServiceDescription>"
				+ "<ServiceDate>2013/12/20 19:00</ServiceDate>" + "</Service>"
				+ "</Services>" + "</API3G>";
		DefaultHttpClient httpClient = new DefaultHttpClient();

		HttpPost httpRequest = new HttpPost("https://secure.3gdirectpay.com/API/v5/");
		httpRequest.setHeader("Content-Type", "application/xml");
		StringEntity xmlEntity;
		xmlEntity = new StringEntity(xmlString);
		httpRequest.setEntity(xmlEntity);
		HttpResponse httpresponse = httpClient.execute(httpRequest);
		String result = EntityUtils.toString(httpresponse.getEntity());
		System.out.println(result);

		DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		InputSource is = new InputSource();
		is.setCharacterStream(new StringReader(result));

		Document doc = db.parse(is);

		Node node = doc.getElementsByTagName("TransToken").item(0).getFirstChild();
		System.out.println("Value of token=" + node.getNodeValue());

		Node check = doc.getElementsByTagName("ResultExplanation").item(0).getFirstChild();
		System.out.println("check success or failure =" + check.getNodeValue());
		tokenId = node.getNodeValue();

		Payment payment = new Payment();
		payment.setAmonut(pricefloat);
		payment.setCreatetokenstatus(check.getNodeValue());
		payment.setEmailid("dipti.prakash@kalimbaradio.com");
		payment.setSongidstring(items);
		payment.setTokenid(tokenId);

		paymentDao.save(payment);
		List songIdList=getSongList(listOfSongs);
		System.out.println(songIdList);

		return tokenId;

	}

	@RequestMapping(value = "/getTransaction", method = RequestMethod.GET)
//	@ResponseBody
	public ModelAndView verifyTransaction(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) {
		String tnsId = request.getParameter("paymenttokenid");
		/*
		 * session.getParameter("emailId");
		 * request.getParameter("paymentstatuscode");
		 * request.getParameter("paymenttimestamp");
		 */
		Payment paymentDetails = paymentDao.getById(tokenId);
		String token = paymentDetails.getTokenid();
		String status = paymentDetails.getCreatetokenstatus();

		if (token.equals(tnsId) && status.equals("Transaction created")) {// if
			List songIdList=getSongList(listOfSongs);														// there
																			// are
																			// any
																			// extra
																			// condition
																			// we
																			// put
																			// here
			// go for rest call
		} else {
			return new ModelAndView("failure");
		}

		return new ModelAndView("success");

	}
	
	
	private List<Integer> getSongList(String listOfSong){
		StringTokenizer st = new StringTokenizer(listOfSong,",");
		List<Integer> songIdList=new ArrayList<Integer>();
        while(st.hasMoreTokens()){
          songIdList.add(Integer.parseInt(st.nextToken()));
        }		
		return songIdList;
	}

}
