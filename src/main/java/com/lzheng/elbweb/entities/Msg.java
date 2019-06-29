package com.lzheng.elbweb.entities;

import lombok.Data;

/**
 * @ClassName Msg
 * @Author lzheng
 * @Date 2019/6/28 16:07
 * @Version 1.0
 * @Description:
 */


public class Msg {
    String remainelectric;
    String errorNumber;
    String[] table;

    public String getRemainelectric() {
        return remainelectric;
    }

    public void setRemainelectric(String remainelectric) {
        this.remainelectric = remainelectric;
    }

    public String getErrorNumber() {
        return errorNumber;
    }

    public void setErrorNumber(String errorNumber) {
        this.errorNumber = errorNumber;
    }

    public String[] getTable() {
        return table;
    }

    public void setTable(String[] table) {
        this.table = table;
    }
}
