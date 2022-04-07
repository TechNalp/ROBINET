package version_6.copy;

import stree.parser.SNode;

public class Sleep implements Command{
	@Override
	public Reference run(Reference receiver, SNode method) throws StopException{
		try {
			Thread.sleep(Integer.valueOf(method.get(2).contents()));
		}catch(InterruptedException e) {
			method.children().clear();
			System.out.println("Un sleep à été interrompu par un stop");
			throw new StopException();
		}
		return null;
	}
	
	class StopException extends Exception{
		
	}
}
