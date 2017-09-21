
package com.taxifind.kts.POJOs;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserInputID {

    @SerializedName("odata.metadata")
    @Expose
    private String odataMetadata;
    @SerializedName("odata.count")
    @Expose
    private String odataCount;
    @SerializedName("value")
    @Expose
    private List<Value> value = null;

    public String getOdataMetadata() {
        return odataMetadata;
    }

    public void setOdataMetadata(String odataMetadata) {
        this.odataMetadata = odataMetadata;
    }

    public String getOdataCount() {
        return odataCount;
    }

    public void setOdataCount(String odataCount) {
        this.odataCount = odataCount;
    }

    public List<Value> getValue() {
        return value;
    }

    public void setValue(List<Value> value) {
        this.value = value;
    }

}
