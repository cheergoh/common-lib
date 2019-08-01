package showfree.commoncore.tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 字符串模版工具
 * @author 
 *
 */
public final class StringTemplate {

private List<Pack> packList = new ArrayList<Pack>();
	
	private String[] variables;
	private Map<String, Pack> pairLookup = new HashMap<String, Pack>();
	
	public StringTemplate(String tpl) {
		if(!StringTool.isEmpty(tpl)) {
			int begin = -1;
			int end = -1;
			Pack pack = null;
			List<Pack> pairList = new ArrayList<Pack>();
			
			while(tpl.length() > 1 && (begin = tpl.indexOf("$")) >= 0) {
				if(begin > 0) {
					packList.add(new Pack(false, tpl.substring(0, begin)));
				}
				switch(tpl.charAt(begin + 1)) {
					case '{':
						end = tpl.indexOf("}", begin + 2);
						if(end >= 0) {
							pack = new Pack(true, tpl.substring(begin + 2, end));
							packList.add(pack);
							pairList.add(pack);
							pairLookup.put(pack.name, pack);
							tpl = tpl.substring(end + 1);
						}else {
							packList.add(new Pack(false, tpl.substring(begin + 2)));
							tpl = "";
						}
						break;
					case '$':
						packList.add(new Pack(false, "$"));
						tpl = tpl.substring(begin + 2);
						break;
					default:
						tpl = tpl.substring(begin + 1);
						break;
				}
			}
			
			if(tpl.length() > 0) {
				packList.add(new Pack(false, tpl));
			}
			
			variables = new String[pairList.size()];
			for(int i = 0; i < pairList.size(); i++) {
				variables[i] = pairList.get(i).name;
			}
		}
	}
	
	public String[] getVariables() {
		return variables;
	}
	
	public void setVariable(String name, Object value) {
		Pack pack = pairLookup.get(name);
		if(pack != null) {
			pack.value = value == null ? "" : String.valueOf(value);
		}
	}
	
	public void reset() {
		for(String name : variables) {
			pairLookup.get(name).value = null;
		}
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		for(Pack pack : packList) {
			sb.append(pack.value == null ? "" : pack.value);
		}
		return sb.toString();
	}
	
	private final class Pack {
		boolean isPair;
		String name;
		String value;
		
		Pack(boolean isPair, String text) {
			this.isPair = isPair;
			if(isPair) {
				this.name = text;
			}else {
				this.value = text;
			}
		}
	}
}
