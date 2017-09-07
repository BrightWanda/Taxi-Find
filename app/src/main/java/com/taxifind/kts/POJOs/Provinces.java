
package com.taxifind.kts.POJOs;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Provinces {

    @SerializedName("odata.metadata")
    @Expose
    private String odataMetadata;
    @SerializedName("value")
    @Expose
    private List<ValueProv> value = null;

    public String getOdataMetadata() {
        return odataMetadata;
    }

    public void setOdataMetadata(String odataMetadata) {
        this.odataMetadata = odataMetadata;
    }

    public List<ValueProv> getValueProv() {
        return value;
    }

    public void setValueProv(List<ValueProv> value) {
        this.value = value;
    }

}
