package net.autosauler.ballance.server.model;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
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

	public void updateConfiguration(File zip) {
		file = zip;
		Log.trace("parse uploaded conf file: " + file.getName());
		DB db = Database.get(domain);
		if (db != null) {
			Database.retain();
			ZipFile zf;
			try {
				zf = new ZipFile(file);

				Enumeration<? extends ZipEntry> entries = zf.entries();

				while (entries.hasMoreElements()) {
					ZipEntry ze = entries.nextElement();

					long size = ze.getSize();
					if (size > 0) {
						StringBuilder sb = new StringBuilder();
						BufferedReader br = new BufferedReader(
								new InputStreamReader(zf.getInputStream(ze)));
						String line;
						while ((line = br.readLine()) != null) {
							sb.append(line);
							sb.append('\n');
						}
						br.close();
						String entryname = ze.getName();
						String text = sb.toString().trim();
						if (entryname.startsWith("scripts/")) {
							String name = entryname.replace("scripts/", "");

							DBObject doc = null;

							DBCollection coll = db.getCollection("scripts");
							BasicDBObject query = new BasicDBObject();
							query.put("domain", domain);
							query.put("name", name);

							doc = coll.findOne(query);

							if (doc != null) {
								doc.put("text", text);
								coll.save(doc);
							} else {
								doc = new BasicDBObject();
								doc.put("domain", domain);
								doc.put("name", name);
								doc.put("text", text);
								coll.insert(doc);
							}

						} else if (entryname.startsWith("structs/")) {
							String name = entryname.replace("structs/", "");

							DBObject doc = null;

							DBCollection coll = db.getCollection("structures");
							BasicDBObject query = new BasicDBObject();
							query.put("domain", domain);
							query.put("name", name);

							doc = coll.findOne(query);

							if (doc != null) {
								doc.put("text", text);
								coll.save(doc);
							} else {
								doc = new BasicDBObject();
								doc.put("domain", domain);
								doc.put("name", name);
								doc.put("text", text);
								coll.insert(doc);
							}
						}
					}

				}
			} catch (ZipException e) {
				Log.error(e.getMessage());
			} catch (IOException e) {
				Log.error(e.getMessage());
			}
			Database.release();
		}

		file.delete();

	}

}
