package com.witl.kalimba.webplayer;

import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.net.URISyntaxException;

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
public class GenerateToken {
	//ModelAndView modelAndView;
	String tokenId;
	@RequestMapping(value = "/getToken", method = RequestMethod.GET)
	@ResponseBody
	public String getToken(HttpServletRequest request,HttpServletResponse response,HttpSession session) throws SAXException, IOException, ParserConfigurationException, URISyntaxException {
		//System.out.println( request.getParameter("totalPrice"));
		//String price=request.getParameter("totalPrice");
		//System.out.println(price);
		//BigDecimal validAmount = new BigDecimal(price);
		//System.out.println(validAmount);
		//Double total=Double.parseDouble(price);
		//System.out.println(total);
		request.getParameter("");
		request.getParameter("");
		request.getParameter("");
		
		//http cleient call
		String xmlString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><API3G><CompanyToken>68B90B5E-25F6-4146-8AB1-C7A3A0C41A7F</CompanyToken><Request>createToken</Request><Transaction><PaymentAmount>8.00</PaymentAmount><PaymentCurrency>ZMK</PaymentCurrency><CompanyRef>49FKEOA</CompanyRef><RedirectURL>http://www.kalimbaradio.com</RedirectURL><BackURL>http://www.kalimbaradio.com</BackURL><CompanyRefUnique>0</CompanyRefUnique><PTL>5</PTL></Transaction><Services><Service><ServiceType>949</ServiceType><ServiceDescription>Kalimba Radio music purchase</ServiceDescription><ServiceDate>2013/12/20 19:00</ServiceDate></Service></Services></API3G>";
		DefaultHttpClient httpClient = new DefaultHttpClient();
		
		HttpPost httpRequest = new HttpPost("https://secure.3gdirectpay.com/API/v5/");
		httpRequest.setHeader("Content-Type", "application/xml");		
		StringEntity xmlEntity;
		xmlEntity = new StringEntity(xmlString);
		httpRequest.setEntity(xmlEntity);
		HttpResponse httpresponse = httpClient.execute(httpRequest);
		String result = EntityUtils.toString(httpresponse.getEntity());
		System.out.println(result);

		DocumentBuilder db = DocumentBuilderFactory.newInstance()
				.newDocumentBuilder();
		InputSource is = new InputSource();
		is.setCharacterStream(new StringReader(result));

		Document doc = db.parse(is);

		Node node = doc.getElementsByTagName("TransToken").item(0).getFirstChild();
		System.out.println("Value of token=" + node.getNodeValue());

		Node check = doc.getElementsByTagName("ResultExplanation").item(0).getFirstChild();
		System.out.println("check success or failure =" + check.getNodeValue());			 
		 tokenId=node.getNodeValue();
		 return tokenId;
		
	

	}
			
	
	
}
