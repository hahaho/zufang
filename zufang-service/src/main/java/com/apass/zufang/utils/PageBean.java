package com.apass.zufang.utils;

import java.util.List;
/**
 * 
 * @author shilin
 *
 * @param <T>
 */
public class PageBean<T> {
	
    //需要计算得来
    private int totalPage;    //总页数，通过totalRecord和pageSize计算可以得来
    private int totalRecord;    //总的记录条数。查询数据库得到的数据
    //开始索引，也就是我们在数据库中要从第几行数据开始拿，有了startIndex和pageSize，
    //就知道了subList(同limit)语句的两个数据，就能获得每页需要显示的数据了
    private int startIndex;        
    private int endIndex;
        
    //将每页要显示的数据放在list集合中
    private List<T> list;
    
	//通过pageNum，pageSize，totalRecord计算得来tatalPage和startIndex
    //构造方法中将pageNum，pageSize，totalRecord获得
    public PageBean(int pageNum,int pageSize,List<T> totalList) {
        this.totalRecord = totalList.size();
        //totalPage 总页数
        if(totalRecord%pageSize==0){
            //说明整除，正好每页显示pageSize条数据，没有多余一页要显示少于pageSize条数据的
            this.totalPage = totalRecord / pageSize;
        }else{
            //不整除，就要在加一页，来显示多余的数据。
            this.totalPage = totalRecord / pageSize +1;
        }
        this.startIndex = (pageNum-1)*pageSize ;
        if (totalPage - pageNum > 0) {
        	//开始索引
        	this.endIndex = startIndex + pageSize ;
        	list = totalList.subList(startIndex, endIndex);
		}if (totalPage - pageNum == 0) {
			//开始索引
			this.endIndex = totalRecord;
			list = totalList.subList(startIndex, endIndex);
		}
        
    }
    //get、set方法。
    public int getTotalRecord() {
        return totalRecord;
    }

    public void setTotalRecord(int totalRecord) {
        this.totalRecord = totalRecord;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

	public int getEndIndex() {
		return endIndex;
	}
	public void setEndIndex(int endIndex) {
		this.endIndex = endIndex;
	}
    

}