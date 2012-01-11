function ExecuteReport(report)
report.addLabel('Currency values (' + report.get('currency') + ')')
report.addDescription('Currency values per date')
report.addColumn('Date')
report.addColumn('Value')
values = Currency.get(report.get('currency'), report.get('startd'), report.get('endd'))
i = values.iterator()
while i.hasNext()
 v = i.next()
 report.putValue(v.getDate())
 report.putValue(v.getValue())
 report.drawRow()
end
end
