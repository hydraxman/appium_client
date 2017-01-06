package com.yunniao.appiumtest.bean;

import java.util.ArrayList;

/**
 * Created by MrBu on 2016/1/13.
 */
public class Operation {
	private String desc;
	private int type;
	private ArrayList<Element> elements;
	private ArrayList<Verify> verify;
	private Operation(){}
	private int sleepCount;
	private String text;
	private ArrayList<ApiParam> apiParams;
	private ArrayList<?> vars;

	public ArrayList<?> getVars() {
		return vars;
	}

	public void setVars(ArrayList<?> vars) {
		this.vars = vars;
	}

	public ArrayList<ApiParam> getApiParams() {
		return apiParams;
	}

	public void setApiParams(ArrayList<ApiParam> apiParams) {
		this.apiParams = apiParams;
	}

	public ArrayList<Verify> getVerify() {
		return verify;
	}

	public void setVerify(ArrayList<Verify> verify) {
		this.verify = verify;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public ArrayList<Element> getElements() {
		return elements;
	}

	public void setElements(ArrayList<Element> elements) {
		this.elements = elements;
	}

	public int getSleepCount() {
		return sleepCount;
	}

	public void setSleepCount(int sleepCount) {
		this.sleepCount = sleepCount;
	}

}
