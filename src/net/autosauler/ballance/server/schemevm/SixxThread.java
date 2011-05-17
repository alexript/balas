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
 * The Class SixxThread.
 */
public class SixxThread extends Thread {

	/** The interpreter. */
	protected Sixx interpreter;

	/** The thunk. */
	protected Object thunk;

	/**
	 * Instantiates a new sixx thread.
	 * 
	 * @param interpreter
	 *            the interpreter
	 * @param thunk
	 *            the thunk
	 */
	public SixxThread(Sixx interpreter, Object thunk) {
		this.interpreter = interpreter;
		this.thunk = thunk;
		start();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		try {
			interpreter.apply(thunk, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
