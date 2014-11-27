package com.puresoltechnologies.parsers.parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.puresoltechnologies.parsers.grammar.Grammar;
import com.puresoltechnologies.parsers.grammar.GrammarException;

public class ParserManager {

	private static final Logger logger = LoggerFactory
			.getLogger(ParserManager.class);

	public static void storeParser(File directory, String name, Parser parser)
			throws IOException {
		persist(parser, new File(directory, name + ".persist"));
	}

	public static Parser loadParser(File directory, String name)
			throws IOException {
		return (Parser) restore(new File(directory, name + ".persist"));
	}

	public static Parser getManagerParser(File directory, String name,
			Grammar grammar) throws GrammarException {
		logger.debug("Look for manager parser '" + name + "' in directory '"
				+ directory + "'...");
		try {
			Parser parser = loadParser(directory, name);
			logger.debug("Parser '" + name + "' was successfully loaded!");
			return parser;
		} catch (IOException e) {
			logger.debug("Parser '" + name + "' not available, yet.");
		}
		Parser parser = null;
		try {
			parser = ParserFactory.create(grammar);
		} catch (ParserFactoryException e) {
			throw new GrammarException(
					"Grammar does not include information about the needed parser in parser-key!");
		}
		try {
			storeParser(directory, name, parser);
		} catch (IOException e) {
			logger.warn(
					"Newly created managed parser '" + name
							+ "' could not be stored in directory '"
							+ directory + "'!", e);
		}
		return parser;
	}

	private static <T> void persist(T object, File file) throws IOException {
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(
				new FileOutputStream(file));
		try {
			objectOutputStream.writeObject(object);
		} finally {
			objectOutputStream.close();
		}
	}

	private static <T> T restore(File file) throws FileNotFoundException,
			IOException {
		ObjectInputStream objectOutputStream = new ObjectInputStream(
				new FileInputStream(file));
		try {
			@SuppressWarnings("unchecked")
			T t = (T) objectOutputStream.readObject();
			return t;
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Could not restore class from file '"
					+ file + "'!", e);
		} finally {
			objectOutputStream.close();
		}
	}

}
