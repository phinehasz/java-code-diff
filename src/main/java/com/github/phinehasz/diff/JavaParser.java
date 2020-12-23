package com.github.phinehasz.diff;

import net.sourceforge.pmd.PMD;
import net.sourceforge.pmd.lang.Parser;
import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.java.JavaLanguageModule;
import net.sourceforge.pmd.lang.java.ast.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * JAVA PARSER BASED ON PMD AST
 * HOW TO IMPROVE THE ALGORITHM?
 * @see JavaParser#getEveryLineCodeAst(net.sourceforge.pmd.lang.java.ast.ASTCompilationUnit)
 * @see JavaParser#equals(net.sourceforge.pmd.lang.ast.Node, net.sourceforge.pmd.lang.ast.Node)
 * @author zhhiyp
 * @version : JavaParser.java 2020-12-23 21:19
 */
public class JavaParser {

	private static final Parser JAVA_PARSER = PMD.parserFor(new JavaLanguageModule().getVersion("1.8"), null);

	public static void main(String[] args){
		Node javaNode = getJavaNode("G:\\java\\me\\AstDiff\\src\\test\\java\\resources\\TestOldJava.java");
		Node javaNode2 = getJavaNode("G:\\java\\me\\AstDiff\\src\\test\\java\\resources\\TestOldJava2.java");
		// TEST
		Node[] tpl = getEveryLineCodeAst((ASTCompilationUnit) javaNode2);
		Node[] str = getEveryLineCodeAst((ASTCompilationUnit) javaNode);
		List<LcsAnalyzer.AstDiffNode> astDiffNodes = LcsAnalyzer.nodeListLcs(tpl, str, 0);
		for (LcsAnalyzer.AstDiffNode node : astDiffNodes) {
			if (node.op == 1) {
				System.out.println("same:" + toString(substring(str,node.startPosition, node.startPosition + node.length)));
			} else if (node.op == 2) {
				System.out.println("++:" + toString(substring(str,node.startPosition, node.startPosition + node.length)));
			} else if (node.op == 3) {
				System.out.println("--:" + toString(node.subTpl));
			}
		}
	}

	/**
	 * FIXME:It depends which node join the compare array
	 * target: every line code is made as AST, then add it.
	 * @param unit
	 * @return compare array
	 */
	public static Node[] getEveryLineCodeAst(ASTCompilationUnit unit){
		List<Node> nodeList = new ArrayList<>();
		nodeList.addAll(unit.findDescendantsOfType(ASTPackageDeclaration.class));
		nodeList.addAll(unit.findDescendantsOfType(ASTImportDeclaration.class));
		nodeList.addAll(unit.findDescendantsOfType(ASTClassOrInterfaceDeclaration.class));
		nodeList.addAll(unit.findDescendantsOfType(ASTEnumDeclaration.class));
		nodeList.addAll(unit.findDescendantsOfType(ASTClassOrInterfaceBodyDeclaration.class));
		nodeList.addAll(unit.findDescendantsOfType(ASTMethodDeclaration.class));
		nodeList.addAll(unit.findDescendantsOfType(ASTBlockStatement.class));
		return nodeList.toArray(new Node[]{});
	}

	/**
	 * compare two ast whether is same
	 * FIXME MAYBE SHALL USE GENERIC TOKEN IN ASTNODE!
	 * @param oldBlockStat it shall be one line code(THIS IS BEST!)
	 * @param newBlockStat
	 * @return isEqual
	 */
	public static boolean equals(Node oldBlockStat, Node newBlockStat) {
		int oldNum = oldBlockStat.jjtGetNumChildren();
		int newNum = newBlockStat.jjtGetNumChildren();
		if(oldNum != newNum){
			return false;
		}

		// check child List
		for(int i = 0; i < oldNum; i++){
			if(!equals(oldBlockStat.jjtGetChild(i), newBlockStat.jjtGetChild(i))){
				return false;
			}
		}

		// check self
		if(oldBlockStat.getImage() == null && newBlockStat.getImage() == null){
			return true;
		}
		if(oldBlockStat.getImage() == null || newBlockStat.getImage() == null){
			return false;
		}

		return oldBlockStat.getImage().equals(newBlockStat.getImage());
	}

	public static String toString(Node[] nodes){
		StringBuilder buf = new StringBuilder();
		for (Node node : nodes) {
			buf.append(toString(node));
		}
		return buf.toString();
	}

	public static String toString(Node node){
		StringBuilder sb = new StringBuilder();
		for(int i=0;i<node.jjtGetNumChildren();i++){
			sb.append(toString(node.jjtGetChild(i)));
		}
		sb.append(node.getImage()==null?"":node.getImage()).append(" ");
		return sb.toString();
	}

	public static Node[] substring(Node[] original, int start, int dst){
		if(start < 0){
			throw new IllegalArgumentException("error start position");
		}
		return Arrays.copyOfRange(original, start, dst);
	}

	public static Node[] substring(Node[] original, int start){
		return Arrays.copyOfRange(original, start, original.length);
	}

	public static Node getJavaNode(String filePath) {
		File file = new File(filePath);
		try{
			return JAVA_PARSER.parse(filePath, new BufferedReader(new FileReader(file)));
		}catch (Throwable e){
			e.printStackTrace();
		}
		return null;
	}
}
