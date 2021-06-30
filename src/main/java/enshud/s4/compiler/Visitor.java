package enshud.s4.compiler;

import java.util.ArrayList;



public interface Visitor {
	public VisitorInfo visit(Node node,ArrayList<VisitorInfo> viList) throws EnshudSemanticErrorException ;

	public void flagSet(Node node)throws EnshudSemanticErrorException;
}
