package version_6.copy;

import java.awt.Color;

import graphicLayer.GElement;
import graphicLayer.GSpace;
import stree.parser.SNode;

public class SetColor implements Command{
	
	@Override
	public Reference run(Reference receiver, SNode method) {
		try {
			if(receiver.getReceiver() instanceof GElement){
				((GElement)(receiver.getReceiver())).setColor((Color)(Color.class.getDeclaredField(method.get(2).contents()).get(null)));
			}else {
				
					((GSpace)(receiver.getReceiver())).setColor((Color)(Color.class.getDeclaredField(method.get(2).contents()).get(null)));
				
				
			}
		} catch (IllegalArgumentException | IllegalAccessException | SecurityException e) {
			e.printStackTrace();
		}catch(NoSuchFieldException e) {
			receiver.hisEnv.ps.println(e.getMessage()+" n'est pas une couleur valide");
			return null;
		}
		return null;
	}

}
