package version_6.copy;


import java.util.HashMap;
import java.util.Map;

import stree.parser.SNode;
import version_6.copy.Sleep.StopException;

public class Reference{
	
	Object receiver;
	Map<String, Command> primitives;

	
	Environment hisEnv;
	
	public Reference(Object receiver) {
		this.receiver = receiver;
		primitives = new HashMap<String,Command>();
		this.hisEnv = null;
		
	}
	
	public Environment getGlobalEnv() {
		Environment toReturn = this.hisEnv;
		while(toReturn.pere!=null) {
			toReturn = toReturn.pere;
		}
		return toReturn;
		
	}
	
	public void setHisEnv(Environment env) {
		this.hisEnv = env;
	}
	
	public Environment getHisEnv() {
		return this.hisEnv;
	}
	
	public Object getReceiver() {
		return this.receiver;
	}
	
	public Command getCommandByName(String selector) {
		return this.primitives.get(selector);
	}
	
	public void addCommand(String selector,Command primitive) {
		if(!this.primitives.containsKey(selector)) {
			this.primitives.put(selector, primitive);
		}
	}
	
	
	
	public Reference run(SNode method) throws StopException {
		if(method.size()<2) {
			this.hisEnv.ps.println("Commande non indiquée");
			return null;
		}
		Command cmd = this.getCommandByName(method.get(1).contents());
		if(cmd != null) {
			return cmd.run(this, method);
		}else {
			this.hisEnv.ps.println(method.get(0).contents()+" ne contient aucune commande ou script avec le nom \""+method.get(1).contents()+"\"");
			return null;
		}
	}
	
}