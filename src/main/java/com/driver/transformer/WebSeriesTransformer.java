package com.driver.transformer;

import com.driver.EntryDto.WebSeriesEntryDto;
import com.driver.model.WebSeries;

public class WebSeriesTransformer {
    public static WebSeries WebSeriesEntryDtoToWebSeries(WebSeriesEntryDto webSeriesEntryDto){
        WebSeries series = new WebSeries();
        series.setSeriesName(webSeriesEntryDto.getSeriesName());
        series.setAgeLimit(webSeriesEntryDto.getAgeLimit());
        series.setRating(webSeriesEntryDto.getRating());
        series.setSubscriptionType(webSeriesEntryDto.getSubscriptionType());
        return series;
    }
}
