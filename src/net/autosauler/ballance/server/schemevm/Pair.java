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

/**
 * The Class Pair.
 */
public class Pair {

	/** The car. */
	public Object car;

	/** The cdr. */
	public Object cdr;

	/**
	 * Instantiates a new pair.
	 * 
	 * @param head
	 *            the head
	 * @param rest
	 *            the rest
	 */
	public Pair(Object head, Object rest) {
		car = head;
		cdr = rest;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object other) {
		return (other instanceof Pair)
				&& (car == null ? ((Pair) other).car == null : car
						.equals(((Pair) other).car))
				&& (cdr == null ? ((Pair) other).cdr == null : cdr
						.equals(((Pair) other).cdr));
	}

	/**
	 * Rest.
	 * 
	 * @return the pair
	 */
	public Pair rest() {
		return (Pair) cdr;
	}
}
