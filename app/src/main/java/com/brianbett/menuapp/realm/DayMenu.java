package com.brianbett.menuapp.realm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class DayMenu extends RealmObject {
    @PrimaryKey
    String dayOfWeek;
    String breakfast1;
    String breakfast2;
    String breakfast3;

    String lunch1;
    String lunch2;
    String lunch3;

    String supper1;
    String supper2;
    String supper3;
    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public String getBreakfast1() {
        return breakfast1;
    }

    public void setBreakfast1(String breakfast1) {
        this.breakfast1 = breakfast1;
    }

    public String getBreakfast2() {
        return breakfast2;
    }

    public void setBreakfast2(String breakfast2) {
        this.breakfast2 = breakfast2;
    }

    public String getBreakfast3() {
        return breakfast3;
    }

    public void setBreakfast3(String breakfast3) {
        this.breakfast3 = breakfast3;
    }

    public String getLunch1() {
        return lunch1;
    }

    public void setLunch1(String lunch1) {
        this.lunch1 = lunch1;
    }

    public String getLunch2() {
        return lunch2;
    }

    public void setLunch2(String lunch2) {
        this.lunch2 = lunch2;
    }

    public String getLunch3() {
        return lunch3;
    }

    public void setLunch3(String lunch3) {
        this.lunch3 = lunch3;
    }

    public String getSupper1() {
        return supper1;
    }

    public void setSupper1(String supper1) {
        this.supper1 = supper1;
    }

    public String getSupper2() {
        return supper2;
    }

    public void setSupper2(String supper2) {
        this.supper2 = supper2;
    }

    public String getSupper3() {
        return supper3;
    }

    public void setSupper3(String supper3) {
        this.supper3 = supper3;
    }
}
