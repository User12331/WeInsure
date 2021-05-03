package tn.weinsure1.restController;


import java.io.IOException; 
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.time.LocalDateTime;

import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import tn.weinsure1.entities.Contract;
import tn.weinsure1.entities.Smsrequest;
import tn.weinsure1.entities.TransType;
import tn.weinsure1.entities.Transaction;
import tn.weinsure1.repository.ContractRepository;
import tn.weinsure1.repository.UserRepository;
import tn.weinsure1.service.ContractServiceImpl;
import tn.weinsure1.service.Smsservice;
import tn.weinsure1.service.TransactionServiceImpl;



@RestController
@RequestMapping("/transaction")
public class TransactionRestController {

	@Autowired
	TransactionServiceImpl transService;
	
	@Autowired
	ContractServiceImpl cr;
	
	@Autowired
	ContractRepository crr;
	
	@Autowired
	UserRepository userRepository;
	@Autowired
	Smsservice sm;
	
	
	@GetMapping("/all")
	@ResponseBody
	public List<Transaction> getTransaction (){

	List<Transaction> list =  transService.retrieveAllTransactions();
	return list;
	}
	
	
		
	@GetMapping("/all/{id}")
	@ResponseBody
	public Transaction getTransaction (@PathVariable("id")int id){
	  Transaction t =transService.retrieveTransactions(id);
	  return t;
	
	}
	/*
	@GetMapping("/project/{name}")
	@ResponseBody
	public List<Project> findByProjectnameOrderByProjectdateAsc (@PathVariable("name")String name){
	  return projectRepository.findByProjectnameOrderByProjectdateAsc(name);
	}
	*/
	//Conversion
	public Date convertToDateViaSqlTimestamp(LocalDateTime dateToConvert) {
	    return java.sql.Timestamp.valueOf(dateToConvert);
	}
	
	
	//as an investor
	@GetMapping("/GiveMoney/{idContract}")
	@ResponseBody
	public ResponseEntity<Object> payWithCoupon (@PathVariable("idContract")Long idC , Smsrequest smsrequest){
		Date d=convertToDateViaSqlTimestamp(LocalDateTime.now());
	
		Optional<Contract> ContractOptional=crr.findById(idC);
	    Contract c=ContractOptional.get();	 
	    float amout = c.getPrice() ; 
	    double nbreC = c.getRate();
		float sum= (float) (amout * nbreC);
		System.out.println(""+sum);
		float sum1 = sum + amout ; 
		System.out.println(""+sum1);
		Transaction t =new Transaction(d,sum1,TransType.debit);
        t.setTransactionprice(c);
        t.setNbreC((int) nbreC);
        t. setAmountC(c.getPrice());
		transService.addTransaction(t);
		String status=sm.sendsms(smsrequest);
		   if("sent".equals(status)||"queued".equals(status))
	       {
	       	return new ResponseEntity<Object>("sent successfully",HttpStatus.OK);
	       }
		   return new ResponseEntity<Object>("failed to send message",HttpStatus.NOT_FOUND);
		
	}
	@GetMapping("/statistic/{year}")
	public String statisticMonthByAmount(@PathVariable("year") int year) throws IOException {
		Map<Double, Double> MA = new HashMap<Double, Double>();
	    MA = transService.StatisticMonthbyAmount(year);	
		String json = new ObjectMapper().writeValueAsString(MA);
		return json;
		
	}
	
}