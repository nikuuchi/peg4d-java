package org.peg4d;

public interface CTags {

	abstract void genSource(ParsingObject pego);
	abstract void genFunction(ParsingObject pego);
	abstract void genDeclaration(ParsingObject pego);
	abstract void genTypeDeclaration(ParsingObject pego);
	abstract void genVarDecl(ParsingObject pego);
	abstract void genDesignation(ParsingObject pego);

	abstract void genTInt(ParsingObject pego);
	abstract void genTStruct(ParsingObject pego);
	abstract void genTUnion(ParsingObject pego);
	abstract void genTConst(ParsingObject pego);
	abstract void genTFunc(ParsingObject pego);
	abstract void genTVoid(ParsingObject pego);
	abstract void genTPointer(ParsingObject pego);
	abstract void genTEnum(ParsingObject pego);
	abstract void genTBoolean(ParsingObject pego);
	abstract void genTComplex(ParsingObject pego);
	abstract void genTName(ParsingObject pego);
	abstract void genName(ParsingObject pego);

	abstract void genBit(ParsingObject pego);
	abstract void genList(ParsingObject pego);

	abstract void genBlock(ParsingObject pego);

	abstract void genInteger(ParsingObject pego);

	abstract void genIf(ParsingObject pego);
	abstract void genWhile(ParsingObject pego);
	abstract void genDoWhile(ParsingObject pego);
	abstract void genFor(ParsingObject pego);
	abstract void genGoto(ParsingObject pego);
	abstract void genContinue(ParsingObject pego);
	abstract void genBreak(ParsingObject pego);
	abstract void genReturn(ParsingObject pego);

	abstract void genSwitch(ParsingObject pego);
	abstract void genSwitchCase(ParsingObject pego);
	abstract void genSwitchDefault(ParsingObject pego);

	abstract void genLabelBlock(ParsingObject pego);
	abstract void genEmpty(ParsingObject pego);

	abstract void genLessThan(ParsingObject pego);

	abstract void genAssign(ParsingObject pego);
	abstract void genAssignMul(ParsingObject pego);
	abstract void genAssignDiv(ParsingObject pego);
	abstract void genAssignMod(ParsingObject pego);
	abstract void genAssignAdd(ParsingObject pego);
	abstract void genAssignSub(ParsingObject pego);
	abstract void genAssignLeftShift(ParsingObject pego);
	abstract void genAssignRightShift(ParsingObject pego);
	abstract void genAssignBitwiseAnd(ParsingObject pego);
	abstract void genAssignBitwiseOr(ParsingObject pego);
	abstract void genAssignBitwiseXOr(ParsingObject pego);

	abstract void genPrefixInc(ParsingObject pego);
	abstract void genPrefixDec(ParsingObject pego);
	abstract void genAddress(ParsingObject pego);
	abstract void genStar(ParsingObject pego);
	abstract void genPlus(ParsingObject pego);
	abstract void genMinus(ParsingObject pego);
	abstract void genCompl(ParsingObject pego);
	abstract void genNot(ParsingObject pego);
	abstract void genSizeOf(ParsingObject pego);

	abstract void genCast(ParsingObject pego);
	abstract void genField(ParsingObject pego);
	abstract void genInc(ParsingObject pego);
	abstract void genDec(ParsingObject pego);
	abstract void genIndex(ParsingObject pego);
	abstract void genApply(ParsingObject pego);
}
