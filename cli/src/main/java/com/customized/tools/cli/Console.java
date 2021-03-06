package com.customized.tools.cli;

public class Console {

	private static final long DEFAULT_SLEEP_TIME = 1000;
	
	protected static final String TAB = "    ";
	
	protected static final String LN = "\n";
	
	public void print(Object obj) {
		System.out.print(obj);
	}

	public void println(Object obj) {
		System.out.println(obj);
	}
	
	public void prompt(Object obj) {
		println(LN + TAB + obj + LN);
	}
	
	public String twoTab() {
		return TAB + TAB ;
	}
	
	public String ln(){
		return LN;
	}
	
	public String ln(int num){
		String result = "";
		for(int i = 0 ; i < num ; i ++){
			result += LN;
		}
		return result;
	}
	
	public String tab(){
		return TAB;
	}
	

	public void pause(Object obj) {
		print(obj);
		sleep(DEFAULT_SLEEP_TIME);
	}

	public void pauseln(Object obj) {
		println(obj);
		sleep(DEFAULT_SLEEP_TIME);
	}

	@SuppressWarnings("static-access")
	public void sleep(long time) {
		try {
			Thread.currentThread().sleep(time);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}
	
	protected abstract class Validation {
		
		protected abstract boolean validate(Object obj) throws RuntimeException ;
	}
}
