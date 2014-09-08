package org.peg4d;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.peg4d.ext.Generator;

public class CSourceGenerator extends Generator implements CTags {

	public CSourceGenerator(String outputFileName) {
		super(outputFileName);
	}

	public void writeC(ParsingObject pego) {
		System.out.println(pego);
		this.dispatch(pego);
		this.close();
	}

	protected void dispatch(ParsingObject po) {
		Method m = null;
		String name = "gen" + po.getTag().toString();
		try {
			m = this.getClass().getDeclaredMethod(name, ParsingObject.class);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		if(m != null) {
			try {
				m.invoke(this, po);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void genSource(ParsingObject pego) {
		for(int i = 0; i < pego.size(); i++) {
			ParsingObject p = pego.get(i);
			this.dispatch(p);
		}
	}

	@Override
	public void genFunction(ParsingObject pego) {
		System.out.println(pego);
	}

	@Override
	public void genTypeDeclaration(ParsingObject pego) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void genVarDecl(ParsingObject pego) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void genDesignation(ParsingObject pego) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void genTInt(ParsingObject pego) {
		this.write("int");
	}

	@Override
	public void genTStruct(ParsingObject pego) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void genTUnion(ParsingObject pego) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void genTConst(ParsingObject pego) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void genTFunc(ParsingObject pego) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void genTVoid(ParsingObject pego) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void genTPointer(ParsingObject pego) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void genTEnum(ParsingObject pego) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void genTBoolean(ParsingObject pego) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void genTComplex(ParsingObject pego) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void genTName(ParsingObject pego) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void genBit(ParsingObject pego) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void genList(ParsingObject pego) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void genBlock(ParsingObject pego) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void genIf(ParsingObject pego) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void genWhile(ParsingObject pego) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void genDoWhile(ParsingObject pego) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void genFor(ParsingObject pego) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void genGoto(ParsingObject pego) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void genContinue(ParsingObject pego) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void genBreak(ParsingObject pego) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void genReturn(ParsingObject pego) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void genSwitch(ParsingObject pego) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void genSwitchCase(ParsingObject pego) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void genSwitchDefault(ParsingObject pego) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void genLabelBlock(ParsingObject pego) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void genEmpty(ParsingObject pego) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void genAssign(ParsingObject pego) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void genAssignMul(ParsingObject pego) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void genAssignDiv(ParsingObject pego) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void genAssignMod(ParsingObject pego) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void genAssignAdd(ParsingObject pego) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void genAssignSub(ParsingObject pego) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void genAssignLeftShift(ParsingObject pego) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void genAssignRightShift(ParsingObject pego) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void genAssignBitwiseAnd(ParsingObject pego) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void genAssignBitwiseOr(ParsingObject pego) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void genAssignBitwiseXOr(ParsingObject pego) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void genPrefixInc(ParsingObject pego) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void genPrefixDec(ParsingObject pego) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void genAddress(ParsingObject pego) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void genStar(ParsingObject pego) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void genPlus(ParsingObject pego) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void genMinus(ParsingObject pego) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void genCompl(ParsingObject pego) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void genNot(ParsingObject pego) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void genSizeOf(ParsingObject pego) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void genCast(ParsingObject pego) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void genField(ParsingObject pego) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void genInc(ParsingObject pego) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void genDec(ParsingObject pego) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void genIndex(ParsingObject pego) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void genApply(ParsingObject pego) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void genDeclaration(ParsingObject pego) {
		this.dispatch(pego.get(0));
		this.dispatch(pego.get(1));
	}

}
