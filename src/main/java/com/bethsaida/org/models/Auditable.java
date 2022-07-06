package com.bethsaida.org.models;

import static javax.persistence.TemporalType.TIMESTAMP;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


@MappedSuperclass
//@EntityListeners(AuditingEntityListener.class)
public abstract class Auditable{
     
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@CreatedDate
	@Temporal(TIMESTAMP)
	protected Date createdDate;
	
    @LastModifiedDate
	@Temporal(TIMESTAMP)
	protected Date lastModifiedDate;

    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
	public Date getCreatedDate() {
		return createdDate;
	}

	@PrePersist
    public void setCreated() {
        this.createdDate = new Date();
    }
	
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getLastModifiedDate() {
        if (lastModifiedDate == null)
            return createdDate;
        return lastModifiedDate;
    }
	
    @PreUpdate
    public void setLastUpdated() {
        this.lastModifiedDate = new Date();
    }
	
    public Date getTime() {
        if (this.lastModifiedDate != null)
            return this.lastModifiedDate;
        return this.createdDate;
    }
	 
    public String getReadableDayMonth(Date date) {
        if (date == null) return "";
        return new SimpleDateFormat("dd MMMM").format(date);
    }
    
    public String getReadableDateWithoutTime(Date date) {
        if (date == null) return "";
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM, dd yyyy");
        return sdf.format(date);
    }

	@Override
	public String toString() {
		return "Auditable [id=" + id + ", createdDate=" + createdDate + ", "
				+ "lastModifiedDate=" + lastModifiedDate + "]";
	}
    
    
    
    
	
}
