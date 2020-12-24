package com.github.phinehasz.diff;

import net.sourceforge.pmd.lang.java.ast.ASTCompilationUnit;
import net.sourceforge.pmd.lang.java.ast.Token;

import static com.github.phinehasz.diff.JavaTokenizer.getJavaNode;
import static com.github.phinehasz.diff.JavaTokenizer.getTokenizedLines;

/**
 * @author zhhiyp
 * @version : Main.java 2020-12-24 22:32
 */
public class Main {

	public static void main(String[] args){
		ASTCompilationUnit javaNode = getJavaNode("src\\test\\java\\resources\\TestOldJava.java");
		ASTCompilationUnit javaNode2 = getJavaNode("src\\test\\java\\resources\\TestOldJava2.java");

		Token[] tokenizedLines = getTokenizedLines(javaNode);
		Token[] tokenizedLines2 = getTokenizedLines(javaNode2);

		Diff diff = new Diff(tokenizedLines, tokenizedLines2);

		System.out.println(diff.getChangeLine());
	}
}
