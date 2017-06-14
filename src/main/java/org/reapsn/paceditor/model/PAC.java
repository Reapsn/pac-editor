package org.reapsn.paceditor.model;

import java.util.LinkedList;

/**
 * Created by Reaps on 2017/6/14.
 */
public class PAC {

	private String name = "";
	private String proxy = "";
	private LinkedList<String> rules = new LinkedList<>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getProxy() {
		return proxy;
	}

	public void setProxy(String proxy) {
		this.proxy = proxy;
	}

	public LinkedList<String> getRules() {
		return rules;
	}

	public void setRules(LinkedList<String> rules) {
		this.rules = rules;
	}
}
