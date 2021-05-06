package tn.weinsure1.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tn.weinsure1.entities.Transaction;
import tn.weinsure1.repository.TransactionRepository;


@Transactional
@Service

public class TransactionServiceImpl implements ITransactionService {

	@Autowired
	TransactionRepository transactionRepository;

	@Override
	public Transaction addTransaction(Transaction t) {
		Transaction transaction = transactionRepository.save(t);
		return transaction;
	}

	@Override
	public void deleteTransaction(int id) {
		transactionRepository.deleteById(id);
	}

	@Override
	public List<Transaction> retrieveAllTransactions() {
		List<Transaction> transaction = (List<Transaction>) transactionRepository.findAll();
		return transaction;
	}

	@Override
	public Transaction retrieveTransactions(int id) {

		Optional<Transaction> TransOptional = transactionRepository.findById(id);
		Transaction t = TransOptional.get();
		return t;

	}
	
	
	

	@Override
	public List<Transaction> listAll() {

		return transactionRepository.findAll(Sort.by("transactionDate").ascending());

	}

	@SuppressWarnings("null")
	@Override
	public List<Transaction> listTransactionByUserId(Long userid) {

		List<Transaction> L = transactionRepository.findAll(Sort.by("transactionDate").ascending());
		List<Transaction> ListUser = new ArrayList<Transaction>();
		for (int i = 0; i < L.size(); i++) {
			if (L.get(i).getTransactionprice().getUser().getId() == userid) {
				ListUser.add(L.get(i));
				// System.out.print(L.get(i).toString());
			}
		}
		return ListUser;
	}

	@SuppressWarnings({ "null", "deprecation" })
	@Override
	public List<Transaction> listTransactionByDate(int annee) {

		List<Transaction> L = transactionRepository.findAll(Sort.by("transactionDate").ascending());
		List<Transaction> ListY = new ArrayList<Transaction>();
		for (int i = 0; i < L.size(); i++) {
			System.out.print("YEAR "
					+ Integer.parseInt(new SimpleDateFormat("yyyy").format(L.get(i).getTransactionDate())) + "\n");

			if (Integer.parseInt(new SimpleDateFormat("yyyy").format(L.get(i).getTransactionDate())) == annee) {

				String dateToStr = String.format("%1$tY-%1$tm-%1$td", L.get(i).getTransactionDate());

				ListY.add(L.get(i));

				System.out.print("Ajout year  +++++ "
						+ Integer.parseInt(new SimpleDateFormat("yyyy").format(L.get(i).getTransactionDate())));

			}
		}
		return ListY;
	}

	@Override
	public Map<Double, Double> StatisticMonthbyAmount(int year) {
		Map<Double, Double> MA = new HashMap<Double, Double>();
		double sum1 = 0;
		double sum2 = 0;
		double sum3 = 0;
		double sum4 = 0;
		double sum5 = 0;
		double sum6 = 0;
		double sum7 = 0;
		double sum8 = 0;
		double sum9 = 0;
		double sum10 = 0;
		double sum11 = 0;
		double sum12 = 0;
		List<Transaction> L = listTransactionByDate(year);
		for (int i = 0; i < L.size(); i++) {
			String dateToStr = String.format("%1$tY-%1$tm-%1$td", L.get(i).getTransactionDate());
			String[] dateParts = dateToStr.split("-");
			String month = dateParts[1];
			if (month.equals("01"))

			{
				System.out.print(L.get(i).getTransactionAmount());
				sum1 += L.get(i).getTransactionAmount();
				System.out.print(L.get(i).getTransactionAmount());
				MA.put((double) 1, sum1);
			}

			else if (month.equals("02")) {
				sum2 += L.get(i).getTransactionAmount();
				MA.put((double) 2, sum2);
			}

			else if (month.equals("03")) {
				sum3 += L.get(i).getTransactionAmount();
				MA.put((double) 3, sum3);
			}

			else if (month.equals("04")) {
				sum4 += L.get(i).getTransactionAmount();
				MA.put((double) 4, sum4);
			}

			else if (month.equals("05")) {
				sum5 += L.get(i).getTransactionAmount();
				MA.put((double) 5, sum5);
			}

			else if (month.equals("06")) {
				sum6 += L.get(i).getTransactionAmount();
				MA.put((double) 6, sum6);
			}

			else if (month.equals("07")) {
				sum7 += L.get(i).getTransactionAmount();
				MA.put((double) 7, sum7);
			}

			else if (month.equals("08")) {
				sum8 += L.get(i).getTransactionAmount();
				MA.put((double) 8, sum8);
			}

			else if (month.equals("09")) {
				sum9 += L.get(i).getTransactionAmount();
				MA.put((double) 9, sum9);
			}

			else if (month.equals("10")) {
				sum10 += L.get(i).getTransactionAmount();
				MA.put((double) 10, sum10);
			}

			else if (month.equals("11")) {
				sum11 += L.get(i).getTransactionAmount();
				MA.put((double) 11, sum11);
			}

			else if (month.equals("12")) {
				sum12 += L.get(i).getTransactionAmount();
				MA.put((double) 12, sum12);
			}

		}

		return MA;
	}
	@Override
	public int addOrUpdateTransaction(Transaction t) {
		transactionRepository.save(t);
	return t.getTransactionid();
	}

	

	@Override
	public List<Transaction> getAllTransById(){
		return (List<Transaction>) transactionRepository.findAllByIdTran();
	}


}