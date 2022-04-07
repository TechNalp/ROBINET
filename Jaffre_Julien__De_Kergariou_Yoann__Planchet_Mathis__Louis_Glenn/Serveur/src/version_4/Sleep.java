package version_4;

import stree.parser.SNode;

public class Sleep implements Command{
	@Override
	public Reference run(Reference receiver, SNode method) throws InterruptedException{
	
			Thread.sleep(Integer.valueOf(method.get(2).contents()));
		
		return null;
	}
}
