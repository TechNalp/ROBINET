package version_5.copy;

import java.io.PrintStream;
import java.util.HashMap;
//ensemble de r�f�rence des �l�ments d'un espace graphique
public class Environment {
	HashMap<String,Reference> variables;
	PrintStream ps;
	public Environment(PrintStream ps){
		this.ps=ps;
		this.variables = new HashMap<String,Reference>();
	}
	//ajoute une r�f�rence � l'environnement
	public boolean addReference(String name, Reference ref) {
		if(!this.variables.containsKey(name)) {
			this.variables.put(name, ref);
			return true;
		}else{
			ps.println("La r�f�rence "+name+" existe d�j�");
			return false;
		}
	}
	//r�cup�re une r�f�rence � partir du nom de l'objet r�f�renc�
	public Reference getReferenceByName(String name) {
		Reference ref = this.variables.get(name);
		if(ref == null) {
			ps.println(name+" n'existe pas");
		}
		return ref;
	}
	//supprime une r�f�rence de l'environnement
	public void removeReference(String name) {
		this.variables.remove(name);
	}
}
