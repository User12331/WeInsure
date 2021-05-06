package tn.weinsure1.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import tn.weinsure1.entities.Transaction;


@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

	@Query("select c from Transaction c where c.transactionprice = '3L'  ")
    List<Transaction> findAllByIdTran();

}