package version_6.copy;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import stree.parser.SDefaultNode;
import stree.parser.SNode;
import version_6.copy.Sleep.StopException;

public class Script implements Command {

	SNode script;
	String self;
	int paramsNumber = 0;
	
	public Script(String self, SNode script) {
		this.script = script;
		this.self = self;
		this.paramsNumber = script.get(0).size()-1;
	}
	
	public void copyTree(SNode node,SNode tree,LinkedHashMap<String,String> params) {
		for(SNode nd : node.children()) {
			if(nd.isLeaf()) {
				SNode newNode = new SDefaultNode();
				tree.addChild(newNode);
				newNode.setParent(tree);
				if(params.containsKey(nd.contents())) {
					newNode.setContents(params.get(nd.contents()));
				}else {
					if(nd.contents().contains("self")) {
						newNode.setContents(nd.contents().replaceAll("self", params.get("self")));
					}else {
						newNode.setContents(nd.contents());
					}
				}

			}else {
				SNode newNode = new SDefaultNode();
				tree.addChild(newNode);
				newNode.setParent(tree);
				this.copyTree(nd,newNode,params);
			}
		}
	}
	
	@Override
	public Reference run(Reference receiver, SNode method) throws StopException {
		
		if(method.size()-2 != this.paramsNumber) {
			receiver.hisEnv.ps.println("Nombre d'arguement invalide ("+this.paramsNumber+" attendus, "+(method.size()-2)+" reçu)");
			return null;
		}
		
		LinkedHashMap<String,String> param_Value = new LinkedHashMap<>();
		
		param_Value.put(this.script.get(0).get(0).contents(), this.self);
		
		if(method.size()>2) {
			int i = 2;
			
			for(SNode node : this.script.get(0).children().subList(1, this.script.get(0).size())) {
				param_Value.put(node.contents(), method.get(i).contents());
				i++;
			}
		}
		
		
		SNode rootCopy = new SDefaultNode();
		this.copyTree(this.script, rootCopy,param_Value);
		List<SNode> copy = rootCopy.children().subList(1, rootCopy.size());
		
		
		Iterator<SNode> iterator = copy.iterator();
		
		while(iterator.hasNext()) {
			new Interpreter().compute(receiver.getGlobalEnv(), iterator.next());
		}
		
		
		return null;
	}

}
