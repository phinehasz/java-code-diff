package com.github.phinehasz.diff;

import net.sourceforge.pmd.PMD;
import net.sourceforge.pmd.lang.Parser;
import net.sourceforge.pmd.lang.java.JavaLanguageModule;
import net.sourceforge.pmd.lang.java.ast.ASTCompilationUnit;
import net.sourceforge.pmd.lang.java.ast.Token;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Java Tokenizer based on PMD
 * @author zhhiyp
 * @version : JavaParser.java 2020-12-23 21:19
 */
public class JavaTokenizer {

	private static final Parser JAVA_PARSER = PMD.parserFor(new JavaLanguageModule().getVersion("1.8"), null);

	public static Token[] getTokenizedLines(ASTCompilationUnit javaNode) {
		List<Token> tokenList = new ArrayList<>();
		Token token = (Token)javaNode.jjtGetFirstToken();
		Token start = token;
		Token next = token;
		int line = token.beginLine;
		tokenList.add(start);
		while((next = next.next) != null){
			if(next.beginLine == line){
				start.image = start.image + next.image;
			}else {
				start = next;
				line = start.beginLine;
				tokenList.add(start);
			}
		}
		return tokenList.toArray(new Token[]{});
	}

	public static ASTCompilationUnit getJavaNode(String filePath) {
		File file = new File(filePath);
		try{
			return (ASTCompilationUnit)JAVA_PARSER.parse(filePath, new BufferedReader(new FileReader(file)));
		}catch (Throwable e){
			e.printStackTrace();
		}
		return null;
	}
}
