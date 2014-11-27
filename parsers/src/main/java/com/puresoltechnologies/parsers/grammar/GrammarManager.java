package com.puresoltechnologies.parsers.grammar;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;

import com.puresoltechnologies.parsers.lexer.Lexer;
import com.puresoltechnologies.parsers.lexer.LexerFactory;
import com.puresoltechnologies.parsers.lexer.LexerFactoryException;
import com.puresoltechnologies.parsers.parser.Parser;
import com.puresoltechnologies.parsers.parser.ParserFactory;
import com.puresoltechnologies.parsers.parser.ParserFactoryException;

/**
 * This class manages a single grammar with all its lexer and parser facilities.
 * It's needed due to an excessive calculation effort for parser table
 * construction.
 * 
 * @author Rick-Rainer Ludwig
 * 
 */
public class GrammarManager {

	public static String getPersistedGrammarPath(String grammarPath) {
		return grammarPath + ".persist";
	}

	public static String getPersistedLexerPath(String grammarPath) {
		return grammarPath + ".lexer.persist";
	}

	public static String getPersistedParserPath(String grammarPath) {
		return grammarPath + ".parser.persist";
	}

	private final URL grammarURL;
	private final File grammarPersistencePath;
	private final File lexerPersistencePath;
	private final File parserPersistencePath;

	private Grammar grammar = null;
	private Lexer lexer = null;
	private Parser parser = null;

	public GrammarManager(URL grammarURL) {
		super();
		this.grammarURL = grammarURL;
		this.grammarPersistencePath = new File(
				getPersistedGrammarPath(grammarURL.getPath()));
		this.lexerPersistencePath = new File(
				getPersistedLexerPath(grammarURL.getPath()));
		this.parserPersistencePath = new File(
				getPersistedParserPath(grammarURL.getPath()));
	}

	/**
	 * @return the grammarFile
	 */
	public URL getGrammarURL() {
		return grammarURL;
	}

	/**
	 * @return the grammarPersistenceFile
	 */
	public File getGrammarPersistenceFile() {
		return grammarPersistencePath;
	}

	/**
	 * @return the lexerPersistenceFile
	 */
	public File getLexerPersistenceFile() {
		return lexerPersistencePath;
	}

	/**
	 * @return the parserPersistenceFile
	 */
	public File getParserPersistenceFile() {
		return parserPersistencePath;
	}

	public Grammar getGrammar() throws IOException, GrammarException {
		if ((!grammarPersistencePath.exists())
				|| (grammarModified() > grammarPersistencePath.lastModified())) {
			updateGrammarPersistenceFile();
		} else if (grammar == null) {
			readGrammarPersistenceFile();
		}
		return grammar;
	}

	private void updateGrammarPersistenceFile() throws IOException,
			GrammarException {
		InputStream inputStream = null;
		try {
			if ((!grammarPersistencePath.exists())
					|| (grammarModified() > grammarPersistencePath
							.lastModified())) {
				inputStream = grammarURL.openStream();
				GrammarReader reader = new GrammarReader(inputStream);
				try {
					grammar = reader.getGrammar();
					ObjectOutputStream objectOutputStream = new ObjectOutputStream(
							new FileOutputStream(grammarPersistencePath));
					try {
						objectOutputStream.writeObject(grammar);
					} finally {
						objectOutputStream.close();
					}
				} finally {
					reader.close();
				}
			}
		} finally {
			if (inputStream != null) {
				inputStream.close();
			}
		}
	}

	private void readGrammarPersistenceFile() throws IOException,
			GrammarException {
		try {
			grammar = (Grammar) restore(grammarPersistencePath);
		} catch (FileNotFoundException e) {
			updateGrammarPersistenceFile();
		}
	}

	public Lexer getLexer() throws IOException, GrammarException,
			LexerFactoryException {
		if ((!lexerPersistencePath.exists())
				|| (grammarModified() > lexerPersistencePath.lastModified())) {
			updateGrammarPersistenceFile();
			updateLexerPersistenceFile();
		} else if (lexer == null) {
			readLexerPersistenceFile();
		}
		return lexer;
	}

	private void updateLexerPersistenceFile() throws IOException,
			GrammarException, LexerFactoryException {
		if (grammar == null) {
			readGrammarPersistenceFile();
		}
		lexer = LexerFactory.create(grammar);
		persist(lexer, lexerPersistencePath);
	}

	private void readLexerPersistenceFile() throws IOException,
			GrammarException, LexerFactoryException {
		try {
			lexer = (Lexer) restore(lexerPersistencePath);
		} catch (FileNotFoundException e) {
			updateLexerPersistenceFile();
		}
	}

	public Parser getParser() throws IOException, GrammarException,
			ParserFactoryException {
		if ((!parserPersistencePath.exists())
				|| (grammarModified() > parserPersistencePath.lastModified())) {
			updateGrammarPersistenceFile();
			updateParserPersistenceFile();
		} else if (parser == null) {
			readParserPersistenceFile();
		}
		return parser;
	}

	private void updateParserPersistenceFile() throws IOException,
			GrammarException, ParserFactoryException {
		if (grammar == null) {
			readGrammarPersistenceFile();
		}
		parser = ParserFactory.create(grammar);
		persist(parser, parserPersistencePath);
	}

	private void readParserPersistenceFile() throws IOException,
			GrammarException, ParserFactoryException {
		try {
			parser = restore(parserPersistencePath);
		} catch (FileNotFoundException e) {
			updateParserPersistenceFile();
		}
	}

	private long grammarModified() {
		if ("file".equals(grammarURL.getProtocol())) {
			return new File(grammarURL.getFile()).lastModified();
		}
		return System.currentTimeMillis();
	}

	private <T> void persist(T object, File file) throws IOException {
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(
				new FileOutputStream(file));
		try {
			objectOutputStream.writeObject(object);
		} finally {
			objectOutputStream.close();
		}
	}

	private <T> T restore(File file) throws FileNotFoundException, IOException {
		ObjectInputStream objectOutputStream = new ObjectInputStream(
				new FileInputStream(file));
		try {
			@SuppressWarnings("unchecked")
			T t = (T) objectOutputStream.readObject();
			return t;
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Could not restore class from '" + file
					+ "'!", e);
		} finally {
			objectOutputStream.close();
		}
	}

}
