package com.driver.repository;

import com.driver.model.SubscriptionType;
import com.driver.model.WebSeries;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WebSeriesRepository extends JpaRepository<WebSeries,Integer> {

    WebSeries findBySeriesName(String seriesName);


    @Query(value = "Select ws from WebSeries ws where ws.subscriptionType = :subscription and ws.ageLimit < :age")
    List<WebSeries> viewableWebSeries(SubscriptionType subscription, int age);
}
