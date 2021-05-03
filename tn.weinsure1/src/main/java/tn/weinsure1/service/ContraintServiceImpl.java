package tn.weinsure1.service;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tn.weinsure1.entities.Contraint;
import tn.weinsure1.entities.Contraint;
import tn.weinsure1.repository.ContraintRepository;
import tn.weinsure1.repository.ContraintRepository;
import tn.weinsure1.repository.UserRepository;

@Service
public class ContraintServiceImpl implements IContraintService {

	@Autowired
	ContraintRepository ContraintRepository;
	@Autowired
	UserRepository UserRepository;
	
	private static final Logger L= LogManager.getLogger(ContraintServiceImpl.class);
	@Override
	public List<Contraint> RetrieveAllContraints(){
		List<Contraint> users = (List<Contraint>) ContraintRepository.findAll();
		for (Contraint user : users){
			L.info("user +++ :" + user);
		}
		return users;
	}
	@Override
	public Contraint AddContraint(Contraint c) {
		Contraint cnt = ContraintRepository.save(c);
		return cnt;
	}
	@Override
	public void DeleteContraint(String id) {
		ContraintRepository.deleteById(Long.parseLong(id));
	}
	@Override
	public Contraint UpdateContraint(Contraint c) {
		Contraint ContraintUpdated = ContraintRepository.save(c);
		return ContraintUpdated;
	}
	
	@Override
	public Contraint RetrieveContraint(String id) {
		L.info("in RetrieveContraint id = " + id);
		Contraint c = ContraintRepository.findById(Long.parseLong(id)).get();
		L.info("Contraint returned = : " + c);
		return c;	
	}
	
	
	
}
