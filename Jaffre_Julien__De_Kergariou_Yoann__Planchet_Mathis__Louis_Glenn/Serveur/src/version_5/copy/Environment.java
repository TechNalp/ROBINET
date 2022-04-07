package version_5.copy;

import java.io.PrintStream;
import java.util.HashMap;
//ensemble de référence des éléments d'un espace graphique
public class Environment {
	HashMap<String,Reference> variables;
	PrintStream ps;
	public Environment(PrintStream ps){
		this.ps=ps;
		this.variables = new HashMap<String,Reference>();
	}
	//ajoute une référence à l'environnement
	public boolean addReference(String name, Reference ref) {
		if(!this.variables.containsKey(name)) {
			this.variables.put(name, ref);
			return true;
		}else{
			ps.println("La référence "+name+" existe déjà");
			return false;
		}
	}
	//récupère une référence à partir du nom de l'objet référencé
	public Reference getReferenceByName(String name) {
		Reference ref = this.variables.get(name);
		if(ref == null) {
			ps.println(name+" n'existe pas");
		}
		return ref;
	}
	//supprime une référence de l'environnement
	public void removeReference(String name) {
		this.variables.remove(name);
	}
}
