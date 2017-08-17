
package com.taxifind.kts.POJOs;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Country {

    @SerializedName("country_id")
    @Expose
    public Integer countryId;
    @SerializedName("Name")
    @Expose
    public String name;
    @SerializedName("Code")
    @Expose
    public String code;
    @SerializedName("Geometry")
    @Expose
    public Object geometry;
    @SerializedName("Date_Added")
    @Expose
    public String dateAdded;
    @SerializedName("Last_Update_Date")
    @Expose
    public String lastUpdateDate;
}
