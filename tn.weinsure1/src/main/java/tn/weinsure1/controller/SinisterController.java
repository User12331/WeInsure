package tn.weinsure1.controller;

import java.io.IOException; 
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.ocpsoft.rewrite.annotation.Join;
import org.ocpsoft.rewrite.el.ELBeanName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


import tn.weinsure1.entities.SinisterMotif;
import tn.weinsure1.entities.User;
import tn.weinsure1.entities.sinister;
import tn.weinsure1.entities.sinisterstatus;
import tn.weinsure1.entities.typeSinister;
import tn.weinsure1.repository.sinisterRepository;
import tn.weinsure1.service.ITableMortaliteService;
import tn.weinsure1.service.IUserService;
import tn.weinsure1.service.IsinisterService;
import tn.weinsure1.service.TableMortaliteService;

@Scope(value = "session")
@Controller(value = "sinController") // Name of the bean in Spring IoC
@ELBeanName(value = "sinController") // Name of the bean used by JSF
@Join(path = "/", to = "/temm.jsf")

public class SinisterController {
	@Autowired
	IsinisterService sinService;
	@Autowired 
	sinisterRepository sr ; 

	
	private Long idSinistre ;
	
	private typeSinister typeSinistre;
     
	private String description;
	 
	private Date dateOccurence;
	
	private sinisterstatus status;
	 
	private SinisterMotif motifStatus;
	
	 private User user;
	
	 private byte[] documents ;
	 
	 private List<sinister> sinistres;
	 private List<sinister> sinistresPerUser;
	 
	 public List<sinister> getSinistresPerUser() {
		 
		 sinistresPerUser = sinService.findbyuserid(7L);
			return sinistresPerUser;
		 
		 
	
	}
	public void setSinistresPerUser(List<sinister> sinistresPerUser) {
		this.sinistresPerUser = sinistresPerUser;
	}

	private float reglemntation ;
	 
	 // ****************************** caclul 

	 
	 	 

	//*********************************************************************//
	
	
	
	public byte[] getDocuments() {
		return documents;
	}
	public float getReglemntation() {
		return reglemntation;
	}
	public void setReglemntation(float reglemntation) {
		this.reglemntation = reglemntation;
	}
	public void setDocuments(byte[] documents) {
		this.documents = documents;
	}
	public void setSinistres(List<sinister> sinistres) {
		this.sinistres = sinistres;
	}
	public IsinisterService getSinService() {
			return sinService;
		}
		public void setUserService(IsinisterService sinService) {
			this.sinService = sinService;
		}
		public Long getIdSinistre() {
			return idSinistre;
		}
		public void setIdSinistre(Long idSinistre) {
			this.idSinistre = idSinistre;
		}
		public typeSinister getTypeSinistre() {
			return typeSinistre;
		}
		public void setTypeSinistre(typeSinister typeSinistre) {
			this.typeSinistre = typeSinistre;
		}
		public String getDescription() {
			return description;
		}
		public void setDescription(String description) {
			this.description = description;
		}
		public Date getDateOccurence() {
			return dateOccurence;
		}
		public void setDateOccurence(Date dateOccurence) {
			this.dateOccurence = dateOccurence;
		}
		public sinisterstatus getStatus() {
			return status;
		}
		public void setStatus(sinisterstatus status) {
			this.status = status;
		}
		public SinisterMotif getMotifStatus() {
			return motifStatus;
		}
		public void setMotifStatus(SinisterMotif motifStatus) {
			this.motifStatus = motifStatus;
		}
		public User getPerson() {
			return user;
		}
		public void setPerson(User person) {
			this.user = person;
		}
		public void setSinService(IsinisterService sinService) {
			this.sinService = sinService;
		}
		
		
		//*******************************//
		public sinisterstatus[] getStatuss() { return sinisterstatus.values(); }
		public typeSinister[] getTypes() { return typeSinister.values(); }
		
		
	
		public List<sinister> getSinistres() {
			sinistres = sinService.getAllSinistres();
			return sinistres;
			}
		public String informations(long id) {
			this.idSinistre = id;
			System.out.println(id);
			return "/sinisterAdminDetails?faces-redirect=true";
		}
		
		
		
		public String  addSinistre() {
			Calendar currentdate = Calendar.getInstance(); 
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
			else {
				sinService.addOrUpdateSinistre(new sinister(typeSinistre, description, dateOccurence, sinisterstatus.enAttente, documents,user));
				navigateTo = "/affichagedetail?faces-redirect=true" ;
				}
			return navigateTo; 
		}
		
		
		public String removeSinistre(Long id)
		{
			sinService.deleteSinistre(id);
			return "/newAdmin?faces-redirect=ture";
		}
		
		// UPDATE
		private long sinistreIdToBeUpdated; // Ajouter getter et setter
		

		 
		public long getSinistreIdToBeUpdated() {
			return sinistreIdToBeUpdated;
		}
		public void setSinistreIdToBeUpdated(long sinistreIdToBeUpdated) {
			this.sinistreIdToBeUpdated = sinistreIdToBeUpdated;
		}
		
		//******************* affich//
		public String displaySinistre(sinister empl)
		{
		this.setTypeSinistre(empl.getTypeSinistre());
		this.setDescription(empl.getDescription());
		this.setDateOccurence(empl.getDateOccurence());
		this.setStatus(empl.getStatus());
		this.setDocuments(empl.getDocuments());
		this.setPerson(empl.getUser());
		this.setSinistreIdToBeUpdated(empl.getIdSinistre());
		return "/sinisterAdminDetails?faces-redirect=ture";
		}
		
		public void updateSinistre()
		{  
		  sinService.addOrUpdateSinistre(new sinister(sinistreIdToBeUpdated,typeSinistre, description, dateOccurence, sinisterstatus.enAttente, documents,user));
		}
		
		public User getUser() {
			return user;
		}
		public void setUser(User user) {
			this.user = user;
		}
			//CHECK STATUS
			public void updateStatus()
		{
			sinService.CheckStatus();
		}
			public void sendMail()
			{
				 sinService.SendMail();
				
			}
			
			/* ***************CALCUL***************** */
		
			public String CalculCVE(Long ids)
			{ 
				    double val1= 0;
				    val1 = sinService.CVE(ids);
				    return ""+ val1 + "" ;
			}

			public String CalculCD(Long ids) throws ParseException
			{ 
				    double val1= 0;
				    val1 = sinService.CapitalCasDéces(ids);
				    return ""+ val1 + "" ;
			}

			public String CalculTDE(Long ids) throws ParseException
			{ 
				    double val1= 0;
				    val1 = sinService.TDEMPRUNTEUR(ids);
				    return ""+ val1 + "" ;
			}

			public String CalculCDP(Long ids) 
			{  		System.out.println(""+ids);
				    double val2= 0;
				    val2 = sinService.CapitalDécesPeriodique(ids);
				    return ""+ val2 + "" ;
			}
			public boolean check(String s ){
				if(this.typeSinistre.toString().equals(s)){
					return true ; 
				}
				return false ;
			}
			   public String goToUpdateSinistre(sinister empl)
				{
			    	
	
				   this.setTypeSinistre(empl.getTypeSinistre());
					this.setDescription(empl.getDescription());
					this.setDateOccurence(empl.getDateOccurence());
					this.setStatus(empl.getStatus());
					this.setDocuments(empl.getDocuments());
					this.setPerson(empl.getUser());
					this.setSinistreIdToBeUpdated(empl.getIdSinistre());
				
			    return "/updateSinister?faces-redirect=true";

				}
			
			
			
}