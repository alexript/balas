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

//
// Library - a core concept in r6rs
// In sixx they are extendable - definitions can be
// updated, added and exported (but other modules won't get
// the new exports until they reimport)
//

package net.autosauler.ballance.server.schemevm;

import java.util.Hashtable;
import java.util.Vector;

/**
 * The Class Library.
 */
public final class Library {
	// list of exported symbols, the slot number can be looked up
	// in the symbol table
	/** The exports. */
	public Pair exports = null;
	// maps a symbol to a pair (slots . index), importing another library
	// adds its exports to the symbol table.
	/** The symbol table. */
	@SuppressWarnings("rawtypes")
	public Hashtable symbolTable = new Hashtable();
	// the top level slots
	/** The slots. */
	@SuppressWarnings("rawtypes")
	public Vector slots = new Vector();
	// The top level macros, maps a symbol onto a procedure. Macros can be
	// exported, in which case the exported symbol won't be in the symbolTable.
	/** The macros. */
	@SuppressWarnings("rawtypes")
	public Hashtable macros = new Hashtable();
}
