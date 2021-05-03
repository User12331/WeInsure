package tn.weinsure1.service;


import java.util.List;
import java.util.Map;
import java.util.Optional;

import tn.weinsure1.entities.Transaction;


public interface ITransactionService {

	Transaction addTransaction(Transaction t);

	void deleteTransaction(int id);

	List<Transaction> retrieveAllTransactions();

	Transaction retrieveTransactions(int id);

	List<Transaction> listAll();
	
	List<Transaction> listTransactionByUserId(Long id) ;

    List<Transaction> listTransactionByDate(int annee);

	Map<Double, Double> StatisticMonthbyAmount(int year);
	
}