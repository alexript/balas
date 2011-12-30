package net.autosauler.ballance.server.vm;

import java.util.Date;

import net.autosauler.ballance.server.model.AbstractDocument;
import net.autosauler.ballance.server.model.RegestryRecord;

public class RegRec {
	private final RegestryRecord rec;

	public RegRec(RegestryRecord r) {
		rec = r;

	}

	public Object get(String name) {
		return rec.get(name);
	}

	public Date getDate() {
		return rec.getDate();
	}

	public DocumentWrapper getDocument() {
		AbstractDocument d = rec.getDocument();
		return new DocumentWrapper(d);
	}

	public Double getSumm() {
		return rec.getSumm();
	}
}
