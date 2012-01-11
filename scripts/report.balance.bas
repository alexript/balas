function ExecuteReport(report)
  rdate = report.get('endd')
  rcurr = report.get('currency')
  report.addLabel('Баланс на ' + strdate(rdate) + ' (' + rcurr + ')')
  report.addDescription('Баланс по контрагентам')
  report.addColumn('Контрагент')
  report.addColumn('Баланс')
  
  partners = Catalogs.getRecords('partners')
  reg = Regestry.get('balance')
  i = 0
  while i < partners.size()
    id = partners.get(i)
    partner = Catalogs.get('partners', id)
    fullname = partner.get('fullname')
    
    report.putValue(fullname)
    filter = Hashtable()
    filter.put('partner', id)
    s = reg.summ(rcurr, rdate, filter)
    report.putValue(round(s, 2))
    i += 1
    report.drawRow()
  end
  

end
