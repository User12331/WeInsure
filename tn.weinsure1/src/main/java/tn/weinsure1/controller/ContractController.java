package tn.weinsure1.controller;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.ocpsoft.rewrite.el.ELBeanName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import tn.weinsure1.entities.Contract;
import tn.weinsure1.entities.ContractType;
import tn.weinsure1.entities.User;
import tn.weinsure1.repository.UserRepository;
import tn.weinsure1.service.IContractService;
import tn.weinsure1.service.ITableMortaliteService;
import tn.weinsure1.service.IUserService;
import tn.weinsure1.service.IsinisterService;

@Scope(value="session")
@Controller(value="ContractController")
@ELBeanName(value="ContractController")
public class ContractController {

	
	@Autowired
	IContractService ic;
	IUserService iu;
	IsinisterService is;
	ITableMortaliteService tr;
	
	private List<Contract> contracts,rentecontracts,viecontracts;
	private boolean approved;
	private User user;
	private String fname,lname;
	private Date exp_date,cre_date;
	private int duration;
	private ContractType contracttype;
	private float price, cost;
	private double rate;
	private long userid, cntid, cntIdToBeUpdated;

	
	public String addContract(){
		Date cr_date= new Date();
		LocalDate exp;
		String navigateTo="/contracts?faces-redirect=true";
		if (cre_date==null)
		{
		cre_date = cr_date;
		exp = cr_date.toInstant()
			      .atZone(ZoneId.systemDefault())
			      .toLocalDate().plusYears(duration);}
		else {exp = cre_date.toInstant()
			      .atZone(ZoneId.systemDefault())
			      .toLocalDate().plusYears(duration);}
		if (contracttype==ContractType.Vie)
		{
			if (price!=0 && cost !=0)
		{
			FacesMessage facesMessage= new FacesMessage("Merci de saisir seulement le capital ou bien la prime !");
			FacesContext.getCurrentInstance().addMessage("addcntform:pricecost", facesMessage);
			navigateTo="null";
		}
		else if (cost==0)
				ic.addOrUpdateContract(new Contract(cre_date,Date.from(exp.atStartOfDay(ZoneId.systemDefault()).toInstant()),duration,price,ContractType.Vie,rate,ic.PrimeVieUnique(price, userid, duration,rate)));
				else if (price ==0)
				ic.addOrUpdateContract(new Contract(cre_date,Date.from(exp.atStartOfDay(ZoneId.systemDefault()).toInstant()),duration,ic.CapitalVieUnique(cost, userid, duration, rate),ContractType.Vie,rate,cost));
				
		}
		else if (contracttype==ContractType.Rente)
		{if (price!=0 && cost !=0)
		{
			FacesMessage facesMessage= new FacesMessage("Merci de saisir seulement le capital ou bien la prime !");
			FacesContext.getCurrentInstance().addMessage("addcntform:pricecost", facesMessage);
			navigateTo="null";
		}
		else if (cost==0)
				ic.addOrUpdateContract(new Contract(cre_date,Date.from(exp.atStartOfDay(ZoneId.systemDefault()).toInstant()),duration,price,ContractType.Rente,rate,(float) ic.RITP(price, userid,rate)));
		else if (price ==0)
				ic.addOrUpdateContract(new Contract(cre_date,Date.from(exp.atStartOfDay(ZoneId.systemDefault()).toInstant()),duration,(float) ic.RITC(cost, userid,rate),ContractType.Rente,rate,cost));
				}
		return navigateTo;	
	}

	public String addVieContract(){
		Date cr_date= new Date();
		userid=1L;
		LocalDate exp;
		String navigateTo="/ucontracts?faces-redirect=true";
		if (cre_date==null)
		{
		cre_date = cr_date;
		exp = cr_date.toInstant()
			      .atZone(ZoneId.systemDefault())
			      .toLocalDate().plusYears(duration);}
		else {exp = cre_date.toInstant()
			      .atZone(ZoneId.systemDefault())
			      .toLocalDate().plusYears(duration);}
		if (price!=0 && cost !=0)
		{
			FacesMessage facesMessage= new FacesMessage("Merci de saisir seulement le capital ou bien la prime !");
			FacesContext.getCurrentInstance().addMessage("addcntform:pricecost", facesMessage);
			navigateTo="null";
		}
		else if (cost==0)
				ic.addOrUpdateContract(new Contract(cre_date,Date.from(exp.atStartOfDay(ZoneId.systemDefault()).toInstant()),duration,price,ContractType.Vie,rate,ic.PrimeVieUnique(price, userid, duration,rate)));
		else if (price ==0)
				ic.addOrUpdateContract(new Contract(cre_date,Date.from(exp.atStartOfDay(ZoneId.systemDefault()).toInstant()),duration,ic.CapitalVieUnique(cost, userid, duration, rate),ContractType.Vie,rate,cost));
		return navigateTo;	
	}
	
	public String addRenteContract(){
		Date cr_date= new Date();
		LocalDate exp;
		userid=1L;
		String navigateTo="/ucontracts?faces-redirect=true";
		if (cre_date==null)
		{
		cre_date = cr_date;
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(iu.RetrieveUser(Long.toString(userid)).getBirthdate());
		int BDay = calendar.get(Calendar.YEAR);
		LocalDate now = LocalDate.now();
		int ageClient = now.getYear()-BDay;
		duration=tr.findAgeMax()-ageClient;
		exp = cr_date.toInstant()
			      .atZone(ZoneId.systemDefault())
			      .toLocalDate().plusYears(duration);}
		else {exp = cre_date.toInstant()
			      .atZone(ZoneId.systemDefault())
			      .toLocalDate().plusYears(duration);}
		if (price!=0 && cost !=0)
		{
			FacesMessage facesMessage= new FacesMessage("Merci de saisir seulement le capital ou bien la prime !");
			FacesContext.getCurrentInstance().addMessage("addcntform:pricecost", facesMessage);
			navigateTo="null";
		}
		else if (cost==0)
				ic.addOrUpdateContract(new Contract(cre_date,Date.from(exp.atStartOfDay(ZoneId.systemDefault()).toInstant()),duration,price,ContractType.Rente,rate,(float) ic.RITP(price, userid,rate)));
		else if (price ==0)
				ic.addOrUpdateContract(new Contract(cre_date,Date.from(exp.atStartOfDay(ZoneId.systemDefault()).toInstant()),duration,(float) ic.RITC(cost, userid,rate),ContractType.Rente,rate,cost));
				
		return navigateTo;}
	
	public String updateContract(){
		ic.addOrUpdateContract(new Contract(cntid,cre_date,exp_date,duration,price,contracttype,rate,approved,cost));
		ic.ContractToUser(cntIdToBeUpdated, userid);
		return "/contracts?faces-redirect=true";
	}

	public String deleteContract(String cntid){
		ic.DeleteContract(Long.parseLong(cntid));
		return "/contracts?faces-redirect=true";
	}
	
	public String afficherContract(Contract cnt) {
		this.setApproved(cnt.isApproved());
		this.setCntid(cnt.getIdcontract());
		this.setContracttype(cnt.getType());
		this.setCre_date(cnt.getCreation_date());
		this.setExp_date(cnt.getExpiration_date());
		this.setDuration(cnt.getDuration());
		this.setPrice(cnt.getPrice());
		this.setCost(cnt.getCost());
		this.setRate(cnt.getRate());
		this.setCntIdToBeUpdated(cnt.getIdcontract());
		return "/addcontract?faces-redirect=true";
		}
	
	public String affecterUsertoContract(){
		ic.ContractToUser(cntIdToBeUpdated, userid);
		return "/contracts?faces-redirect=true";
	}
	
	public String approveContract(Contract cnt){
		ic.ApproveContract(cnt.getIdcontract());
		return "/contracts?faces-redirect=true";	}

	public String resignContract(String cntid){
		String navigateTo="null";
		
		
		return navigateTo;
	}
	
	///SETTERS GETTER
	public void setContracts(List<Contract> contracts) {
		this.contracts = contracts;
	}

	public List<User> getUsers(){
		return (ic.retrieveallusers());
	}
	
	public List<Contract> getContracts() {
		contracts = ic.RetrieveAllContracts();
		return contracts;
	}
	
	public List<Contract> getVieContracts(){
		viecontracts = ic.retrieveContractsbytype(ContractType.Vie);
		return viecontracts;
	}
	
	public List<Contract> getRenteContracts(){
		rentecontracts = ic.retrieveContractsbytype(ContractType.Rente);
		return rentecontracts;
	}
	
	public void setRentecontracts(List<Contract> rentecontracts) {
		this.rentecontracts = rentecontracts;
	}

	public void setViecontracts(List<Contract> viecontracts) {
		this.viecontracts = viecontracts;
	}
	
	public boolean isApproved() {
		return approved;
	}

	public void setApproved(boolean approved) {
		this.approved = approved;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getFname() {
		return fname;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}

	public String getLname() {
		return lname;
	}

	public void setLname(String lname) {
		this.lname = lname;
	}

	public Date getExp_date() {
		return exp_date;
	}

	public void setExp_date(Date exp_date) {
		this.exp_date = exp_date;
	}

	public Date getCre_date() {
		return cre_date;
	}

	public void setCre_date(Date cre_date) {
		this.cre_date = cre_date;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public ContractType[] getTypes(){
		return ContractType.values();
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public float getCost() {
		return cost;
	}

	public void setCost(float cost) {
		this.cost = cost;
	}

	public long getUserid() {
		return userid;
	}

	public void setUserid(long userid) {
		this.userid = userid;
	}

	public long getCntid() {
		return cntid;
	}

	public void setCntid(long cntid) {
		this.cntid = cntid;
	}

	public long getCntIdToBeUpdated() {
		return cntIdToBeUpdated;
	}

	public void setCntIdToBeUpdated(long cntIdToBeUpdated) {
		this.cntIdToBeUpdated = cntIdToBeUpdated;
	}

	public ContractType getContracttype() {
		return contracttype;
	}

	public void setContracttype(ContractType contracttype) {
		this.contracttype = contracttype;
	}

	public double getRate() {
		return rate;
	}

	public void setRate(double rate) {
		this.rate = rate;
	}

}
