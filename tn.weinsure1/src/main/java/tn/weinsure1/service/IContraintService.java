package tn.weinsure1.service;

import java.util.List; 

import tn.weinsure1.entities.Contraint;

public interface IContraintService {
	List<Contraint> RetrieveAllContraints(); 
	Contraint AddContraint(Contraint c);
	void DeleteContraint(String id);
	Contraint UpdateContraint(Contraint c);
	Contraint RetrieveContraint(String id);
}
