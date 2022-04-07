package version_4;


import java.util.HashMap;
import java.util.Map;

import stree.parser.SNode;
//classe qui représente une référence d'un objet dans l'environnement

public class Reference{
	Object receiver;
	Map<String, Command> primitives;

	
	public Reference(Object receiver) {
		this.receiver = receiver;
		primitives = new HashMap<String,Command>();
		
	}
	//getter
	public Object getReceiver() {
		return this.receiver;
	}
	//
	public Command getCommandByName(String selector) {
		return this.primitives.get(selector);
	}
	
	public void addCommand(String selector,Command primitive) {
		if(!this.primitives.containsKey(selector)) {
			this.primitives.put(selector, primitive);
		}
	}
	
	public Reference run(SNode method)throws Exception {
		return this.getCommandByName(method.get(1).contents()).run(this, method);
		
	}
	
}