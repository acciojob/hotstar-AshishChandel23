package com.driver.services;

import com.driver.EntryDto.WebSeriesEntryDto;
import com.driver.model.ProductionHouse;
import com.driver.model.WebSeries;
import com.driver.repository.ProductionHouseRepository;
import com.driver.repository.WebSeriesRepository;
import com.driver.transformer.WebSeriesTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class WebSeriesService {

    @Autowired
    WebSeriesRepository webSeriesRepository;

    @Autowired
    ProductionHouseRepository productionHouseRepository;

    public Integer addWebSeries(WebSeriesEntryDto webSeriesEntryDto)throws  Exception{

        //Add a webSeries to the database and update the ratings of the productionHouse
        //Incase the seriesName is already present in the Db throw Exception("Series is already present")
        //use function written in Repository Layer for the same
        //Dont forget to save the production and webseries Repo
        Optional<ProductionHouse> houseOptional = productionHouseRepository.findById(webSeriesEntryDto.getProductionHouseId());
        if(houseOptional.isEmpty()){
            return null;
        }
        ProductionHouse productionHouse = houseOptional.get();
        WebSeries webSeries = webSeriesRepository.findBySeriesName(webSeriesEntryDto.getSeriesName());
        if(webSeries!=null){
            throw new RuntimeException("Series is already present");
        }
        webSeries = WebSeriesTransformer.WebSeriesEntryDtoToWebSeries(webSeriesEntryDto);
        webSeries.setProductionHouse(productionHouse);
        productionHouse.getWebSeriesList().add(webSeries);
        double rating = 0.0;
        for(WebSeries ws : productionHouse.getWebSeriesList()){
            rating+=ws.getRating();
        }
        rating/=productionHouse.getWebSeriesList().size();
        productionHouse.setRatings(rating);
        ProductionHouse savedProductionHouse = productionHouseRepository.save(productionHouse);
        List<WebSeries> list = savedProductionHouse.getWebSeriesList();
        return list.get(list.size()-1).getId();
    }

}
