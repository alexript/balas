package net.autosauler.ballance.server.model;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import net.autosauler.ballance.server.mongodb.Database;

import com.allen_sauer.gwt.log.client.Log;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

public class Configuration {
	private final String domain;
	private File file;

	public Configuration(String domain) {
		this.domain = domain;
		file = null;
	}

	public void createDownloadFile() {
		try {
			file = File.createTempFile("conf", "zip");
			FileOutputStream dest = new FileOutputStream(file);
			ZipOutputStream zip = new ZipOutputStream(new BufferedOutputStream(
					dest));
			zip.setLevel(9);
			ZipEntry entry = null;

			DB db = Database.get(domain);
			if (db != null) {
				Database.retain();
				BasicDBObject q = new BasicDBObject();
				q.put("domain", domain);

				DBCollection coll = db.getCollection("scripts");

				DBCursor cur = coll.find(q);
				while (cur.hasNext()) {
					DBObject myDoc = cur.next();

					String txt = (String) myDoc.get("text");
					String name = (String) myDoc.get("name");
					entry = new ZipEntry("scripts/" + name);
					zip.putNextEntry(entry);
					zip.write(txt.getBytes("UTF-8"));

				}

				coll = db.getCollection("structures");
				cur = coll.find(q);
				while (cur.hasNext()) {
					DBObject myDoc = cur.next();

					String txt = (String) myDoc.get("text");
					String name = (String) myDoc.get("name");
					entry = new ZipEntry("structs/" + name);
					zip.putNextEntry(entry);
					zip.write(txt.getBytes("UTF-8"));

				}

				Database.release();
			}
			zip.flush();
			zip.close();

		} catch (IOException e) {
			Log.error(e.getMessage());
			file = null;
		}

	}

	public String getDownloadFileName() {

		return "configuration.zip";
	}

	public int getDownloadFileSize() {
		if (file == null) {
			return 0;
		}

		return (int) file.length();
	}

	public File getFile() {
		Log.trace("conf file: " + file.getAbsolutePath());
		return file;
	}

}
