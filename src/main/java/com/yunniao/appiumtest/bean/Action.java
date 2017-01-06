package com.yunniao.appiumtest.bean;

import java.util.ArrayList;

/**
 * Created by MrBu on 2016/1/13.
 */
public class Action {
	private String text;
	private String name;
	private double startX;
	private double startY;
	private double endX;
	private double endY;
	private int duration;
	private int keyCode;
	private int elementIndex;
	private int fingers;
	private int repeatCount;
	private ArrayList<Var> vars;
	private boolean ignore;

	public boolean isIgnore() {
		return ignore;
	}

	public void setIgnore(boolean ignore) {
		this.ignore = ignore;
	}

	public ArrayList<Var> getVars() {
		return vars;
	}

	public void setVars(ArrayList<Var> vars) {
		this.vars = vars;
	}

	public int getRepeatCount() {
		return repeatCount;
	}

	public void setRepeatCount(int repeatCount) {
		this.repeatCount = repeatCount;
	}

	public int getFingers() {
		return fingers;
	}

	public void setFingers(int fingers) {
		this.fingers = fingers;
	}

	public int getElementIndex() {
		return elementIndex;
	}

	public void setElementIndex(int elementIndex) {
		this.elementIndex = elementIndex;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getStartX()
	{
		return  startX;
	}

	public void setStartX(double startX)
	{
		this.startX = startX;
	}

	public double getStartY()
	{
		return startY;
	}

	public void setStartY(double startY)
	{
		this.startY = startY;
	}

	public double getEndX()
	{
		return endX;
	}

	public void setEndX(double endX)
	{
		this.endX = endX;
	}

	public double getEndY()
	{
		return endY;
	}

	public void setEndY(double endY)
	{
		this.endY = endY;
	}

	public int getDuration()
	{
		return duration;
	}

	public void setDuration(int duration)
	{
		this.duration = duration;
	}

	public int getKeyCode()
	{
		return keyCode;
	}

	public void setKeyCode(int keyCode)
	{
		this.keyCode = keyCode;
	}

	public Action(){}
}
