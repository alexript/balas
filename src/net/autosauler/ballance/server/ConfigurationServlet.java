package net.autosauler.ballance.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.autosauler.ballance.server.model.Configuration;
import net.autosauler.ballance.shared.UserRole;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.allen_sauer.gwt.log.client.Log;

public class ConfigurationServlet extends HttpServlet {

	private static final long serialVersionUID = 1475747129190231475L;
	private boolean isMultipart;
	/** The file items. */
	private List<FileItem> items = null;

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

	@SuppressWarnings("unchecked")
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		UserRole role = getRole(req);
		String domain = getDomain(req);
		if (role.isAdmin()) {
			isMultipart = ServletFileUpload.isMultipartContent(req);
			if (isMultipart) {
				FileItemFactory factory = new DiskFileItemFactory();
				ServletFileUpload upload = new ServletFileUpload(factory);
				try {
					items = upload.parseRequest(req);
				} catch (FileUploadException e) {
					Log.error(e.getMessage());
				}

				String cmd = get(req, "cmd");
				if (cmd.equals("upload")) {
					uploadConfiguration(domain, items, resp);
				}
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
	 * Gets the.
	 * 
	 * @param param
	 *            the param
	 * @return the string
	 */
	private String get(HttpServletRequest req, String param) {
		String text = "";
		if (isMultipart) {
			if (items != null) {
				Iterator<FileItem> iter = items.iterator();
				while (iter.hasNext()) {
					FileItem item = iter.next();

					if (item.isFormField()) {
						String name = item.getFieldName();
						String val = item.getString();
						if (name.equals(param)) {
							text = val;
						}
					}
				}
			}
		} else {
			text = req.getParameter(param);
		}
		return text;
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

	private FileItem getUploadedFileItem(String param) {
		if (isMultipart) {
			Iterator<FileItem> iter = items.iterator();
			while (iter.hasNext()) {
				FileItem item = iter.next();
				String name = item.getFieldName();
				if (name.equals(param)) {
					return item;
				}
			}
		}
		return null;

	}

	private void uploadConfiguration(String domain, List<FileItem> items2,
			HttpServletResponse resp) {
		FileItem item = getUploadedFileItem("f");

		if (item != null) {
			File file;
			try {
				Configuration conf = new Configuration(domain);
				file = File.createTempFile("uploadconfiguration", ".zip");
				item.write(file);
				conf.updateConfiguration(file);

			} catch (IOException e) {
				Log.error(e.getMessage());
			} catch (Exception e) {
				Log.error(e.getMessage());
			}

		}

	}

}
