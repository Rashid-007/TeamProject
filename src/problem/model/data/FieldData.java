package problem.model.data;

import org.objectweb.asm.Type;

import problem.model.visit.IVisitor;
import problem.util.StringParser;

public class FieldData implements IFieldData {
	private String fieldName;
	private String access;
	private String type;
	
	public FieldData(String name, String access, Type type, String sig) {
		this.fieldName = name;
		this.access = access;
		if(sig!=null){
			this.type = StringParser.fieldTypeFromSignature(sig);
		}
		else{
			this.type = StringParser.parseClassName(type.getClassName());
		}
	}

	@Override
	public String getName() {
		return this.fieldName;
	}
	
	@Override
	public String getAccessLevel() {
		return this.access;
	}
	
	@Override
	public String getType() {
		return this.type;
	}
	
	@Override
	public String toString() {
		String result = this.access+" "+this.fieldName+" : ";
		return result + this.type + "\\l";
	}

	@Override
	public void setName(String nm) {
		this.fieldName = nm;
	}
	
	@Override
	public void accept(IVisitor v) {
		v.preVisit(this);
		v.visit(this);
		v.postVisit(this);
	}
}
