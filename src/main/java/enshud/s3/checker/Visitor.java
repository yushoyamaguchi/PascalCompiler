package enshud.s3.checker;

import java.util.ArrayList;

public interface Visitor {
	public VisitorInfo visit(Node node,ArrayList<VisitorInfo> viList) throws EnshudSemanticErrorException ;

	public void flagSet(Node node)throws EnshudSemanticErrorException;

}
