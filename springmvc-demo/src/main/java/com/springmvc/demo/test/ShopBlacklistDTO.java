package com.springmvc.demo.test;

import com.chinaredstar.demeter.excel.annotation.RedStarExcelField;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by mengran.gao on 2018/7/18.
 */
public class ShopBlacklistDTO implements Serializable {
    private Long id;
    @RedStarExcelField(title = "商场编号", sort = 1)
    private String marketNo;
    @RedStarExcelField(title = "商场名称", sort = 2)
    private String marketName;
    @RedStarExcelField(title = "店铺号", sort = 3)
    private String shopNo;
    @RedStarExcelField(title = "店铺名称", sort = 4)
    private String shopName;
    private Byte status;
    @RedStarExcelField(title = "店铺状态", sort = 5)
    private String statusDesc;
    private Date createTime;
    private Date updateTime;
    private List<String> marketNos;
    private List<String> shopNos;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMarketNo() {
        return marketNo;
    }

    public void setMarketNo(String marketNo) {
        this.marketNo = marketNo == null ? null : marketNo.trim();
    }

    public String getMarketName() {
        return marketName;
    }

    public void setMarketName(String marketName) {
        this.marketName = marketName == null ? null : marketName.trim();
    }

    public String getShopNo() {
        return shopNo;
    }

    public void setShopNo(String shopNo) {
        this.shopNo = shopNo == null ? null : shopNo.trim();
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName == null ? null : shopName.trim();
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public List<String> getMarketNos() {
        return marketNos;
    }

    public void setMarketNos(List<String> marketNos) {
        this.marketNos = marketNos;
    }

    public List<String> getShopNos() {
        return shopNos;
    }

    public void setShopNos(List<String> shopNos) {
        this.shopNos = shopNos;
    }

    public String getStatusDesc() {
        return statusDesc;
    }

    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }
}
