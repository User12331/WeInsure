package tn.weinsure1.service;

import java.text.ParseException; 
import java.util.List;

import javax.mail.MessagingException;

import org.springframework.data.repository.query.Param;
import org.springframework.web.multipart.MultipartFile;


import tn.weinsure1.entities.Contract;
import tn.weinsure1.entities.ContractType;
import tn.weinsure1.entities.User;
import tn.weinsure1.entities.sinister;
import tn.weinsure1.entities.sinisterstatus;
import tn.weinsure1.entities.typeSinister;

public interface IsinisterService {

	 List<sinister> retrieveAllSinistres(); 
	 sinister addSinistre(sinister s , Long id);
	 void deleteSinistre(Long id);
	 void deleteSinistreS(sinister s);
	 sinister updateSinistre(Long id);
	 sinister retrieveSinistre(String id);
	 List<sinister> findByStatus(sinisterstatus sins);
	 List<sinister> findByDescription(String name);
	 List<sinister> findByYear(String year) ;
	 List<sinister> findByAny(String any);
	 List<sinister> findSinisterByStatusRejected()  ;
	 List<sinister> findSinisterByStatusEnAttente()  ;
	 List<sinister> findByDescriptionn(String sins);
	 void CheckStatus() ; 
	 float CVE(Long idS ) ;
	 void SendMail() ;
	 List<sinister> findbyuserid(Long id);
	 int findcontractdurationBysinister(Long id , ContractType str);
	 float CapitalCasDéces(Long idS ) throws ParseException ;
	 float CapitalDécesPeriodique(Long idS  );
	 float TDEMPRUNTEUR(Long idS   ) throws ParseException ;
	 void affecterUserSinister(Long SinId , Long UserId) ; 
	 void affecterSinisterUser(Long SinId, Long userId);
	 List<sinister> findSinisterDescriptionwithUR( Long id);
	 float CreditSimulator( Long idS , Long idC) ;
	 void UpdateSinDescription(Long idS ,String description ) ;
	 String yallacurrent() ;
	 float GetSalaireMoyenUsersContractSinister();
	 float GetPrixContratContractSinister();
	 public sinister addSinisterv2(String description,typeSinister type, MultipartFile documents , String d) throws ParseException ;
	 public List<sinister> getAllSinistres();
 	public int addOrUpdateSinistre(sinister sin);
 	public	Long findcontractidbysisnVIEENTIERE( Long id2 );
 	public	Long findcontractidbysisnTDEMPREUNTEUR( Long id2 );
 	public	Long findcontractidbysisnCASDECES( Long id2 );
 	public	Long findcontractidbysisnCASDECESP( Long id2 );
 	public	Long findcontractidbysisn( Long id2 );
 	public int countVE();
 	public int countCD();
 	public int countTDE();
 	 
 
	 

}
