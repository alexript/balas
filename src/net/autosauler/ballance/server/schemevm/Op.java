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
// Op class, stores information on how to retrieve the next argument.
// In a pure CPS VM specifying arguments are the only operations...
//

package net.autosauler.ballance.server.schemevm;

import java.util.Vector;

/**
 * The Class Op.
 */
public final class Op {

	/** The type. */
	public int type;

	/** The p2. */
	public int p1, p2;

	/** The lib slots. */
	@SuppressWarnings("rawtypes")
	public Vector libSlots;

	/** The proc. */
	public Procedure proc;

	/** The literal. */
	public Object literal;

	/**
	 * Instantiates a new op.
	 * 
	 * @param type
	 *            the type
	 * @param p1
	 *            the p1
	 * @param p2
	 *            the p2
	 * @param libSlots
	 *            the lib slots
	 * @param proc
	 *            the proc
	 * @param literal
	 *            the literal
	 */
	@SuppressWarnings("rawtypes")
	public Op(int type, int p1, int p2, Vector libSlots, Procedure proc,
			Object literal) {
		this.type = type;
		this.p1 = p1;
		this.p2 = p2;
		this.libSlots = libSlots;
		this.proc = proc;
		this.literal = literal;
	}
}
