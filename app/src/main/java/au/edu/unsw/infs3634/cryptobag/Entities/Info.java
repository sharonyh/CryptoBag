package au.edu.unsw.infs3634.cryptobag.Entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

public class Info implements Serializable {

    @SerializedName("coins_num")
    @Expose
    private Integer coinsNum;
    @SerializedName("time")
    @Expose
    private Integer time;

    public Info() {
    }

    /**
     *
     * @param time
     * @param coinsNum
     */
    public Info(Integer coinsNum, Integer time) {
        super();
        this.coinsNum = coinsNum;
        this.time = time;
    }

    public Integer getCoinsNum() {
        return coinsNum;
    }

    public void setCoinsNum(Integer coinsNum) {
        this.coinsNum = coinsNum;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

}
