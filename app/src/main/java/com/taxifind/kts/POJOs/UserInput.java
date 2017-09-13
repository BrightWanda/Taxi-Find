
package com.taxifind.kts.POJOs;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserInput {

    @SerializedName("user_input_id")
    @Expose
    private Integer userInputId;
    @SerializedName("Origin")
    @Expose
    private String origin;
    @SerializedName("Destination")
    @Expose
    private String destination;
    @SerializedName("Price")
    @Expose
    private String price;
    @SerializedName("Origin_Loc_Lat")
    @Expose
    private Double originLocLat;
    @SerializedName("Origin_Loc_Long")
    @Expose
    private Double originLocLong;
    @SerializedName("Destination_Loc_Lat")
    @Expose
    private Double destinationLocLat;
    @SerializedName("Destination_Loc_Long")
    @Expose
    private Double destinationLocLong;
    @SerializedName("Valid")
    @Expose
    private Object valid;

    public Integer getUserInputId() {
        return userInputId;
    }

    public void setUserInputId(Integer userInputId) {
        this.userInputId = userInputId;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Double getOriginLocLat() {
        return originLocLat;
    }

    public void setOriginLocLat(Double originLocLat) {
        this.originLocLat = originLocLat;
    }

    public Double getOriginLocLong() {
        return originLocLong;
    }

    public void setOriginLocLong(Double originLocLong) {
        this.originLocLong = originLocLong;
    }

    public Double getDestinationLocLat() {
        return destinationLocLat;
    }

    public void setDestinationLocLat(Double destinationLocLat) {
        this.destinationLocLat = destinationLocLat;
    }

    public Double getDestinationLocLong() {
        return destinationLocLong;
    }

    public void setDestinationLocLong(Double destinationLocLong) {
        this.destinationLocLong = destinationLocLong;
    }

    public Object getValid() {
        return valid;
    }

    public void setValid(Object valid) {
        this.valid = valid;
    }

}
