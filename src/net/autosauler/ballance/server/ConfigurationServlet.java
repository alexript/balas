package net.autosauler.ballance.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.autosauler.ballance.server.model.Configuration;
import net.autosauler.ballance.shared.UserRole;

public class ConfigurationServlet extends HttpServlet {

	private static final long serialVersionUID = 1475747129190231475L;

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		UserRole role = getRole(req);
		if (role.isAdmin()) {
			String cmd = req.getParameter("cmd");
			if (cmd.equals("download")) {
				downloadConfiguration(req, resp);
			}
		}
	}

	private void downloadConfiguration(HttpServletRequest req,
			HttpServletResponse resp) throws IOException {
		ServletOutputStream outStream = resp.getOutputStream();
		resp.setContentType("application/octet-stream");
		Configuration conf = new Configuration(getDomain(req));
		conf.createDownloadFile();
		resp.setHeader("Content-Disposition",
				"attachment;filename=" + conf.getDownloadFileName());
		resp.setContentLength(conf.getDownloadFileSize());

		File f = conf.getFile();

		FileInputStream fileIn = null;
		try {
			fileIn = new FileInputStream(f);

			byte[] outputByte = new byte[4096];
			// copy binary contect to output stream
			int n = 0;
			while ((n = fileIn.read(outputByte, 0, 4096)) != -1) {
				outStream.write(outputByte, 0, n);
			}
			fileIn.close();
			outStream.flush();
			outStream.close();
		} catch (FileNotFoundException e) {
			try {
				resp.sendError(500);
			} catch (IOException e1) {

			}
		} catch (IOException e) {
			try {
				resp.sendError(500);
			} catch (IOException e1) {

			}
		}
		if (f != null) {

			f.delete();
		}

	}

	/**
	 * Gets the domain.
	 * 
	 * @return the domain
	 */
	private String getDomain(HttpServletRequest req) {
		HttpSession httpSession = req.getSession();
		String domain = HttpUtilities.getUserDomain(httpSession);
		return domain;
	}

	/**
	 * Gets the role.
	 * 
	 * @return the role
	 */
	private UserRole getRole(HttpServletRequest req) {
		HttpSession httpSession = req.getSession();
		UserRole role = HttpUtilities.getUserRole(httpSession);
		return role;
	}

}
