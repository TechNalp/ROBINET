package version_6.copy;

import java.io.PrintStream;
import java.util.HashMap;

public class Environment {
	HashMap<String,Reference> variables;
	
	Environment pere;
	PrintStream ps;
	
	public Environment(Environment pere,PrintStream ps){
		this.variables = new HashMap<String,Reference>();
		this.pere = pere;
		this.ps = ps;
	}
	
	public void setDadEnv(Environment env) {
		this.pere = env;
	}
	
	public Environment getDadEnv() {
		return this.pere;
	}
	
	public boolean addReference(String name, Reference ref) {
		if(!this.variables.containsKey(name)) {
			this.variables.put(name, ref);
			return true;
		}else{
			this.ps.println("La référence "+name+" existe déjà");
			return false;
		}
	}
	
	public Reference getReferenceByName(String name) {
		Reference ref = this.variables.get(name);
		if(ref != null) {
			return ref;
		}
		if(!name.contains(".")) {
			return null;
		}
		String[] pathElt = name.split("\\.");
		
		Environment currentEnv = this;
		Reference currentRef = null;
		for (String s : pathElt) {
			currentRef = currentEnv.getReferenceByName(s);
			if(currentRef == null) {
				return null;
			}
			currentEnv = currentRef.getHisEnv();
		}
		
		return currentRef;
		
	}
	
	public void removeReference(String name) {
		this.variables.remove(name);
	}
}
