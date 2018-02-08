package com.apass.zufang.common.model;
import java.util.Date;
public class CreatedUser extends QueryParams{
	private Date createdTime;
    private Date updatedTime;
    private String createdUser;
    private String updatedUser;
    public String getCreatedUser() {
        return createdUser;
    }
    public void setCreatedUser(String createdUser) {
        this.createdUser = createdUser;
    }
    public String getUpdatedUser() {
        return updatedUser;
    }
    public void setUpdatedUser(String createdUser) {
        this.createdUser = createdUser;
    }
    public Date getCreatedTime() {
        return createdTime;
    }
    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }
    public Date getUpdatedTime() {
        return updatedTime;
    }
    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }
    public void fillUser(String user){
        setUpdatedUser(user);
    }
    public void fillTime(){
        setUpdatedTime(new Date());
    }
    public void fillAllUser(String user){
        setCreatedUser(user);
        setUpdatedUser(user);
    }
    public void fillAllTime(){
        setCreatedTime(new Date());
        setUpdatedTime(new Date());
    }
    public void fillAllField(String user){
        fillAllUser(user);
        fillAllTime();
    }
    public void fillField(String user){
        fillUser(user);
        fillTime();
    }
}