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
 * The Class Procedure.
 */
public class Procedure {

	/** The name. */
	public String name;

	/** The flags. */
	public int flags;

	/** The idx. */
	public int idx;

	/** The args. */
	public int args;

	/** The env_size. */
	public int env_size;

	/** The env. */
	public Pair env;

	/** The mappings. */
	public Pair mappings;

	/**
	 * Instantiates a new procedure.
	 */
	public Procedure() {
	}

	/**
	 * Instantiates a new procedure.
	 * 
	 * @param flags
	 *            the flags
	 * @param idx
	 *            the idx
	 * @param args
	 *            the args
	 * @param env_size
	 *            the env_size
	 * @param env
	 *            the env
	 * @param name
	 *            the name
	 * @param mappings
	 *            the mappings
	 */
	public Procedure(int flags, int idx, int args, int env_size, Pair env,
			String name, Pair mappings) {
		this.flags = flags;
		this.idx = idx;
		this.args = args;
		this.env_size = env_size;
		this.env = env;
		this.name = name;
		this.mappings = mappings;
	}

	/**
	 * Instantiates a new procedure.
	 * 
	 * @param flags
	 *            the flags
	 * @param args
	 *            the args
	 * @param env_size
	 *            the env_size
	 * @param env
	 *            the env
	 * @param name
	 *            the name
	 * @param mappings
	 *            the mappings
	 */
	public Procedure(int flags, int args, int env_size, Pair env, String name,
			Pair mappings) {
		this.flags = flags;
		this.args = args;
		this.env_size = env_size;
		this.env = env;
		this.name = name;
		this.mappings = mappings;
	}
}
