package com.todo.dao;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TodoItem {
	private int id;
    private String title;
    private String desc;
    private String current_date;
    private String category;
    private String due_date;
    private int is_completed;
    private String es_time;
    private String ac_time;
    
    public TodoItem(String category, String title, String desc, String due_date, String es_time){
        this.title=title;
        this.desc=desc;
        this.category=category;
        this.due_date=due_date;
        this.es_time=es_time;
        SimpleDateFormat f = new SimpleDateFormat("yyyy/MM/dd kk:mm:ss");
        this.current_date=f.format(new Date());
    }
    
	public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getCurrent_date() {
        return current_date;
    }

    public void setCurrent_date(String current_date) {
        this.current_date = current_date;
    }
    
    public String getCategory() {
        return category;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }
    
    public String getDue_date() {
        return due_date;
    }

    public void setDue_date(String due_date) {
        this.due_date = due_date;
    }
    
    public int getId() {
    	return id;
    }
    
    public void setId(int id) {
		this.id=id;
	}
    
    public int getIs_completed() {
		return is_completed;
	}

	public void setIs_completed(int is_completed) {
		this.is_completed = is_completed;
	}
	
    public String getEs_time() {
		return es_time;
	}

	public void setEs_time(String es_time) {
		this.es_time = es_time;
	}

	public String getAc_time() {
		return ac_time;
	}

	public void setAc_time(String ac_time) {
		this.ac_time = ac_time;
	} 
    
    @Override
	public String toString() {
    	if (is_completed==0){
    		return id + ". [" + category +"] " + title + " - " + desc + " - " + due_date + " - " + current_date + " - " + es_time;
    	}
    	else {
    		return id + ". [" + category +"] " + title + "[V] " + " - " + desc + " - " + due_date + " - " + current_date + " - " + es_time + " - " + ac_time;
    	}
	}
}
