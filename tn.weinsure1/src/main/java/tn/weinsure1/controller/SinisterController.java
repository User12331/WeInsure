package tn.weinsure1.controller;

import java.io.IOException;   
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.annotation.MultipartConfig;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.ocpsoft.rewrite.annotation.Join;
import org.ocpsoft.rewrite.el.ELBeanName;

import org.primefaces.model.chart.PieChartModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import tn.weinsure1.entities.Contract;
import tn.weinsure1.entities.ContractType;
import tn.weinsure1.entities.SinisterMotif;
import tn.weinsure1.entities.User;
import tn.weinsure1.entities.sinister;
import tn.weinsure1.entities.sinisterstatus;
import tn.weinsure1.entities.typeSinister;
import tn.weinsure1.repository.UserRepository;
import tn.weinsure1.repository.sinisterRepository;
import tn.weinsure1.service.IContractService;
import tn.weinsure1.service.ITableMortaliteService;
import tn.weinsure1.service.IUserService;
import tn.weinsure1.service.IsinisterService;
import tn.weinsure1.service.TableMortaliteService;

@Scope(value = "session")
@Controller(value = "sinController") // Name of the bean in Spring IoC
@ELBeanName(value = "sinController") // Name of the bean used by JSF
@Join(path = "/", to = "/temm.jsf")
@MultipartConfig
public class SinisterController {
	@Autowired
	IsinisterService sinService;
	@Autowired 
	sinisterRepository sr ; 
	@Autowired
	IContractService cs;
	@Autowired
	UserRepository us;

	
	private Long idSinistre ;
	
	private typeSinister typeSinistre;
     
	private String description;
	 
	private Date dateOccurence;
	
	private sinisterstatus status;
	 
	private SinisterMotif motifStatus;
	private Contract tempC ;
	private ContractType m ; 
	private PieChartModel barchart ;
	private PieChartModel barchartt ;
	private PieChartModel barcharttt ;
	
	public void setBarcharttt(PieChartModel barcharttt) {
		this.barcharttt = barcharttt;
	}
	public void setBarchartt(PieChartModel barchart) {
		this.barchartt = barchartt;
	}
	public void setBarchart(PieChartModel barchart) {
		this.barchart = barchart;
	}
	public ContractType getM() {
		return m;
	}
	public void setM(ContractType m) {
		this.m = m.VieEntiere;
	}
	public Contract getTempC() {
		return tempC;
	}
	public void setTempC(Contract tempC) {
		this.tempC = tempC;
	}
	
	

	private User user;
	
	 private byte[] documents ;
	
	 
	 private List<sinister> sinistres;
	 private List<sinister> sinistresPerUser;
	 private List<sinister> FiltredSins;
	 
	 public List<sinister> getFiltredSins(String sins) {
		 FiltredSins = sinService.findByDescriptionn(sins);
		 return FiltredSins;
	}
	public void setFiltredSins(List<sinister> filtredSins) {
		FiltredSins = filtredSins;
	}
	public List<sinister> getSinistresPerUser() {
		 
		 sinistresPerUser = sinService.findbyuserid(5L);
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
		
		
		
		public String  addSinistreVE() throws IOException {
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
			FacesContext.getCurrentInstance().addMessage("hihi:awah", facesMessage);
			}
			if( description.isEmpty() ==true){
				FacesMessage facesMessage= new FacesMessage("Veuillez decrire votre Sinistre");
			FacesContext.getCurrentInstance().addMessage("hihi:des", facesMessage);
			}
			if( cs.findContrat2()==null){
				FacesMessage facesMessage= new FacesMessage("Vous avez beosin d'un contrat");
			FacesContext.getCurrentInstance().addMessage("hihi:lol", facesMessage);
			}
			else {

				sinService.addOrUpdateSinistre(new sinister(typeSinistre.VieEntiere, description, dateOccurence, sinisterstatus.enAttente,documents,us.getOne(5L)));
				navigateTo = "/affichagedetail?faces-redirect=true" ;
				}
			return navigateTo; 
		}
		public String  addSinistreCD() {
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
			FacesContext.getCurrentInstance().addMessage("hihi:awah", facesMessage);
			}
			if( description.isEmpty() ==true){
				FacesMessage facesMessage= new FacesMessage("Veuillez decrire votre Sinistre");
			FacesContext.getCurrentInstance().addMessage("hihi:des", facesMessage);
			}
			if( cs.findContrat1()==null){
				FacesMessage facesMessage= new FacesMessage("Vous avez beosin d'un contrat");
			FacesContext.getCurrentInstance().addMessage("hihi:lol", facesMessage);
			}
			
			else {
				
				sinService.addOrUpdateSinistre(new sinister(typeSinistre.casDeces, description, dateOccurence, sinisterstatus.enAttente, documents,us.getOne(5L)));
				navigateTo = "/affichagedetail?faces-redirect=true" ;
				}
			return navigateTo; 
		}
		public String  addSinistreCDP() {
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
			FacesContext.getCurrentInstance().addMessage("hihi:awah", facesMessage);
			}
			if( description.isEmpty() ==true){
				FacesMessage facesMessage= new FacesMessage("Veuillez decrire votre Sinistre");
			FacesContext.getCurrentInstance().addMessage("hihi:des", facesMessage);
			}
			if( cs.findContrat4(8L)==null){
				FacesMessage facesMessage= new FacesMessage("Vous avez beosin d'un contrat");
			FacesContext.getCurrentInstance().addMessage("hihi:lol", facesMessage);
			}
			else {
				sinService.addOrUpdateSinistre(new sinister(typeSinistre.casDecesperiodique, description, dateOccurence, sinisterstatus.enAttente, documents,us.getOne(8L)));
				navigateTo = "/affichagedetail?faces-redirect=true" ;
				}
			return navigateTo; 
		}
		public String  addSinistreTDE() {
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
			FacesContext.getCurrentInstance().addMessage("hihi:awah", facesMessage);
			}
			if( description.isEmpty() ==true){
				FacesMessage facesMessage= new FacesMessage("Veuillez decrire votre Sinistre");
			FacesContext.getCurrentInstance().addMessage("hihi:des", facesMessage);
			}
			if( cs.findContrat3()==null){
				FacesMessage facesMessage= new FacesMessage("Vous avez beosin d'un contrat");
			FacesContext.getCurrentInstance().addMessage("hihi:lol", facesMessage);
			}
			else {
				sinService.addOrUpdateSinistre(new sinister(typeSinistre.TemporairedecesEmprunteur, description, dateOccurence, sinisterstatus.enAttente, documents,us.getOne(5L)));
				navigateTo = "/affichagedetail?faces-redirect=true" ;
				}
			return navigateTo; 
		}
		
		
		public String removeSinistre(Long id)
		{
			sinService.deleteSinistre(id);
			return "/affichagedetail?faces-redirect=ture";
		}
		public String removeSinistre2(Long id)
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
		this.setMotifStatus(empl.getMotifStatus());
		this.setSinistreIdToBeUpdated(empl.getIdSinistre());
		return "/sinisterAdminDetails?faces-redirect=ture";
		}
		
		public String updateSinistre()
		{  
		  sinService.addOrUpdateSinistre(new sinister(sinistreIdToBeUpdated,typeSinistre, description, dateOccurence, sinisterstatus.enAttente, documents,us.getOne(5L)));
		return "/affichagedetail?faces-redirect=ture";
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
				    if(val1==0){
				    	FacesMessage facesMessage= new FacesMessage("Vous Devez "
						    	+ "Verifiez votre Contrat");
					FacesContext.getCurrentInstance().addMessage("hihiho:hahahi", facesMessage);
				    }
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
			   public void verif(ContractType s) {
				   
				  tempC = this.cs.retrieveContractsbytype2(s) ;
				   this.getTypeSinistre().equals(tempC.getType());
				
				
			   }
			   public String Simulator(Long ids )
				{

				    double k=0;
				    Long d = 0L;
				    d=sr.findcontractidbysisn(ids);
				    
					k=sinService.CreditSimulator(ids,d );
					FacesMessage facesMessage = new FacesMessage(" Le coût éstimé de l’assurance bancaire = "+ k);

					FacesContext.getCurrentInstance().addMessage("null:wawawa",facesMessage);
					
					return ""+ k + "" ;
				}
			   public int cal(){
				return  sinService.countVE() ;	}
			   public int cal2(){
					return  sinService.countCD() ;	}
			   public int caL3(){
					return   sinService.countTDE() ;	}
			   
			   
				public PieChartModel getBarchart() {
					barchart = new PieChartModel();
					List<Map<String, BigInteger>> jj = sinService.statistiqueSinisters();
					for ( int i =0 ; i<jj.size() ; i++){
						//Integer[] aKeys = jj.get(i).keySet().toArray(new Integer[jj.get(i).size()]);
						barchart.set(String.valueOf(jj.get(i).get("typeSinistre")),jj.get(i).get("nombre_sinisters"));
					}		
					barchart.setTitle("Sinister par Type");
					barchart.setLegendPosition("w");
					return barchart;
				}
				public PieChartModel getBarchartt() {
					barchartt = new PieChartModel();
					List<Map<String, BigInteger>> jj = sinService.statistiqueSinisters2();
					for ( int i =0 ; i<jj.size() ; i++){
						//Integer[] aKeys = jj.get(i).keySet().toArray(new Integer[jj.get(i).size()]);
						barchartt.set(String.valueOf(jj.get(i).get("status")),jj.get(i).get("nombre_sinisters"));
					}		
					barchartt.setTitle("Sinister par Status");
					barchartt.setLegendPosition("w");
					return barchartt;
				}
				public PieChartModel getBarcharttt() {
					barcharttt = new PieChartModel();
					List<Map<String, BigInteger>> jj = sinService.statistiqueSinisters3();
					for ( int i =0 ; i<jj.size() ; i++){
						//Integer[] aKeys = jj.get(i).keySet().toArray(new Integer[jj.get(i).size()]);
						barcharttt.set(String.valueOf(jj.get(i).get("name")),jj.get(i).get("nombre_sinisters"));
					}		
					barcharttt.setTitle("Sinister par UserName");
					barcharttt.setLegendPosition("w");
					return barcharttt;
				}
			
			
			
}