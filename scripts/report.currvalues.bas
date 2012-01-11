function ExecuteReport(report)

report.addLabel('Currency values (' + report.get('currency') + ')')
report.addDescription('Currency values per date')
report.addColumn('Date')
report.addColumn('Value')
 c = report.get('currency')
 s = report.get('startd')
 e = report.get('endd')
  
values = Currency.get(c, s, e)
i = values.iterator()
while i.hasNext()
 v = i.next()
 report.putValue(v.getDate())
 report.putValue(round(v.getValue(), 4))
 report.drawRow()
end
end
