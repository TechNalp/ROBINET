package version_6.copy;

import stree.parser.SNode;

public class AddScript implements Command {

	@Override
	public Reference run(Reference receiver, SNode method) {
		String scriptName = method.get(2).contents();
		String receiverName = method.get(0).contents();
		
		if(receiver.getCommandByName(scriptName)!=null) {
			receiver.hisEnv.ps.println("Impossible d'ajouter un script avec le nom \""+ scriptName+"\" car "+receiverName+" possède déjà une commande ou un script avec ce nom");
			return null;
		}
		
		receiver.addCommand(scriptName, new Script(receiverName,method.get(3)));
		return null;
	}
}
