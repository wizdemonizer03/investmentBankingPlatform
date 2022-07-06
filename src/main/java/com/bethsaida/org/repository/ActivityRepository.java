package com.bethsaida.org.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bethsaida.org.models.Activity;
import com.bethsaida.org.models.User;

@Repository
public interface ActivityRepository extends JpaRepository<Activity,Long> {
	
	Activity findFirstBy();
    Activity findFirstByUserOrderByIdDesc(User user);
    Page<Activity> findByUser(User user, Pageable pageable);
    
}
