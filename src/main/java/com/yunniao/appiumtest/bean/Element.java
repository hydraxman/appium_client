package com.yunniao.appiumtest.bean;

import java.util.ArrayList;

/**
 * Created by MrBu on 2016/1/13.
 */
public class Element<T extends Object> {
	private String text;
	private ArrayList<Action> actions;
	private Verify verify;
	private String className;
	private T classIndex;
	private String id;
	private Element subElement;
	private int textMatchType;
	private int textType;
	private String xPath;
	private int numText;
	private String desc;
	private Element siblingElement;
	private boolean ignoreElement = false;
	private String attribute;
	private String value;
	private ArrayList<Var> vars;

	public ArrayList<Var> getVars() {
		return vars;
	}

	public void setVars(ArrayList<Var> vars) {
		this.vars = vars;
	}

	public String getAttribute() {
		return attribute;
	}

	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public boolean isIgnoreElement() {
		return ignoreElement;
	}

	public void setIgnoreElement(boolean ignoreElement) {
		this.ignoreElement = ignoreElement;
	}

	public Element getSiblingElement() {
		return siblingElement;
	}

	public void setSiblingElement(Element siblingElement) {
		this.siblingElement = siblingElement;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public int getNumText() {
		return numText;
	}

	public void setNumText(int numText) {
		this.numText = numText;
	}

	public String getxPath() {
		return xPath;
	}

	public void setxPath(String xPath) {
		this.xPath = xPath;
	}

	public int getTextType() {
		return textType;
	}

	public void setTextType(int textType) {
		this.textType = textType;
	}

	public int getTextMatchType() {
		return textMatchType;
	}

	public void setTextMatchType(int textMatchType) {
		this.textMatchType = textMatchType;
	}

	public Element getSubElement() {
		return subElement;
	}

	public void setSubElement(Element subElement) {
		this.subElement = subElement;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public ArrayList<Action> getActions() {
		return actions;
	}

	public void setActions(ArrayList<Action> actions) {
		this.actions = actions;
	}

	public Verify getVerify() {
		return verify;
	}

	public void setVerify(Verify verify) {
		this.verify = verify;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public T getClassIndex() {
		return classIndex;
	}

	public void setClassIndex(T classIndex) {
		this.classIndex = classIndex;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Element(){}
}
