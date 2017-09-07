
package com.taxifind.kts.POJOs;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Municipalities {

    @SerializedName("odata.metadata")
    @Expose
    private String odataMetadata;
    @SerializedName("valueMun")
    @Expose
    private List<ValueMun> valueMun = null;

    public String getOdataMetadata() {
        return odataMetadata;
    }

    public void setOdataMetadata(String odataMetadata) {
        this.odataMetadata = odataMetadata;
    }

    public List<ValueMun> getValueMun() {
        return valueMun;
    }

    public void setValueMun(List<ValueMun> valueMun) {
        this.valueMun = valueMun;
    }

}
