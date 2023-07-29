package com.driver.transformer;

import com.driver.EntryDto.SubscriptionEntryDto;
import com.driver.model.Subscription;
import com.driver.model.SubscriptionType;

import javax.xml.crypto.Data;
import java.util.Date;

public class SubscriptionTransformer {

    public static int getSubscribeAmount(SubscriptionType subscriptionType, int noOfScreenSubscribed){
        if(subscriptionType==SubscriptionType.BASIC){
            return 500 + 200*noOfScreenSubscribed;
        }else if(subscriptionType==SubscriptionType.PRO){
            return 800 + 250*noOfScreenSubscribed;
        }
        else if(subscriptionType==SubscriptionType.ELITE){
            return 1000 + 350*noOfScreenSubscribed;
        }
        return -1;
    }
    public static Subscription SubscriptionEntryDtoToSubscription(SubscriptionEntryDto subscriptionEntryDto){
        Subscription subscription = new Subscription();
        subscription.setSubscriptionType(subscriptionEntryDto.getSubscriptionType());
        subscription.setNoOfScreensSubscribed(subscriptionEntryDto.getNoOfScreensRequired());
        int amount = getSubscribeAmount(subscriptionEntryDto.getSubscriptionType(),subscriptionEntryDto.getNoOfScreensRequired());
        subscription.setTotalAmountPaid(amount);
        return subscription;
    }
}
