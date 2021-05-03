package tn.weinsure1.restController;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;    
import java.util.Optional;

import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import tn.weinsure1.entities.ContractType;
import tn.weinsure1.entities.User;
import tn.weinsure1.entities.sinister;
import tn.weinsure1.entities.sinisterstatus;
import tn.weinsure1.entities.typeSinister;
import tn.weinsure1.repository.sinisterRepository;
import tn.weinsure1.service.ITableMortaliteService;
import tn.weinsure1.service.IUserService;
import tn.weinsure1.service.IsinisterService;
import tn.weinsure1.service.TableMortaliteService;
@RestController
public class sinisterrestController {
	      @Autowired
	      IsinisterService sr;
	      @Autowired
		  ITableMortaliteService ts;
	      @Autowired     
	  	sinisterRepository sinistreRepository ;
	      @Autowired
		  IUserService us;
	      private static final Logger L= LogManager.getLogger(TableMortaliteService.class);
	   //  @PreAuthorize("hasRole('ROLE_ADMIN')")
		 @GetMapping("/retrieve-All-Sinistres")
		 @ResponseBody
		  public List<sinister> getSinister() { 
		 List<sinister> s = sr.retrieveAllSinistres();
		 return s;
		 } 
		//  @PreAuthorize("hasRole('ROLE_USER')")
		  @GetMapping("/retrieve-SinsitresByIdUser")
			 @ResponseBody
		  public List<sinister> SinByIdUser() { 
				  Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			 List<sinister> s = sr.findbyuserid(((User)principal).getId());
			// System.out.println(""+ ((User)principal).getContraint().getIdcontraint());
			 return s;
			 } 
		  @GetMapping("/findByAny/{any}")
			 @ResponseBody
		  public List<sinister> findByANy(@PathVariable(value = "any") String any) {
				  
			 List<sinister> s = sr.findByAny(any);
			 return s;
			 }  
		  @GetMapping("/retrieveSinWithReclamation/{id}")
			 @ResponseBody
		  public List<sinister> getSinistersRecla(@PathVariable(value = "id") Long id) {
				  
			 List<sinister> s = sr.findSinisterDescriptionwithUR(id);
			 return s;
			 } 
			  @GetMapping("/userrr")
				 @ResponseBody
		  public String getetet() {
				 String s = sr.yallacurrent();
				 return s;
				 } 
		 @GetMapping("/getSinisterBystatus/{id}")
		 @ResponseBody
		  public List<sinister> getsinisterbystat(@PathVariable(value = "id") Long id) {
		 List<sinister> s = sr.findbyuserid(id);
		 return s;
		 } 
		 @GetMapping("/getSinisterBystatuss/{id}/{str}")
		 @ResponseBody
		  public int getduration(@PathVariable(value = "id") Long id , @PathVariable(value = "str") ContractType str) {
		 int s = sr.findcontractdurationBysinister(id ,str);
		 return s;
		 } 
		  @PostMapping("/add-sinistre")
		  @ResponseBody
		  public sinister addSinister(@RequestBody sinister s) {
			  Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			  s.setStatus(sinisterstatus.enAttente);			
		  return sr.addSinistre(s,((User)principal).getId());
		  
		  }
		  @GetMapping("/calculCVE/{idS}")
		  @ResponseBody
		  public float calculCVE(@PathVariable("idS") Long idS){
				float k = 0 ;
				k = (float) sr.CVE(idS);
				return k ;
			  
			}
		@GetMapping("/calculCD/{idS}")
		@ResponseBody
		  public float CapitalCasDéces(@PathVariable("idS") Long idS) throws ParseException{
		float k = 0 ;
		k = (float) sr.CapitalCasDéces(idS) ; 
		return k ;
	  
	}
		@GetMapping("/calculCDP/{idS}")
		@ResponseBody
		  public float CapitalDécesPeriodique(@PathVariable("idS") Long idS) throws ParseException{
		float k = 0 ;
		k = (float) sr.CapitalDécesPeriodique(idS) ; 
		return k ;
	  
	}
		@GetMapping("/calculTDE/{idS}")
		@ResponseBody
		  public float TDEMPRUNTEUR(@PathVariable("idS") Long idS) throws ParseException{
		float k = 0 ;
		k = (float) sr.TDEMPRUNTEUR(idS) ; 
		return k ;
	  
	}
		  @PutMapping(value = "/aff-sinistre/{idSin}/{idUser}") 
		  public void affecterSinistreUser(@PathVariable("idSin")Long idSin, @PathVariable("idUser")Long idUser) {
		  sr.affecterSinisterUser(idSin, idUser);
		  }
		  @GetMapping("/creditsimul/{idS}/{idC}")
		  @ResponseBody
		  public float creditsimul(@PathVariable("idS") Long idS, @PathVariable("idC") Long idC){
				float k = 0 ;
				k = (float) sr.CreditSimulator( idS, idC) ; 
				return k ;
			  
			}
		     @PutMapping("/CheckStatus")
		  public void checkStatus() {
			 sr.CheckStatus();
			 } 
		     @PutMapping("/sendMail")
		  public void sendMail() {
			 sr.SendMail();
			 }
		    @PutMapping(value = "/modifyDescription/{idS}/{newdescription}") 
		 	@ResponseBody
	 	  public void mettreAjourDescriptionBysinIdId(@PathVariable("idS") Long idS, @PathVariable("newdescription") String disc) {
		 		sr.UpdateSinDescription(idS, disc);
		 		
		 	}
			@GetMapping("/AVG")
			@ResponseBody
		  public float AVG()  {

			float k = 0 ;
			k = (float) sr.GetSalaireMoyenUsersContractSinister() ; 
			return k ;
		  
		}
			@DeleteMapping("/DeleteSinistre/{id}")
			 @ResponseBody
		  public void delSinistre(  @PathVariable(value = "id") Long id) {
				sinister ss = sinistreRepository.findById(id).get() ;
				ss.setUser(null);
			    sr.deleteSinistre(id);
			 } 
				
			@PostMapping("/addv2")
			public sinister addSinisterv2( @RequestParam("description") String description, @RequestParam("typeSinistre") typeSinister type,@RequestParam("documents") MultipartFile documents ,@RequestParam("dateOccurence") String d) throws ParseException {
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
				sinister contResponse = sr.addSinistre(c1 , ((User)principal).getId());
				 
				 String fileDownloadUri1 = ServletUriComponentsBuilder.fromCurrentContextPath().path("/Media/")
						 .path(String.valueOf(contResponse.getIdSinistre())).path("/image").toUriString();

					return c1;
			}


}

