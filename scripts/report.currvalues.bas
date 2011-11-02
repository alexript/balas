function CreateForm(form)
 form.add('currency', 'Валюта', DT_CURRENCY, 'USD')
 form.add('startd', 'Начальная дата', 'DT_DATE')
 form.add('endd', 'Конечная дата', 'DT_DATE')
end

function ExecuteReport(report)
	report.addLabel('Курс валюты (' + report.get('currency') + ')')
	report.addDescription('Курсы валюты на интервал дат')
	report.addColumn('Дата')
	report.addColumn('Курс')
	report.addColumn('Конв.Проц.')
	report.addColumn('Итого')
	values = Currency.get(report.get('currency'), report.get('startd'), report.get('endd'))
	i = values.iterator()
	while i.hasNext()
	 v = i.next()
	 realval = v.getValue()
	 percent = globalCalcPercent(realval)
	 summ = realval + percent
	 report.putValue(v.getDate())
	 report.putValue(realval)
	 report.putValue(percent)
	 report.putValue(summ)
	 report.drawRow()
	end
end
