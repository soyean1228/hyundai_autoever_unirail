package com.easysubway;


public class RouteStation {
    private String startStation = "발산";
    private String finalStation = "동묘앞";
    public void setStartStation(String station) {
        startStation = station;
    }
    public void setFinalStation(String station) {
        finalStation = station;
    }
    public String getStartStation(){
        return startStation;
    }
    public String getFinalStation(){
        return finalStation;
    }
}
