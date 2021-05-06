package tn.weinsure1.controller;

import java.time.LocalDateTime; 
import java.util.Date;
import java.util.List;

import org.ocpsoft.rewrite.el.ELBeanName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;

import tn.weinsure1.entities.Contract;
import tn.weinsure1.entities.ContractType;
import tn.weinsure1.entities.Smsrequest;
import tn.weinsure1.entities.TransType;
import tn.weinsure1.entities.Transaction;
import tn.weinsure1.entities.User;
import tn.weinsure1.entities.sinister;
import tn.weinsure1.entities.typeSinister;
import tn.weinsure1.service.IContractService;
import tn.weinsure1.service.ITransactionService;
import tn.weinsure1.service.PaypalService;
import tn.weinsure1.service.Smsservice;



@Scope(value = "session")
@Controller(value = "transacController") // Name of the bean in Spring IoC
@ELBeanName(value = "transacController") // Name of the bean used by JSF
//@Join(path = "/", to = "/pages/admin/afficherSins.jsf")
public class TransactionControlImpl {
	@Autowired
	ITransactionService transacService;
	@Autowired
	   private Smsservice smsservice;

	@Autowired
	IContractService contract;
	@Autowired
	PaypalService paypal;
		
    private int transactionid;
	
	
	private Date transactionDate;

	private float transactionAmount;
	
	private TransType transactionType;
	
    private Contract transactionprice;

    private List<Transaction> tranSs;
    
    public List<Transaction> getTranSs() {
    	tranSs =transacService.listAll();
		return tranSs;
	}



	public void setTranSs(List<Transaction> tranSs) {
		this.tranSs = tranSs;
	}




	//contart
    private Float premiumContract;
    private String NomContract;
	private long idContract;

	public String getNomContract() {
		return NomContract;
	}

	public void setNomContract(String nomContract) {
		NomContract = nomContract;
	}

	public long getIdContract() {
		return idContract;
	}

	public void setIdContract(long idContract) {
		this.idContract = idContract;
	}

	public Float getPremiumContract() {
		return premiumContract;
	}

	public void setPremiumContract(Float premiumContract) {
		this.premiumContract = premiumContract;
	}

	public ITransactionService getTransacService() {
		return transacService;
	}

	public void setTransacService(ITransactionService transacService) {
		this.transacService = transacService;
	}

	public int getTransactionid() {
		return transactionid;
	}

	public void setTransactionid(int transactionid) {
		this.transactionid = transactionid;
	}

	public Date getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}

	public float getTransactionAmount() {
		return transactionAmount;
	}

	public void setTransactionAmount(float transactionAmount) {
		this.transactionAmount = transactionAmount;
	}

	public TransType getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(TransType transactionType) {
		this.transactionType = transactionType;
	}

	public Contract getTransactionprice() {
	
		return transactionprice;
	}

	public void setTransactionprice(Contract transactionprice) {
		this.transactionprice = transactionprice;
	}
	
	/* ********************************************************* */
	
	 private List<Contract> contracts; 
	 private List<Transaction> transBYID; 
	 private List<Transaction> TransALl; 
	 public TransType[] getTypess() { return TransType.values(); }
	 
	 
	 
	public List<Transaction> getTransALl() {
		return TransALl;
	}

	public void setTransALl(List<Transaction> transALl) {
		TransALl = transALl;
	}

	public List<Transaction> getTransBYID() {
		return transBYID;
	}

	public void setTransBYID(List<Transaction> transBYID) {
		this.transBYID = transBYID;
	}

	public IContractService getContract() {
		return contract;
	}

	public void setContract(IContractService contract) {
		this.contract = contract;
	}

	public List<Contract> getContracts() {
		return contracts;
	}

	public void setContracts(List<Contract> contracts) {
		this.contracts = contracts;
	}

	/*public List<Contract> getTypes() {
		contracts = contract.findByIdPerson();
		return contracts;
		}*/
	public List<Long> getTypes() {
		List<Long> idc = contract.findByIdPerson();
		return idc;
	}
	

 //////CONTRACT
	
	public Long getIDContratbyNom(ContractType nom) {
		Long idc = contract.findIdContratByNom(nom);
		return idc;
	}
	
	
	public Long getIDContrat(float prime) {
		Long idc = contract.findIdContrat(prime);
		return idc;
	}
	
	
	
	
	
	public List<String> getNomContrat() {
		List<String> idc = contract.findByIdPerson2();
		return idc;
	}
	public String  addTransaction( long idC) {
		
	/*	Calendar currentdate = Calendar.getInstance(); 
		Date d = currentdate.getTime();  
		
		currentdate.add(Calendar.DAY_OF_MONTH,-5);
		Date d1= currentdate.getTime();
		System.out.println(""+d1);
		String navigateTo = "null"; 
		System.out.println(""+dateOccurence);
		if( dateOccurence.compareTo(d1) <0 ){
			FacesMessage facesMessage= new FacesMessage("Date Invalid: "
			    	+ "Please check occurence Date");
		FacesContext.getCurrentInstance().addMessage("form:btn", facesMessage);
		}
		else {*/
		//houni ndakhel ic contrat mtaa transaction (nrecuperih mel sinistre -> person-- > contrat w baad nekhou donnée mel contrat
		//type contrat + prime + date+
		
		Contract c=  contract.RetrieveContract2(idC);
		
		String navigateTo = "null"; 
		//Smsrequest smsrequest = new Smsrequest("+216 97 841 301","+21697841301");
		//String status=smsservice.sendsms(smsrequest);
			transacService.addOrUpdateTransaction(new Transaction(convertToDateViaSqlTimestamp(LocalDateTime.now()), c.getPrice(),transactionType,c));
			
			 
			
			navigateTo = "/TransactionUser.xhtml?faces-redirect=true" ;
			//}
		return navigateTo; 
	}
	//date tawa
	public Date convertToDateViaSqlTimestamp(LocalDateTime dateToConvert) {
		return java.sql.Timestamp.valueOf(dateToConvert);
	}
	

	public List<Transaction> getTransactionbyID() {
		transBYID = transacService.getAllTransById();
		return transBYID;
		}
	public List<Transaction> getTransactionAll() {
		TransALl = transacService.retrieveAllTransactions();
		return transBYID;
		}
	/////////////////////////////////////////PAYPAL
    public String payment(int idt ) throws PayPalRESTException {return "";}
		
	/*	Transaction c= transacService.retrieveTransactions(idt);
		//Payment payment = paypal.createPayment(c.getTransactionAmount(), c.getTransactionprice()., c.getTransactionType().toString(), c.getTransactionprice().getIntent(), c.getTransactionprice().getDescriptionContract(),
		//		"http://localhost:8000/", "https://www.sandbox.paypal.com/cgi-bin/webscr?cmd=_express-checkout&token=EC-00R637296Y265914W" );
				//(c.getPremiumContract(), c.getCurrency() , c.getPaymentType(), c.getIntent(), c.getDescriptionContract()
	/*	for(Links link:payment.getLinks()) {
			if(link.getRel().equals("approval_url")) {
				//return "success" ;
				return "redirect:"+ "https://www.sandbox.paypal.com/cgi-bin/webscr?cmd=_express-checkout&token=EC-00R637296Y265914W " ;//link.getHref();
			}
		}
		//return "redirect:/"; 
		return "paypal";
	}

    
   /* public static final String SUCCESS_URL = "pay/success";
	public static final String CANCEL_URL = "pay/cancel";*/

	//@GetMapping(value = SUCCESS_URL) // cas de sucess
	public String successPay( String paymentId,   String payerId) {
return "" ;
	}
	
	
	//delete

	public String removeTransaction(int tId)
	{
		transacService.deleteTransaction(tId);
		return "/newAdminT?faces-redirect=ture";
	}
	public String displayTransaction(Transaction empl)
	{
		
	this.setTransactionType(empl.getTransactionType());	
	this.setTransactionDate(empl.getTransactionDate());
	this.setTransactionAmount(empl.getTransactionAmount());
	this.setTransactionprice(empl.getTransactionprice());
	return "/TransAdminDetails?faces-redirect=ture";
	
	}
	
}
