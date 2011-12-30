package net.autosauler.ballance.server.vm;

import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import net.autosauler.ballance.server.model.Regestry;
import net.autosauler.ballance.server.model.RegestryRecord;

public class RegestryWrapper {
	private final String domain;

	private Regestry reg = null;

	public RegestryWrapper(String domain) {
		this.domain = domain;
	}

	private RegestryWrapper(String domain, String name) {
		this.domain = domain;

		reg = new Regestry(name, domain);
	}

	public void add(DocumentWrapper doc, Double summ, String currency,
			Hashtable<String, Object> values) {
		if (reg != null) {
			reg.add(doc.impl, summ, currency, values);
		}
	}

	public void add(DocumentWrapper doc, String summ, String currency,
			Hashtable<String, Object> values) {
		Double s = Double.parseDouble(summ);
		add(doc, s, currency, values);
	}

	public RegestryWrapper get(String name) {
		return new RegestryWrapper(domain, name);
	}

	public List<RegRec> history(String currency, Date start, Date end,
			Hashtable<String, Object> filter) {
		List<RegRec> result = new ArrayList<RegRec>();
		if (reg != null) {
			List<RegestryRecord> h = reg.history(currency, start, end, filter);
			for (RegestryRecord r : h) {
				result.add(new RegRec(r));
			}
		}
		return result;
	}

	public void minus(DocumentWrapper doc, Double summ, String currency,
			Hashtable<String, Object> values) {
		if (reg != null) {
			reg.add(doc.impl, -summ, currency, values);
		}
	}

	public void minus(DocumentWrapper doc, String summ, String currency,
			Hashtable<String, Object> values) {
		Double s = Double.parseDouble(summ);
		minus(doc, s, currency, values);
	}

	public void remove(DocumentWrapper doc) {
		if (reg != null) {
			reg.remove(doc.impl);
		}
	}

	public Double summ(String currency, Date date,
			Hashtable<String, Object> filter) {
		Double result = new Double(0.0D);
		if (reg != null) {
			result = reg.summ(currency, date, filter);
		}

		return result;
	}

	public Double summ(String currency, Long date,
			Hashtable<String, Object> filter) {
		Double result = new Double(0.0D);
		if (reg != null) {
			result = reg.summ(currency, new Date(date), filter);
		}

		return result;
	}

}
