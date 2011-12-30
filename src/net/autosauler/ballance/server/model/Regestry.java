package net.autosauler.ballance.server.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

import net.autosauler.ballance.server.mongodb.Database;
import net.autosauler.ballance.shared.Description;
import net.autosauler.ballance.shared.Field;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

public class Regestry {
	private static final String prefix = "reg_";
	private final String domain;
	private final String tablename;
	private final String structname;

	protected final static String fieldname_domain = "domain";
	protected final static String fieldname_docname = "docname";
	protected final static String fieldname_docnum = "docnum";
	protected final static String fieldname_date = "adddate";
	protected final static String fieldname_summ = "s";
	protected final static String fieldname_curr = "c";

	private Description descr;

	public Regestry(String name, String domain) {
		this.domain = domain;
		tablename = prefix + name;
		structname = "regestry." + name;
		initStruct();
	}

	public boolean add(AbstractDocument doc, Double summ, String currency,
			Hashtable<String, Object> values) {
		boolean result = false;

		DB db = Database.get(domain);
		if (db != null) {
			BasicDBObject data = new BasicDBObject();

			if (data != null) {
				data.put(fieldname_domain, domain);
				data.put(fieldname_docname, doc.getDocname());
				data.put(fieldname_docnum, doc.getNumber());
				data.put(fieldname_date, new Date());
				data.put(fieldname_summ, summ);
				data.put(fieldname_curr, currency);

				Set<String> names = values.keySet();
				for (String name : names) {
					data.put(name, values.get(name));
				}

				Database.retain();
				DBCollection coll = db.getCollection(tablename);
				coll.insert(data);
				Database.release();
				result = true;
			}
		}

		return result;
	}

	public List<RegestryRecord> history(String currency, Date startdate,
			Date enddate, Hashtable<String, Object> filter) {
		List<RegestryRecord> result = new ArrayList<RegestryRecord>();
		DB db = Database.get(domain);
		if (db != null) {
			BasicDBObject query = new BasicDBObject();
			query.put(fieldname_domain, domain);
			BasicDBObject dq = new BasicDBObject();
			dq.put("$gte", startdate);
			dq.put("$lte", enddate);
			query.put(fieldname_date, dq);

			Set<String> names = filter.keySet();
			for (String name : names) {
				query.put(name, filter.get(name));
			}

			Database.retain();
			DBCollection coll = db.getCollection(tablename);

			DBCursor cur = coll.find(query);
			while (cur.hasNext()) {
				DBObject myDoc = cur.next();
				result.add(new RegestryRecord(descr, myDoc, currency));

			}
			Database.release();
		}
		return result;
	}

	private void initStruct() {
		Structures structs = new Structures(domain);
		descr = structs.getDescription(structname);

		DB db = Database.get(domain);
		if (db != null) {
			Database.retain();
			DBCollection coll = db.getCollection(tablename);
			List<DBObject> indexes = coll.getIndexInfo();
			if (indexes.size() < 1) {
				BasicDBObject i = new BasicDBObject();
				// for delete
				i.put(fieldname_domain, 1);
				i.put(fieldname_docname, 1);
				i.put(fieldname_docnum, 1);
				coll.createIndex(i);

				// for summ
				List<Field> fields = descr.get();
				for (Field field : fields) {
					i = new BasicDBObject();
					i.put(fieldname_domain, 1);
					i.put(fieldname_date, 1);
					i.put(field.getFieldname(), 1);
					coll.createIndex(i);
				}

			}
			Database.release();
		}
	}

	public boolean remove(AbstractDocument doc) {
		boolean result = false;

		String docname = doc.getDocname();
		Long docnum = doc.getNumber();
		DB db = Database.get(domain);
		if (db != null) {
			BasicDBObject data = new BasicDBObject();
			if (data != null) {
				data.put(fieldname_domain, domain);
				data.put(fieldname_docname, docname);
				data.put(fieldname_docnum, docnum);
				Database.retain();
				DBCollection coll = db.getCollection(tablename);

				coll.remove(data);
				Database.release();
				result = true;
			}
		}
		return result;
	}

	public Double summ(String currency, Date date,
			Hashtable<String, Object> filter) {
		Double result = new Double(0.0D);
		DB db = Database.get(domain);
		if (db != null) {
			BasicDBObject query = new BasicDBObject();
			query.put(fieldname_domain, domain);
			BasicDBObject dq = new BasicDBObject();
			dq.put("$lte", date);
			query.put(fieldname_date, dq);

			Set<String> names = filter.keySet();
			for (String name : names) {
				query.put(name, filter.get(name));
			}

			Database.retain();
			DBCollection coll = db.getCollection(tablename);

			DBCursor cur = coll.find(query);
			while (cur.hasNext()) {
				DBObject myDoc = cur.next();
				Double summ = (Double) myDoc.get(fieldname_summ);
				String curr = (String) myDoc.get(fieldname_curr);
				Date recorddate = (Date) myDoc.get(fieldname_date);

				result = result
						+ Currency.convert(summ, curr, currency, recorddate);

			}
			Database.release();
		}
		return result;
	}
}
