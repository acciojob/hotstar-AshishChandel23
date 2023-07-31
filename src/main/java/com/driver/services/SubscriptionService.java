package com.driver.services;


import com.driver.EntryDto.SubscriptionEntryDto;
import com.driver.model.Subscription;
import com.driver.model.SubscriptionType;
import com.driver.model.User;
import com.driver.repository.SubscriptionRepository;
import com.driver.repository.UserRepository;
import com.driver.transformer.SubscriptionTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class SubscriptionService {

    @Autowired
    SubscriptionRepository subscriptionRepository;

    @Autowired
    UserRepository userRepository;

    public Integer buySubscription(SubscriptionEntryDto subscriptionEntryDto){

        //Save The subscription Object into the Db and return the total Amount that user has to pay
        Optional<User> optionalUser = userRepository.findById(subscriptionEntryDto.getUserId());
        if(optionalUser.isEmpty()){
            return null;
        }
        User user = optionalUser.get();
        Subscription subscription = SubscriptionTransformer.SubscriptionEntryDtoToSubscription(subscriptionEntryDto);
        subscription.setUser(user);
        user.setSubscription(subscription);
        User savedUser = userRepository.save(user);
        return savedUser.getSubscription().getTotalAmountPaid();
    }

    public Integer upgradeSubscription(Integer userId)throws Exception{

        //If you are already at an ElITE subscription : then throw Exception ("Already the best Subscription")
        //In all other cases just try to upgrade the subscription and tell the difference of price that user has to pay
        //update the subscription in the repository
        User user=userRepository.findById(userId).get();
        if(user.getSubscription().getSubscriptionType().toString().equals("ELITE")){
            throw new Exception("Already the best Subscription");
        }

        Subscription subscription=user.getSubscription();
        Integer previousFair=subscription.getTotalAmountPaid();
        Integer currentFair;
        if(subscription.getSubscriptionType().equals(SubscriptionType.BASIC)){
            subscription.setSubscriptionType(SubscriptionType.PRO);
            currentFair =previousFair+300+(50*subscription.getNoOfScreensSubscribed());
        }else {
            subscription.setSubscriptionType(SubscriptionType.ELITE);
            currentFair=previousFair+200+(100*subscription.getNoOfScreensSubscribed());
        }

        subscription.setTotalAmountPaid(currentFair);
        user.setSubscription(subscription);
        subscriptionRepository.save(subscription);

        return currentFair-previousFair;
//        Optional<User> optionalUser = userRepository.findById(userId);
//        if(optionalUser.isEmpty()){
//            return null;
//        }
//        User user = optionalUser.get();
//        if(user.getSubscription().getSubscriptionType()==SubscriptionType.ELITE){
//            throw new RuntimeException("Already the best Subscription");
//        }
//        SubscriptionType newSubscriptionType = null;
//        if(user.getSubscription().getSubscriptionType()==SubscriptionType.BASIC){
//            newSubscriptionType = SubscriptionType.PRO;
//        }
//        else if(user.getSubscription().getSubscriptionType()==SubscriptionType.PRO){
//            newSubscriptionType = SubscriptionType.ELITE;
//        }
//        int newAmountPaid = SubscriptionTransformer.getSubscribeAmount(newSubscriptionType, user.getSubscription().getNoOfScreensSubscribed());
//        int diffAmount = newAmountPaid - user.getSubscription().getTotalAmountPaid();
//        user.getSubscription().setSubscriptionType(newSubscriptionType);
//        user.getSubscription().setTotalAmountPaid(newAmountPaid);
//        userRepository.save(user);
//        return diffAmount;
    }

    public Integer calculateTotalRevenueOfHotstar(){

        //We need to find out total Revenue of hotstar : from all the subscriptions combined
        //Hint is to use findAll function from the SubscriptionDb

        //return subscriptionRepository.totalRevenue();
        List<Subscription> subscriptionList = subscriptionRepository.findAll();

        Integer totalRevenue = 0;
        for(Subscription subscription : subscriptionList){
            totalRevenue += subscription.getTotalAmountPaid();
        }
        return totalRevenue;
    }

}
