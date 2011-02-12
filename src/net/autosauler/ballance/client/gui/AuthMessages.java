/*
   Copyright 2011 Alex 'Ript' Malyshev <alexript@gmail.com>

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */

package net.autosauler.ballance.client.gui;

import com.google.gwt.i18n.client.Messages;

/**
 * The Interface AuthMessages (localization methods).
 */
public interface AuthMessages extends Messages {

	/**
	 * Hello user msg.
	 * 
	 * @param username
	 *            the username
	 * @return the string
	 */
	String helloUserMsg(String username);

	/**
	 * Btn logout.
	 * 
	 * @return the string
	 */
	String btnLogout();

	/**
	 * Label login.
	 * 
	 * @return the string
	 */
	String labelLogin();

	/**
	 * Label pssword.
	 * 
	 * @return the string
	 */
	String labelPssword();

	/**
	 * Btn login.
	 * 
	 * @return the string
	 */
	String btnLogin();

	/**
	 * Btn cancel.
	 * 
	 * @return the string
	 */
	String btnCancel();

	/**
	 * Qtn logout.
	 * 
	 * @return the string
	 */
	String qtnLogout();

	/**
	 * Btn yes.
	 * 
	 * @return the string
	 */
	String btnYes();

	/**
	 * Btn no.
	 * 
	 * @return the string
	 */
	String btnNo();

	/**
	 * Bad field value.
	 * 
	 * @return the string
	 */
	String badFieldValue();

	/**
	 * Bad auth.
	 * 
	 * @return the string
	 */
	String badAuth();

	/**
	 * Comm error.
	 * 
	 * @return the string
	 */
	String commError();

	/**
	 * Title roles.
	 * 
	 * @return the string
	 */
	String titleRoles();
}
