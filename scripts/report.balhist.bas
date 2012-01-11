function ExecuteReport(report)
  partner = Catalogs.get('partners', report.get('partners'))
  startd = report.get('startd')
  endd = report.get('endd')
  curr = report.get('currency')
  report.addLabel('История платежей для ' + partner.get('fullname') + ' с ' + strdate(startd) + ' по ' + strdate(endd) + ' (' + curr + ')')
  report.addDescription('Баланс по контрагенту')
  report.addColumn('Дата')
  report.addColumn('Документ')
  report.addColumn('Задолжность')
  report.addColumn('Платеж')
  report.addColumn('Баланс')
  
  reg = Regestry.get('balance')
  filter = Hashtable()
  filter.put('partner', report.get('partners'))
  balance = 0.0
  history = reg.history(curr, startd, endd, filter)
  i = 0
  while i < history.size()
  	rec = history.get(i)
  	summ = rec.getSumm()
  	balance += summ
  	doc = rec.getDocument()
  	report.putValue(strdate(rec.getDate()))
  	report.putValue(doc.getName())
  	if summ < 0
  	  report.putValue(round(summ, 2))
  	  report.putValue("")
  	else
  	  report.putValue("")
  	  report.putValue(round(summ, 2))
  	end
  	report.putValue(round(balance, 2))
  	report.drawRow()
  	i += 1
  end
  
  
end
