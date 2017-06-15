package org.reapsn.paceditor.model;

import java.util.LinkedHashSet;

/**
 * Created by Reaps on 2017/6/14.
 */
public class PAC {

	private String name = "";
	private String proxy = "";
	private LinkedHashSet<String> rules = new LinkedHashSet<String>();

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

	public LinkedHashSet<String> getRules() {
		return rules;
	}

	public void setRules(LinkedHashSet<String> rules) {
		this.rules = rules;
	}
}
