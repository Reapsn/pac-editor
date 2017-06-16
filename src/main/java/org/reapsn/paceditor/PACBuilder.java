package org.reapsn.paceditor;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.reapsn.paceditor.model.PAC;
import org.reapsn.paceditor.util.JavaScriptUtil;
import sun.org.mozilla.javascript.internal.NativeArray;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Reaps on 2017/6/16.
 */
public class PACBuilder {

	public static final String RES_JS_COMBINED_MATCHER = "model/CombinedMatcher.js";
	public static final String RES_JS_PAC = "model/Pac.js";

	private LinkedHashSet<PAC> pacs;

	public PACBuilder(LinkedHashSet<PAC> pacs) {
		this.pacs = pacs;
	}

	public String build() throws IOException {
		StringBuilder pacBuilder = new StringBuilder();

		URL combinedMatcherURL = this.getClass().getClassLoader().getResource(RES_JS_COMBINED_MATCHER);
		URL pacURL = this.getClass().getClassLoader().getResource(RES_JS_PAC);

		pacBuilder.append(IOUtils.toString(combinedMatcherURL, "UTF-8"));

		String title = "// Generate by PAC EDITOR\r\n// https://github.com/Reapsn/pac-editor";

		pacBuilder.append("\r\n\r\n").append(title).append("\r\n\r\n").append(encodeToJS(pacs)).append("\r\n\r\n");

		pacBuilder.append(IOUtils.toString(pacURL, "UTF-8"));

		return pacBuilder.toString();

	}

	/**
	 * 按照 JAVASCRIPT 语法编写
	 * <p>
	 * 例如：
	 * <p>
	 * <pre>
	 *
	 *     var names = ['proxy1'];
	 *
	 *     var proxies = ['PROXY 127.0.0.1:1080;'];
	 *
	 *     var rules = [['google.com']];
	 *
	 * </pre>
	 *
	 * @param pacs
	 * @return
	 */
	public static String encodeToJS(LinkedHashSet<PAC> pacs) {

		// js: var names = [];
		StringBuilder namesBuilder = new StringBuilder();
		namesBuilder.append("var names = [");

		// js: var proxies = [];
		StringBuilder proxiesBuilder = new StringBuilder();
		proxiesBuilder.append("var proxies = [");

		// js: var rules = [[]];
		StringBuilder rulesBuilder = new StringBuilder();
		rulesBuilder.append("var rules = [");

		for (PAC pac :
				pacs) {
			if (StringUtils.isBlank(pac.getProxy())) {
				break;
			}

			namesBuilder.append("\"").append(pac.getName()).append("\",\r\n");

			proxiesBuilder.append("\"").append(pac.getProxy()).append("\",\r\n");

			rulesBuilder.append("[");
			LinkedHashSet<String> rules = pac.getRules();
			for (String rule :
					rules) {
				rulesBuilder.append("\"").append(rule).append("\",\r\n");
			}
			rulesBuilder.append("],\r\n");
		}
		namesBuilder.append("];\r\n");
		proxiesBuilder.append("];\r\n");
		rulesBuilder.append("];");

		return namesBuilder.append(proxiesBuilder).append(rulesBuilder).toString();
	}

	/**
	 * 从 PAC 文件 中找出代理名称、代理服务器、代理规则，并实例化未 JAVA 对象
	 *
	 * @param pacString
	 * @return
	 */
	public static LinkedHashSet<PAC> decodeFromPAC(String pacString) throws Exception {
		if (pacString == null) {
			return null;
		}

		ScriptEngineManager sem = new ScriptEngineManager();
		ScriptEngine engine = sem.getEngineByName("javascript");

		try {
			engine.eval(pacString);
		} catch (ScriptException e) {
			throw new Exception("PAC文件无效，解析失败", e);
		}

		// find names
		List<String> names = new ArrayList<String>();
		NativeArray js_names = (NativeArray) engine.get("names");
		List<String> java_names = JavaScriptUtil.convertToJavaList(js_names, String.class);
		if (java_names != null) {
			names.addAll(java_names);
		}

		// find proxies
		List<String> proxies = new ArrayList<String>();
		String js_proxy = (String) engine.get("proxy");
		if (!StringUtils.isBlank(js_proxy)) {
			proxies.add(js_proxy);
		} else {
			NativeArray js_proxies = (NativeArray) engine.get("proxies");
			List<String> java_proies = JavaScriptUtil.convertToJavaList(js_proxies, String.class);
			if (java_proies != null) {
				proxies.addAll(java_proies);
			}
		}

		// find rules
		List<LinkedHashSet<String>> rules = new ArrayList<LinkedHashSet<String>>();

		NativeArray js_rules = (NativeArray) engine.get("rules");
		Class contentType = JavaScriptUtil.getContentType(js_rules);
		if (contentType == null) {

		} else if (contentType.isAssignableFrom(NativeArray.class)) {
			List<List<String>> java_rules = JavaScriptUtil.convertToJavaList(js_rules, (Class<List<String>>) Class.forName("java.util.List"));

			for (List<String> java_oneRules :
					java_rules) {
				LinkedHashSet<String> oneRules = new LinkedHashSet<String>();
				oneRules.addAll(java_oneRules);
				rules.add(oneRules);
			}

		} else {
			List<String> java_rules = JavaScriptUtil.convertToJavaList(js_rules, String.class);
			LinkedHashSet<String> oneRules = new LinkedHashSet<String>();
			oneRules.addAll(java_rules);
			rules.add(oneRules);
		}

		return toPACs(names, proxies, rules);
	}

	public static LinkedHashSet<PAC> toPACs(List<String> names, List<String> proxies, List<LinkedHashSet<String>> rules) {

		LinkedHashSet<PAC> pacs = new LinkedHashSet<PAC>();

		int pacsSize = Math.max(Math.max(names.size(), rules.size()), rules.size());

		for (int i = 0; i < pacsSize; i++) {
			PAC pac = new PAC();

			if (names.size() > i) {
				pac.setName(names.get(i));
			} else {
				pac.setName(UUID.randomUUID().toString());
			}

			if (proxies.size() > i) {
				pac.setProxy(proxies.get(i));
			} else {
				continue;
			}

			if (rules.size() > i) {
				pac.setRules(rules.get(i));
			} else {
				continue;
			}

			pacs.add(pac);

		}

		return pacs;
	}


}
