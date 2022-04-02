package version_5;

import java.io.PrintStream;
import java.util.HashMap;

public class Environment {
	HashMap<String,Reference> variables;
	PrintStream ps;
	public Environment(PrintStream ps){
		this.ps=ps;
		this.variables = new HashMap<String,Reference>();
	}

	public boolean addReference(String name, Reference ref) {
		if(!this.variables.containsKey(name)) {
			this.variables.put(name, ref);
			return true;
		}else{
			ps.println("La r�f�rence "+name+" existe d�j�");
			return false;
		}
	}

	public Reference getReferenceByName(String name) {
		Reference ref = this.variables.get(name);
		if(ref == null) {
			ps.println(name+" n'existe pas");
		}
		return ref;
	}

	public void removeReference(String name) {
		this.variables.remove(name);
	}
}
