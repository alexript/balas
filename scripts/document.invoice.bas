import java.lang
import com.allen_sauer.gwt.log.client.Log

function OnCreate(document)
 Log.error('method OnCreate not defined')
end

function OnTrash(document)
 Log.error('method OnTrash not defined')
end

function OnRestore(document)
 Log.error('method OnRestore not defined')
end

function OnUpdate(document)
 Log.error('method OnUpdate not defined')
end

function OnActivate(document)
 reg = Regestry.get("balance")
 partner = document.get("partner")
 records = document.getTableRecords("invoicerecords")
 i = 0
 while i < records.size()
 	record = records.get(i)
 	s = record.get("summ")
 	c = record.get("currency")
 	ingoods = record.get("ingoods")
 	values = Hashtable()
 	values.put("partner", partner)
 	values.put("ingoods", ingoods)
 	
 	reg.minus(document, s, c, values)
 	i+=1
 end
end

function OnUnactivate(document)
 reg = Regestry.get("balance")
 reg.remove(document)
end

// must return HashTable
function OnChange(fieldname, hashTable)
 return hashTable
end

