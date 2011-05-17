/*
 * Copyright (c) 2006 James Bailey (dgym.REMOVE_THIS.bailey@gmail.com).
 *
 * Permission is hereby granted, free of charge, to any person obtaining a
 * copy of this software and associated documentation files (the "Software"), to
 * deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or
 * sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package net.autosauler.ballance.server.schemevm;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * The Class Reader.
 */
public class Reader {
	// Reads scheme
	/** The stream. */
	protected BufferedReader stream;

	/** The line. */
	public int line = 1;

	/** The column. */
	public int column = 1;

	/**
	 * Instantiates a new reader.
	 * 
	 * @param reader
	 *            the reader
	 */
	public Reader(java.io.BufferedReader reader) {
		stream = reader;
	}

	/**
	 * Instantiates a new reader.
	 * 
	 * @param reader
	 *            the reader
	 */
	public Reader(java.io.Reader reader) {
		stream = new BufferedReader(reader);
	}

	/**
	 * Peek.
	 * 
	 * @return the char
	 * @throws Exception
	 *             the exception
	 */
	protected char peek() throws Exception {
		char c, t;

		// skip whitespace and comments
		for (;;) {
			stream.mark(2);
			c = readChar();

			if (Character.isWhitespace(c)) {
				continue;
			} else if (c == ';') {
				while ((c != '\r') && (c != '\n')) {
					c = readChar();
				}
			} else if (c == '#') {
				t = readChar();
				if (t == ';') {
					read();
				} else if (t == '|') // nestable block comment
				{
					int depth = 1;
					while (depth > 0) {
						t = readChar();
						if ((t == '#') && (readChar() == '|')) {
							++depth;
						} else if ((t == '|') && (readChar() == '#')) {
							--depth;
						}
					}
				} else {
					break;
				}
			} else {
				break;
			}
		}

		stream.reset();
		return c;
	}

	/**
	 * Read.
	 * 
	 * @return the object
	 * @throws Exception
	 *             the exception
	 */
	public Object read() throws Exception {
		char c = peek();
		stream.skip(1);

		if (c == '#') {
			// true, false, vectors, characters, hexadecimal or a comment
			stream.mark(1);
			c = readChar();
			if (c == 't') {
				return Boolean.TRUE;
			} else if (c == 'f') {
				return Boolean.FALSE;
			} else if (c == '\\') {
				StringBuffer chars = new StringBuffer();
				chars.append(readChar());
				for (;;) {
					stream.mark(1);
					c = readChar();
					if (!Character.isLetter(c)) {
						stream.reset();

						String charname = chars.toString();
						if (charname.equalsIgnoreCase("space")) {
							c = ' ';
						} else if (charname.equalsIgnoreCase("tab")) {
							c = '\t';
						} else if (charname.equalsIgnoreCase("newline")
								|| charname.equalsIgnoreCase("linefeed")) {
							c = '\n';
						} else if (charname.equalsIgnoreCase("return")) {
							c = '\r';
						} else {
							c = charname.charAt(0);
						}

						return new Character(c);
					}
					chars.append(c);
				}
			} else if (c == '(') {
				stream.reset();
				return Sixx.makeArray(java.lang.Object.class, (Pair) read());
			} else if (c == 'x') {
				StringBuffer buf = new StringBuffer();
				for (;;) {
					stream.mark(1);
					c = readChar();
					if (Character.isWhitespace(c)) {
						break;
					}
					if ((c == '(') || (c == ')')) {
						stream.reset();
						break;
					}
					buf.append(c);
				}

				return new Integer(Integer.parseInt(buf.toString(), 16));
			}
		} else if (c == '"') {
			// string
			StringBuffer buf = new StringBuffer();
			while ((c = readChar()) != '"') {
				if (c == '\\') {
					c = readChar();
					if (c == 'n') {
						buf.append('\n');
					} else {
						buf.append(c);
					}
				} else {
					buf.append(c);
				}
			}
			return buf.toString().toCharArray();
		} else if (c == '(') {
			// list
			int sline = line;
			int scol = column;
			try {
				if (peek() == ')') {
					stream.skip(1);
					return null;
				}

				Object atom = read();
				Pair list = new Pair(atom, null);
				Pair lastPair = list;

				while (peek() != ')') {
					atom = read();
					if (atom == ".") {
						lastPair.cdr = read();
					} else {
						lastPair.cdr = new Pair(atom, null);
						lastPair = (Pair) lastPair.cdr;
					}
				}
				stream.skip(1);

				return list;
			} catch (IOException e) {
				System.err.println("Unclosed list starting at line " + sline
						+ " column " + scol);
				throw e;
			}
		} else if (c == '\'') {
			return new Pair("quote", new Pair(read(), null));
		} else if (c == '`') {
			return new Pair("quasiquote", new Pair(read(), null));
		} else if (c == ',') {
			if (peek() == '@') {
				readChar();
				return new Pair("unquote-splicing", new Pair(read(), null));
			}
			return new Pair("unquote", new Pair(read(), null));
		} else {
			// symbol or number
			StringBuffer buf = new StringBuffer();
			int b;
			for (;;) {
				buf.append(c);
				stream.mark(1);
				b = readByte();
				if (b == -1) {
					break;
				}
				c = (char) b;
				if (Character.isWhitespace(c)) {
					break;
				}
				if ((c == '(') || (c == ')')) {
					stream.reset();
					break;
				}
			}

			try {
				return new Integer(buf.toString());
			} catch (NumberFormatException e) {
			}

			try {
				return new Double(buf.toString());
			} catch (NumberFormatException e) {
			}

			return buf.toString().intern();
		}

		throw new IOException();
	}

	/**
	 * Read byte.
	 * 
	 * @return the int
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public int readByte() throws IOException {
		int i = stream.read();
		if (i == '\n') {
			++line;
			column = 1;
		} else {
			++column;
		}
		return i;
	}

	/**
	 * Read char.
	 * 
	 * @return the char
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public char readChar() throws IOException {
		int i = stream.read();
		if (i == -1) {
			throw new IOException();
		}
		if (i == '\n') {
			++line;
			column = 1;
		} else {
			++column;
		}
		return (char) i;
	}
}
