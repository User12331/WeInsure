package tn.weinsure1.controller;

import java.util.Date;
import java.util.List;
import org.ocpsoft.rewrite.annotation.Join;
import org.ocpsoft.rewrite.el.ELBeanName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import tn.weinsure1.entities.Contract;
import tn.weinsure1.entities.ContractType;
import tn.weinsure1.entities.User;
import tn.weinsure1.service.IContractService;
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
	
	private List<Contract> contracts;
	private boolean approved;
	private User user;
	private String fname,lname;
	private Date exp_date,cre_date;
	private int duration;
	private ContractType contracttype;
	private float price, cost;
	private double rate;
	private long userid, cntid;

	public List<Contract> getContracts() {
		contracts = ic.RetrieveAllContracts();
		return contracts;
	}

	public void setContracts(List<Contract> contracts) {
		this.contracts = contracts;
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
