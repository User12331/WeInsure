package tn.weinsure1.entities;

import java.io.File;
import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="Contraint")
public class Contraint implements Serializable {
	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY )
	@Column(name="IDContraint")
	private Long idcontraint;
	
	
	@Column(name="State")
	private int state;

	@Column(name="Description")
	private String description;
	
	@Column(name="Documents")
	private File documents;
	
	@Column(name="DateContraint")
	@Temporal(TemporalType.DATE)
	private Date datecontraint;
	
	@Column(name="Type")
	@Enumerated(EnumType.STRING)
	ContraintType type;

	@JsonIgnore
	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="IDNotice")
	private Notice notice ;
	
	
	@JsonIgnore
	@OneToOne(mappedBy="contraint" , fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private User user;
	
	public Contraint() {
		super();
	}

	public Contraint(int state, String description, File documents, Date datecontraint, ContraintType type,
			Notice notice, User user) {
		super();
		this.state = state;
		this.description = description;
		this.documents = documents;
		this.datecontraint = datecontraint;
		this.type = type;
		this.notice = notice;
		this.user = user;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Notice getNotice() {
		return notice;
	}

	public void setNotice(Notice notice) {
		this.notice = notice;
	}

	public Long getIdcontraint() {
		return idcontraint;
	}

	public void setIdcontraint(Long idcontraint) {
		this.idcontraint = idcontraint;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public File getDocuments() {
		return documents;
	}

	public void setDocuments(File documents) {
		this.documents = documents;
	}

	public Date getDatecontraint() {
		return datecontraint;
	}

	public void setDatecontraint(Date datecontraint) {
		this.datecontraint = datecontraint;
	}

	public ContraintType getType() {
		return type;
	}

	public void setType(ContraintType type) {
		this.type = type;
	}

	public Contraint(int state, String description, File documents, Date datecontraint, ContraintType type) {
		super();
		this.state = state;
		this.description = description;
		this.documents = documents;
		this.datecontraint = datecontraint;
		this.type = type;
	}
	
	
	
	
	

}
