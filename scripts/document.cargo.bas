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
 invoices = Hashtable()
 incomingdocuments = document.getTableRecords('ingoods')
 i=0
 while i<incomingdocuments.size()
 	record = incomingdocuments.get(i)
  
 	indoc = Documents.get("ingoods", record.get('ingoods'))
 	goodstable = indoc.getTableRecords('goods')
 	
 	j = 0
 	while j<goodstable.size()
 		grecord = goodstable.get(j)
 		partner = grecord.get('partner')
         	
         invoice = 0
 		if invoices.containsKey(partner)
            
 			invoicenum = invoices.get(partner)
             invoice = Documents.get("invoice", invoicenum)
 		else
           
 		    invoice = Documents.create("invoice")
 		    invoice.setParent(document)
 		    invoice.set("partner", partner)
 		    invoice.set("cargo", document.get("number"))
 		    invoice.save()
             invoicenum = invoice.get("number")
 		    invoices.put(partner, invoicenum)
 		end
        
        
        recordstable = invoice.getTable("invoicerecords")
        values = Hashtable()
        values.put("summ", grecord.get('summ'))
        values.put("currency", grecord.get('currency'))
        values.put("ingoods", indoc.get("number"))
        values.put("comment", "Оплата доставки в Россию")
        
        recordstable.addRecord(values) 
        
        j+=1
 	end
 	i+=1
 end
 
end

function OnUnactivate(document) 
 document.deleteChilds("invoice")
end

// must return HashTable
function OnChange(fieldname, hashTable)
 if fieldname = 'status'
      hashTable.put('statusgdate', Date())
 end
 return hashTable
end

// must return HashTable
function OnChangeTable(tablename, fieldname, hashTable)
 return hashTable
end
