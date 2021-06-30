package enshud.s4.compiler;

public class WhatEbnf {
	private boolean isSub;
    private boolean isWrite;
    private boolean isRead;
    private boolean isPara;
    private boolean afterVar;
    private boolean isAssign;
    private boolean isAssignLeft;
    private boolean isAssignSubscript;
    private boolean isCode;
    private boolean isWhileCondition;
    private boolean isIfCondition;
    private int depthOfAssign;
    private int depthOfWrite;
    private int depthOfConditions;
    private int numOfIfWhile;
    private int readingLine;
    private int numOfFormula;
    private int numOfParam;

	public boolean getIsSub() {
		return isSub;
	}

	public void setIsSub(boolean isSub) {
		this.isSub = isSub;
	}

	public boolean getIsWrite() {
		return isWrite;
	}

	public void setIsWrite(boolean isWrite) {
		this.isWrite = isWrite;
	}

	public boolean getAfterVar() {
		return afterVar;
	}

	public void setAfterVar(boolean afterProc) {
		this.afterVar = afterProc;
	}

	public boolean getIsAssign() {
		return isAssign;
	}

	public void setIsAssign(boolean isAssign) {
		this.isAssign = isAssign;
	}

	public boolean getIsAssignLeft() {
		return isAssignLeft;
	}

	public void setIsAssignLeft(boolean isAssignVar) {
		this.isAssignLeft = isAssignVar;
	}

	public boolean getIsCode() {
		return isCode;
	}

	public void setIsCode(boolean isCode) {
		this.isCode = isCode;
	}

	public boolean getIsAssignSubscript() {
		return isAssignSubscript;
	}

	public void setIsAssignSubscript(boolean isAssignSubscript) {
		this.isAssignSubscript = isAssignSubscript;
	}

	public int getDepthOfAssign() {
		return depthOfAssign;
	}

	public void setDepthOfAssign(int numOfAssignArray) {
		this.depthOfAssign = numOfAssignArray;
	}

	public int getDepthOfWrite() {
		return depthOfWrite;
	}

	public void setDepthOfWrite(int depthOfWrite) {
		this.depthOfWrite = depthOfWrite;
	}

	public int getDepthOfConditions() {
		return depthOfConditions;
	}

	public void setDepthOfConditions(int depthOfStatements) {
		this.depthOfConditions = depthOfStatements;
	}

	public boolean getIsWhileCondition() {
		return isWhileCondition;
	}

	public void setIsWhileCondition(boolean isWhileCondition) {
		this.isWhileCondition = isWhileCondition;
	}

	public boolean getIsIfCondition() {
		return isIfCondition;
	}

	public void setIsIfCondition(boolean isIfCondition) {
		this.isIfCondition = isIfCondition;
	}

	public int getNumOfIfWhile() {
		return numOfIfWhile;
	}

	public void setNumOfIfWhile(int numOfIfWhile) {
		this.numOfIfWhile = numOfIfWhile;
	}

	public int getReadingLine() {
		return readingLine;
	}

	public void setReadingLine(int readingLine) {
		this.readingLine = readingLine;
	}

	public int getNumOfFormula() {
		return numOfFormula;
	}

	public void setNumOfFormula(int numOfFormula) {
		this.numOfFormula = numOfFormula;
	}

	public int getNumOfParm() {
		return numOfParam;
	}

	public void setNumOfParam(int numOfParam) {
		this.numOfParam = numOfParam;
	}

	public boolean getIsPara() {
		return isPara;
	}

	public void setIsPara(boolean isPara) {
		this.isPara = isPara;
	}

	public boolean getIsRead() {
		return isRead;
	}

	public void setIsRead(boolean isRead) {
		this.isRead = isRead;
	}

}
