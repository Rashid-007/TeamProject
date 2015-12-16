package problem.asm;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

public class ClassMethodVisitor extends ClassVisitor {
	private ClassData classData;
	public ClassMethodVisitor(int api) {
		super(api);
		this.classData = new ClassData();
	}

	public ClassMethodVisitor(int api, ClassVisitor decorated) {
		super(api, decorated);
		this.classData = new ClassData();
	}
	
	public ClassMethodVisitor(int api, ClassDeclarationVisitor decorated) {
		super(api, decorated);
		this.classData = decorated.getClassData();
	}

	@Override
	public MethodVisitor visitMethod(int access, String name, String desc,
			String signature, String[] exceptions) {
		MethodVisitor toDecorate = super.visitMethod(access, name, desc,
				signature, exceptions);
		
		System.out.println("------------------------------");
		// TODO: delete the line below
		System.out.println("method " + name);
		addAccessLevel(access);
		addReturnType(desc);
		addArguments(desc);
		String type= Type.getType(desc).getClassName();
		this.classData.addMethod(new MethodData(name, type, access));
		return toDecorate;
	}

	void addAccessLevel(int access) {
		String level = "";
		if ((access & Opcodes.ACC_PUBLIC) != 0) {
			level = "public";
		} else if ((access & Opcodes.ACC_PROTECTED) != 0) {
			level = "protected";
		} else if ((access & Opcodes.ACC_PRIVATE) != 0) {
			level = "private";
		} else {
			level = "default";
		}
		// TODO: delete the next line
		System.out.println("access level: " + level);
		// TODO: ADD this information to your representation of the current method.
	}

	void addReturnType(String desc) {
		String returnType = Type.getReturnType(desc).getClassName();
		// TODO: delete the next line
		System.out.println("return type: " + returnType);
		// TODO: ADD this information to your representation of the current method.
	}

	void addArguments(String desc) {
		Type[] args = Type.getArgumentTypes(desc);
		for (int i = 0; i < args.length; i++) {
			String arg = args[i].getClassName();
			// TODO: delete the next line
			System.out.println("arg " + i + ": " + arg);
			// TODO: ADD this information to your representation of the current method.
		}
	}

	public ClassData getClassData() {
		return this.classData;
	}

}