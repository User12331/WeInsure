package tn.weinsure1.service;
import java.io.File; 
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


import tn.weinsure1.annotation.CurrentUser;
import tn.weinsure1.entities.Contract;
import tn.weinsure1.entities.ContractType;
import tn.weinsure1.entities.ContraintType;
import tn.weinsure1.entities.SendEmailService;
import tn.weinsure1.entities.SinisterMotif;
import tn.weinsure1.entities.User;
import tn.weinsure1.entities.sinister;
import tn.weinsure1.entities.sinisterstatus;
import tn.weinsure1.entities.typeSinister;
import tn.weinsure1.repository.ContractRepository;
import tn.weinsure1.repository.TableMortalitéRepository;
import tn.weinsure1.repository.UserRepository;
import tn.weinsure1.repository.sinisterRepository;

@Service
public class sinisterServiceImpl implements IsinisterService {
	
	
	@Autowired
	private SendEmailService sendEmailService;
	
	File file = new File("C:\\Users\\Bourguiba\\Desktop\\11.jpg");

	@Autowired     
	sinisterRepository sinistreRepository ;
	@Autowired
	TableMortalitéRepository tr ;
	@Autowired
	UserRepository ur ;
	@Autowired
	ContractRepository cr ;
	private static final Logger L = LogManager.getLogger(sinisterServiceImpl.class);
	
	@Override
	public sinister addSinistre(sinister s , Long id ) {
		User u=ur.findById(id).get();
		s.setUser(u);
		s.setStatus(sinisterstatus.enAttente);
		//s.setDateOccurence(new Date());
		sinistreRepository.save(s);
		
		return s;
	
	}
	@Override
	public void deleteSinistre(Long id) {
		sinister s =sinistreRepository.findById(id).get();
		s.setUser(null);
		L.info("sinistre +++ :" + id);
		sinistreRepository.deleteById(id);	
	}
	@Override
	public void deleteSinistreS(sinister s) {
		s.setUser(null);
		L.info("sinistre +++ :" + s.getIdSinistre());
		sinistreRepository.deleteById(s.getIdSinistre());	
	}
	@Override
	public sinister updateSinistre(Long id) {
		sinister s =sinistreRepository.findById(id).get();
		sinister sinistreAdded = sinistreRepository.save(s);
		return sinistreAdded;
	}
	@Override
	public sinister retrieveSinistre(String id) {
		L.info("in retrieveSinistre id = " + id);
		sinister s = sinistreRepository.findById(Long.parseLong(id)).get();
		L.info("sinistre returned = : " + s);
		return s;
			}
	@Override
	public List<sinister> retrieveAllSinistres() {
		List<sinister> sinistres = (List<sinister>) sinistreRepository.findAll(); 
		for(sinister sin : sinistres)
		{
			L.info("sinistre +++ :" + sin);
		}
					
		return sinistres;
	}	

	@Override 
	public List<sinister> findByStatus(sinisterstatus sins) {
		return sinistreRepository.findSinisterByStatus(sins);
		
	}
	@Override
	public List<sinister> findByDescription(String name) {

		List<sinister> sins = sinistreRepository.findByDescription(name);
		L.info("sinister +++ :" + sins) ;
		return sins;
	} 
	@Override
	public List<sinister> findByYear(String year) {
		List<sinister> sins = sinistreRepository.findByYear(year);
		L.info("sinistre +++ :" + sins) ;
		return sins;
	}
	@Override
	public List<sinister> findByAny(String any) {
		List<sinister> sins = sinistreRepository.findByAny(any);
		L.info("sinistre +++ :" + sins) ;
		return sins;
	}
	@Override
	public List<sinister> findSinisterByStatusRejected( ) {
		List<sinister> sins = sinistreRepository.findSinisterByStatusRejected();
		L.info("sinistre +++ :" + sins) ;
		return sins;
	}
	@Override
	public List<sinister> findSinisterByStatusEnAttente( ) {
		List<sinister> sins = sinistreRepository.findSinisterByStatusEnAttente();
		L.info("sinistre +++ :" + sins) ;
		return sins;
	}
	@Override
	public void SendMail() {
		try {
		List<sinister> sinstreRej = sinistreRepository.findSinisterByStatusRejected();
		//SimpleMailMessage mail = new SimpleMailMessage();
		for(int i=0;i< sinstreRej.size();i++)
		{    String email = sinstreRej.get(i).getUser().getEmail();
			String nom = sinstreRej.get(i).getUser().getFirstName();
			String motif =sinstreRej.get(i).getMotifStatus().toString();
			String date =sinstreRej.get(i).getDateOccurence().toString();			
			sendEmailService.sendEmail("yahiabourguiba1997@gmail.com", "Sinistre rejeté" , "Cher cleint monsieur " + nom +", on vous informe que votre demande de remboursement"
					+ "de sinistre, envoyée à la date "+date+ " , a été rejetée car cette demande " + motif + ". Merci pour votre compréhension. ", file);	
		} 
		} 
		catch (Exception e)
		{System.out.println(e.getMessage());}
		}
	@Override
	public void CheckStatus() {
		List<sinister> sinsenattente = sinistreRepository.findSinisterByStatusEnAttente();
		Calendar currentdate = Calendar.getInstance(); 
		Date d = currentdate.getTime();  
		
		currentdate.add(Calendar.DAY_OF_MONTH,-5);
		Date d1= currentdate.getTime();
	
	
		sinisterstatus status=sinisterstatus.rejected;
		sinisterstatus status2=sinisterstatus.encours;
		sinisterstatus status3=sinisterstatus.WaitingForReclamation;

		for(int i=0;i<sinsenattente.size();i++)
		{ // L.info("date OCC:" + sinsenattente.get(i).getDateOccurence()) ;
		
		
			if (sinsenattente.get(i).getDateOccurence().compareTo(d1) < 0)
			{
				 L.info("BOUCLE 1:");
				 SinisterMotif motif=SinisterMotif.depasse5jours;
				 sinsenattente.get(i).setStatus(status);
				 sinsenattente.get(i).setMotifStatus(motif);
				 sinistreRepository.save(sinsenattente.get(i));
				 L.info("new sin +++ :" + sinsenattente.get(i)) ;
				 SendMail();

			}
			else if (sinsenattente.get(i).getDocuments() == null)
			{
				L.info("boucle 2") ;
				SinisterMotif motif=SinisterMotif.Pasdedocuments;
				sinsenattente.get(i).setStatus(status);
				sinsenattente.get(i).setMotifStatus(motif);
				 sinistreRepository.save(sinsenattente.get(i));
				 L.info("new sin 2 +++ :" + sinsenattente.get(i));
				 SendMail();
			}
			
			else if (sinsenattente.get(i).getUser().getContraint() != null &&  sinsenattente.get(i).getUser().getContraint().getType().equals(ContraintType.SinisterReclamation))
			{
				L.info("boucle 3") ;
				SinisterMotif motif=SinisterMotif.ReclamationSinistreEnCoursDeTraitement;
				sinsenattente.get(i).setStatus(status3);
				sinsenattente.get(i).setMotifStatus(motif);
				 sinistreRepository.save(sinsenattente.get(i));
				 L.info("new sin 2 +++ :" + sinsenattente.get(i));
				 SendMail();
			} 
			else {
				SinisterMotif motif=SinisterMotif.Réglé;
				sinsenattente.get(i).setStatus(status2);
				sinsenattente.get(i).setMotifStatus(motif);
				sinistreRepository.save(sinsenattente.get(i));
			}

		} 

	}	
	@Override
	public float CVE(Long idS){
		int k;
		float prime = 0 ; 
		float cd = 0  ;
		Long m = 0L ;
		//CurrentUser.getId();
		sinister ss = sinistreRepository.findById(idS).get();
		m = this.findcontractidbysisnVIEENTIERE(idS);
		L.info("ID CONTRAT"+m ) ;
		User u =ss.getUser();
		Contract c = cr.findById((long) m).get();
		int AgeMax = tr.findAgeMax();
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(u.getBirthdate());
		int BDay = calendar.get(Calendar.YEAR);
		LocalDate now = LocalDate.now();
		int years = now.getYear()-BDay;
		double taux = c.getRate();
/*
		if (ss.getUser().getContracts() == null)
		{
			L.info("Vous Avez Un probleme de Contract Veuillez Nous joindre chez la plus proche agence" ) ;
			return 0;
			
		}
		 if (ss.getTypeSinistre().toString().equals(c.getType().toString()) == false)
		{
			L.info("Vous Avez Un probleme de Contract Veuillez Nous joindre chez la plus proche agence" ) ;
			return 0;
			
		}
		if (ss.getStatus().equals(sinisterstatus.rejected) )
		{
			L.info("Vous devez regler votre situation avec la plus proche agence" ) ;
			return 0 ;
		}
		 if ( ss.getStatus().equals(sinisterstatus.enAttente))
		{
			L.info("Veuillez patienter , Un de nos agent est entrain de regler votre situation" ) ;
			return 0 ;
		}
		 if (ss.getTypeSinistre().compareTo(typeSinister.VieEntiere) != 0)
		{
			L.info("Veuillez verifier votre Type de Sinistre" ) ;
			return 0 ;
		}
		*/
		for (k =0; k < AgeMax - years; k++) {
			float dxk= tr.findByDecesDx(years+k); 	
			L.info("DX " + dxk) ;
			float lx = tr.findBySurvivantsLx(years);
			//double v = Math.pow( (1/(1+taux)) ,  (k + (1/2))  );			 
			prime += (float) (Math.pow( (1/(1+taux)) ,  (k + (1/2))  )) * ( dxk / lx) ;	
			L.info("PRIMEeee+++++++++ =" + prime) ;
	}
		L.info("PRIME11eee+++++++++ =" + prime) ;
		cd= c.getPrice() / prime ; 
		ss.setReglemntation(cd);
		ss.setMotifStatus(SinisterMotif.Réglé);
		ss.setStatus(sinisterstatus.valide);
		sinistreRepository.save(ss);
		L.info("reg " + ss.getReglemntation()) ;
		L.info("PRIME+++++++++ =" + cd) ;
		L.info("prix cotnract+++++++++ =" + c.getPrice()) ;
		return cd;
	}
	public List<sinister> findbyuserid(Long id) {
		List<sinister> sins = sinistreRepository.findbyuserid(id);
		L.info("sinister +++ :" + sins) ;
		return sins;
	}
	public int findcontractdurationBysinister(Long id , ContractType str) {
		int k = sinistreRepository.findcontractdurationBysinister(id , str);
		L.info("sinister +++ :" + k) ;
		return k;
	}
	@Override
	public float CapitalCasDéces(Long idS  ) throws ParseException {
		int k;
		float prime = 0 , dxk = 0 , lx = 0 ; 
		Long m = 0L;
		sinister ss = sinistreRepository.findById(idS).get();
		User u =ss.getUser();
		m = sinistreRepository.findcontractidbysisnCASDECES(idS);
		Contract c = cr.findById(m).get();
		double taux = c.getRate();
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(u.getBirthdate());
		int BDay = calendar.get(Calendar.YEAR);
		LocalDate now = LocalDate.now();
		int years = now.getYear()-BDay;
		L.info("PRIME+++++++++ =" +years) ;	
		float cd = 0; 
		int s = 0 ;  
		
		if (ss.getTypeSinistre().toString().equals(c.getType().toString()))
		{
			 s = findcontractdurationBysinister(m,c.getType()) ; 
		}
		/*
		 if (ss.getTypeSinistre().toString().equals(c.getType().toString()) == false)
		{
			L.info("Vous Avez Un probleme de Contract Veuillez Nous joindre chez la plus proche agence" ) ;
			return 0;
			
		}
		if ( ss.getStatus().equals(sinisterstatus.rejected) )
		{
			L.info("Vous devez regler votre situation avec la plus proche agence" ) ;
			return 0 ;
		}
		 if ( ss.getStatus().equals(sinisterstatus.enAttente))
		{
			L.info("Veuillez patienter , Un de nos agent est entrain de regler votre situation" ) ;
			return 0 ;
		}
		 if (ss.getTypeSinistre().compareTo(typeSinister.casDeces) != 0 && c.getType().compareTo(ContractType.Décès) != 0)
		{
			L.info("Veuillez verifier votre Type de Sinistre" ) ;
			return 0 ;
		}*/
		for (k =0; k < s-1; k++) {
			L.info("DX " + dxk) ;
			 lx = tr.findBySurvivantsLx(years);
			prime += Math.pow( 1/ (1+taux) ,  k + (1/2)  ) * ( tr.findByDecesDx(years+k) / lx) ;
			L.info("PRIME+++++++++ =" +prime) ;				 
			}
		    cd = c.getPrice() / prime ; 
		    ss.setReglemntation(cd);
			ss.setStatus(sinisterstatus.valide);
			sinistreRepository.save(ss);
			L.info("reg " + ss.getReglemntation()) ;
		L.info("PRIME+++++++++ =" + cd) ;
		return cd;
	}
	@Override
	public float CapitalDécesPeriodique(Long idS   ) {
		int k1,k2;
		float prime1 = 0 , prime = 0; 
		Long m = 0L ;
		float cd = 0 ;
		int s = 0;
		sinister ss = sinistreRepository.findById(idS).get();
		m = this.findcontractidbysisnCASDECESP(idS);
		User u =ss.getUser();
		Contract c = cr.findById(m).get();
		double taux = c.getRate();
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(u.getBirthdate());
		int BDay = calendar.get(Calendar.YEAR);
		LocalDate now = LocalDate.now();
		int years = now.getYear()-BDay;
		if (ss.getTypeSinistre().toString().equals(c.getType().toString()))
		{
			 s = findcontractdurationBysinister(m,c.getType()) ; 
		}
		
		/*
		 if (ss.getTypeSinistre().toString().equals(c.getType().toString()) == false)
		{
			L.info("Vous Avez Un probleme de Contract Veuillez Nous joindre chez la plus proche agence" ) ;
			return 0;
			
		}
		 if ( ss.getStatus().equals(sinisterstatus.rejected) )
		{
			L.info("Vous devez regler votre situation avec la plus proche agence" ) ;
			return 0 ;
		}
		 if ( ss.getStatus().equals(sinisterstatus.enAttente))
		{
			L.info("Veuillez patienter , Un de nos agent est entrain de regler votre situation" ) ;
			return 0 ;
		}
		 if (ss.getTypeSinistre().toString().equals(typeSinister.casDecesperiodique.toString()) == false)
		{
			L.info("Veuillez verifier votre Type de Sinistre" ) ;
			return 0 ;
		}*/
		for (k1 =0; k1 < (s-1)*12; k1++) {
			float lxk= tr.findBySurvivantsLx((years+k1)/12); 	
			L.info("DX1 " + lxk) ;
			float lx = tr.findBySurvivantsLx(years);
			  //p = c.getPrice() * (( ( Math.pow((1 + taux) , (years + 1)) - 1 ) / years ) ) ; 
				L.info("DX " + lx) ;
			prime1  += (float) (c.getPrice() * (( ( Math.pow((1 + taux) , (years + 1)) - 1 ) / years ) ) * Math.pow( 1/ (1+taux) ,  k1/12   )) *  ( lxk / lx) ;	
	}

		for (k2 =0; k2 < s-1; k2++) {
			float lxk= tr.findByDecesDx(years+k2); 	
			L.info("DX2 " + lxk) ;
			float lx = tr.findBySurvivantsLx(years);
			 //double v = Math.pow( 1/ (1+taux) ,  k2 +(1/2)  );	
			// double p = c.getPrice() * (( ( Math.pow((1 + taux) , (years + 1)) - 1 ) / years ) - 1) ; 
			prime += (float) (Math.pow( 1/ (1+taux) ,  k2 +(1/2)  )) *  ( lxk / lx) ;	
			
	}
		L.info("ayayayayay " + prime) ;
		L.info("ayayayayay " + prime1) ;
		cd = prime1 / prime ;
		L.info("DX3 " + ss.getUser().getId()) ;
		ss.setReglemntation(cd);
		ss.setMotifStatus(SinisterMotif.Réglé);
		ss.setStatus(sinisterstatus.valide);
		sinistreRepository.save(ss);
		L.info("reg " + ss.getReglemntation()) ;

		return cd;
	}   
    public float TDEMPRUNTEUR(Long idS   ) {
		int k;
		float prime = 0;
		int s = 0;
		Long m = 0L ;
		sinister ss = sinistreRepository.findById(idS).get();
		m = sinistreRepository.findcontractidbysisnTDEMPREUNTEUR(idS);
		User u =ss.getUser();
		Contract c = cr.findById(m).get();
		double taux = c.getRate();
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(u.getBirthdate());
		int BDay = calendar.get(Calendar.YEAR);
		LocalDate now = LocalDate.now();
		int years = now.getYear()-BDay;
		int o = findcontractdurationBysinister(m,c.getType()) ; 
		L.info("OOOOO"+o ) ;
		float crd = 0 , crd1 = 0 , tde = 0;
		L.info("TYPESINISTRE"+ss.getTypeSinistre().toString() ) ;
		L.info("TYPECONTRAT"+c.getType().toString()) ;
		if (ss.getTypeSinistre().toString().equals(c.getType().toString()))
		{
			 s = findcontractdurationBysinister(m,c.getType()) ; 
			 L.info("SSSS"+s ) ;
			
		}
		/*
		 if ( ss.getStatus().equals(sinisterstatus.rejected) )
		{
			L.info("Vous devez regler votre situation avec la plus proche agence" ) ;
			return 0 ;
		}
		 if ( ss.getStatus().equals(sinisterstatus.enAttente))
		{
			L.info("Veuillez patienter , Un de nos agent est entrain de regler votre situation" ) ;
			return 0 ;
		}
		 if (ss.getTypeSinistre().compareTo(typeSinister.TemporairedecesEmprunteur) != 0)
		{
			L.info("Veuillez verifier votre Type de Sinistre" ) ;
			return 0 ;
		}
		*/
		for (k =0; k < ((s-1)*12) ; k++) {
			 //double v = Math.pow((1+(taux/12)) ,  s*12 );
			//double l = Math.pow( 1/ (1+(taux/12)) ,  s*12 - 1 );
			//prime = ((float) (Math.pow((1+(taux/12)) ,  s*12 )) * c.getPrice()) - (float)(Math.pow((1+(taux/12)) ,  s*12 )) ;	
			//crd = (float) (((float) (Math.pow((1+(taux/12)) ,  s*12 )) * c.getPrice()) - (float)(Math.pow((1+(taux/12)) ,  s*12 )) / Math.pow( 1/ (1+(taux/12)) ,  s*12 - 1 )) ; 
			//crd1 = (float) ((float) (((float) (Math.pow((1+(taux/12)) ,  s*12 )) * c.getPrice()) - (float)(Math.pow((1+(taux/12)) ,  s*12 )) / Math.pow( 1/ (1+(taux/12)) ,  s*12 - 1 )) / Math.pow( (1+taux) , k )) ; 
			L.info("crd1+++++++++ =" + crd1) ;
			//float lxk= tr.findProbaByAgeClient(years+k); 
			//L.info("lxk+++++++++ =" + lxk) ;
			tde += (((float) ((float) (((float) (Math.pow((1+(taux/12)) ,  s*12 )) * c.getPrice()) - (float)(Math.pow((1+(taux/12)) ,  s*12 )) / Math.pow( 1/ (1+(taux/12)) ,  s*12 - 1 )) / Math.pow( (1+taux) , k )))* tr.findProbaByAgeClient(years+k)) ; 
									      }
		tde = tde /12 ; 
		ss.setReglemntation(tde);
		ss.setStatus(sinisterstatus.valide);
		sinistreRepository.save(ss);
		L.info("reg " + ss.getReglemntation()) ;
		L.info("PRIME+++++++++ =" + tde) ;
		return tde;
		
		
	}
	public void affecterUserSinister(Long SinId , Long UserId){
		User u  = ur.findById(UserId).get();
	    sinister s =  sinistreRepository.findById(SinId).get();
	    s.setUser(u);
	    sinistreRepository.save(s);	
	}
	@Transactional	
	public void affecterSinisterUser(Long SinId, Long userId) {
		User u = ur.findById(userId).get();
		sinister s = sinistreRepository.findById(SinId).get();
		if(s.getUser() == null){
			List<User> urs = new ArrayList<>();
			urs.add(u);
			s.setUser(u);
		}else{
			s.setUser(u);
		}
	}
	public List<sinister> findSinisterDescriptionwithUR( Long id)
	{ 
		List<sinister> sins = sinistreRepository.findSinisterDescriptionwithUR(id);
		L.info("sinistre +++ :" + sins) ;
		return sins;
	}
	public float CreditSimulator( Long idS, Long idc) {	 
		sinister s = sinistreRepository.findById(idS).get() ; 
		User u= s.getUser();
		double taux =0;
		Contract  c =cr.findById(idc).get();
		//Long id=(Long)session.getAttribute("name");	 
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(u.getBirthdate());
		int BDay = calendar.get(Calendar.YEAR);
		float ss = 0 ;
		LocalDate now = LocalDate.now();
		int years = now.getYear()-BDay;
		if (years < 35 ) // taux 0.00276
		{ taux =0.00276;
		 ss= (float) Math.pow((1/1+taux), -years);
		}
		else  // tauxt 0.00042
			{taux=0.00042;
		 ss= (float) Math.pow((1/1+taux), -years);
			}
		double res = (this.TDEMPRUNTEUR(idS) * ss ) / 12  ; 
		L.info("sinister +++ :" + res) ;
		float res1 = (float) (this.TDEMPRUNTEUR(idS) + res) ;
		L.info("wiww +++ :" + res1) ;
		L.info("WAWAWA +++ :" + u.getSalary()) ;
		String aa = Float.toString(res1) ; 
		if(u.getSalary().toString().compareTo(aa) > 0 )
		{
			L.info("Vous n'etes pas éligible pour ce type d'emprunt veuillez verifer avec notre administration pour voir nos offres" ) ;
			return 0 ; 
		}
		else {
		return res1 ;
		}
	}
	public void UpdateSinDescription( Long idS , String description) {
		sinister s = sinistreRepository.findById(idS).get();
		s.setDescription(description);
		sinistreRepository.save(s);

	}
	public String yallacurrent(){
	Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

	if (principal instanceof UserDetails) {
	  String username = ((UserDetails)principal).getUsername();
	} else {
	  String username = principal.toString();
	  return username ; 
	}
	
	return ((UserDetails)principal).getUsername() ; 
}
	public float GetSalaireMoyenUsersContractSinister(){
		float k = 0 ; 
		k = sinistreRepository.GetSalaireMoyenUsersContractSinister();
		return k ;
	}
	public float GetPrixContratContractSinister() {
		float k = 0 ;
		k = sinistreRepository.GetPrixContratContractSinister();
		return k ;
	}
	
	public sinister addSinisterv2( String description, typeSinister type, MultipartFile documents , String d) throws ParseException {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		sinister c1 = new sinister();
		try {
			c1.setDocuments(documents.getBytes());
			c1.setStatus(sinisterstatus.enAttente);
			c1.setDescription(description);
			c1.setTypeSinistre(type);
			Date date1=new SimpleDateFormat("yyyy-MM-dd").parse(d);  
			c1.setDateOccurence(date1);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		sinister contResponse = this.addSinistre(c1 , ((User)principal).getId());
		 
		 String fileDownloadUri1 = ServletUriComponentsBuilder.fromCurrentContextPath().path("/Media/")
				 .path(String.valueOf(contResponse.getIdSinistre())).path("/image").toUriString();

			return c1;
	}
	@Override
	public List<sinister> getAllSinistres(){
		return (List<sinister>) sinistreRepository.findSinisterWithContracts();
	}
	@Override
	public int addOrUpdateSinistre(sinister sin) {
		sinistreRepository.save(sin);
	return sin.getIdSinistre().intValue();
	}
	public Long findcontractidbysisnVIEENTIERE(Long id2 ) {

	Long k =0L;
	k = sinistreRepository.findcontractidbysisnVIEENTIERE(id2);
	return k ;
		
	}
	public Long findcontractidbysisnTDEMPREUNTEUR(Long id2 ) {

		Long k =0L;
		k = sinistreRepository.findcontractidbysisnTDEMPREUNTEUR(id2);
		return k ;
			
		}
	public Long findcontractidbysisnCASDECES(Long id2 ) {

		Long k =0L;
		k = sinistreRepository.findcontractidbysisnCASDECES(id2);
		return k ;
			
		}
	public Long findcontractidbysisnCASDECESP(Long id2 ) {

		Long k =0L;
		k = sinistreRepository.findcontractidbysisnCASDECESP(id2);
		return k ;
			
		}
	
	
	
	
	
	
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	


