package com.bethsaida.org.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.bethsaida.org.models.Activity;
import com.bethsaida.org.models.User;
import com.bethsaida.org.repository.ActivityRepository;

@Service
public class ActivityService {
    
	private final ActivityRepository activityRepo;
	
	@Autowired
    public ActivityService(ActivityRepository activityRepo) {
    this.activityRepo = activityRepo;
    }
	
	public Activity save(Activity activity) {
    if (activity.getId()   == null) { // new activity (user logged in)
    Activity firstActivity = this.findFirst();
    if (firstActivity != null) {
    long total = firstActivity.getTotalVisitors();
    activity.setTotalVisitors(++total);
    firstActivity.setTotalVisitors(total);
    this.activityRepo.save(firstActivity);
            }
        }
        return this.activityRepo.save(activity);
    }
	
	public Activity findFirst() {
        return this.activityRepo.findFirstBy();
    }
	
	public Activity findLast(User user) {
        return activityRepo.findFirstByUserOrderByIdDesc(user);
    }
	
	public Page<Activity> findByUser(User user, int page, int size) {
    return this.activityRepo.findByUser(user, PageRequest.of(page, size, Sort.by("id").descending()));
    }
    
    public List<Activity> findAll() {
        return this.activityRepo.findAll();
    }

    public void delete(Long id) {
        this.activityRepo.deleteById(id);
    }
	
	
}
