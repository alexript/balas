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
// ArgLists are used to iterate over arguments, whether they are a list
// of immediate values, or a list of Ops that must be run.
//

package net.autosauler.ballance.server.schemevm;

/**
 * The Class ArgList.
 */
public final class ArgList {

	/** The args. */
	public Pair args;

	/** The curr args. */
	public Object[] currArgs;

	/** The curr env. */
	public Pair currEnv;

	/** The immediates. */
	public boolean immediates = false;

	/** The next. */
	public Pair next = new Pair(null, null);

	/**
	 * Next.
	 * 
	 * @return the object
	 * @throws Exception
	 *             the exception
	 */
	public final Object next() throws Exception {
		Object car = args.car;
		args = args.rest();
		if (immediates) {
			// this is the result of a builtin
			return car;
		}

		Op arg = (Op) car;
		switch (arg.type) {
		case Sixx.OP_ENV: {
			int up = arg.p1;
			Pair env = currEnv;
			while (--up >= 0) {
				env = env.rest();
			}
			return ((Object[]) env.car)[arg.p2];
		}
		case Sixx.OP_LIB:
			return arg.libSlots.elementAt(arg.p1);
		case Sixx.OP_ARG:
			return currArgs[arg.p1];
		case Sixx.OP_LMBD: {
			Procedure lcl = new Procedure();
			Procedure templ = arg.proc;
			lcl.flags = templ.flags;
			lcl.args = templ.args;
			lcl.env_size = templ.env_size;
			lcl.env = currEnv;
			lcl.name = templ.name;
			lcl.mappings = templ.mappings;
			return lcl;
		}
		case Sixx.OP_LIT:
			return arg.literal;
		default:
			throw new Exception("invalid Op");
		}
	}

	/**
	 * Sets the next.
	 * 
	 * @param result
	 *            the new next
	 */
	public final void setNext(Object result) {
		next.car = result;
		args = next;
		immediates = true;
	}
}
