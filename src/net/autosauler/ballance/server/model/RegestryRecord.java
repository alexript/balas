package net.autosauler.ballance.server.model;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import net.autosauler.ballance.shared.Description;
import net.autosauler.ballance.shared.Field;

import com.mongodb.DBObject;

public class RegestryRecord {

	private final String domain;
	private final String docname;
	private final Long docnum;
	private final Date date;
	private final Double summ;
	private final HashMap<String, Object> values;

	public RegestryRecord(Description descr, DBObject myDoc, String currency) {

		values = new HashMap<String, Object>();
		domain = (String) myDoc.get(Regestry.fieldname_domain);
		docname = (String) myDoc.get(Regestry.fieldname_docname);
		docnum = (Long) myDoc.get(Regestry.fieldname_docnum);
		date = (Date) myDoc.get(Regestry.fieldname_date);
		summ = Currency.convert((Double) myDoc.get(Regestry.fieldname_summ),
				(String) myDoc.get(Regestry.fieldname_curr), currency, date);

		List<Field> fields = descr.get();
		for (Field field : fields) {
			values.put(field.getFieldname(), myDoc.get(field.getFieldname()));
		}
	}

	public Object get(String name) {
		if (!values.containsKey(name)) {
			return null;
		}

		return values.get(name);
	}

	public Date getDate() {
		return date;
	}

	public AbstractDocument getDocument() {
		return new AbstractDocument(docname, domain, docnum);
	}

	public Double getSumm() {
		return summ;
	}

}
