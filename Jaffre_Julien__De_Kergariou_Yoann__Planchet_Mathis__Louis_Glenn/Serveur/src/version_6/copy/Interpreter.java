package version_6.copy;

import stree.parser.SNode;
import version_6.copy.Sleep.StopException;

class Interpreter {
	public void compute(Environment env, SNode method) throws StopException {
		if(method.size()<=0) {
			return;
		}
		String receiverName = method.get(0).contents();
		Reference receiver = env.getReferenceByName(receiverName);
		if(receiver != null) {
			receiver.run(method);
			
		}else {
			env.ps.println(receiverName+" est inconnu");
		}
	}
}
