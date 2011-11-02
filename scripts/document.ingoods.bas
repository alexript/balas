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
 Log.error('method OnActivate not defined')
end

function OnUnactivate(document)
 Log.error('method OnUnactivate not defined')
end

// must return HashTable
function OnChange(fieldname, hashTable)
 return hashTable
end

// must return HashTable
function OnChangeTable(tablename, fieldname, hashTable)
 if tablename='goods'
   if fieldname = 'weight'
     hashTable.put("summ", hashTable.get("weight") * hashTable.get("tarif"))
   elseif fieldname = 'tarif'
     hashTable.put("summ", hashTable.get("weight") * hashTable.get("tarif"))
   elseif fieldname = 'partner'
     rec = Catalogs.get("partners", hashTable.get("partner"))
     hashTable.put("currency", rec.get("currency"))
     rec2 = Catalogs.get("tarifs", rec.get("tarif"))
     hashTable.put("tarif", rec2.get("perkg"))
     hashTable.put("summ", hashTable.get("weight") * hashTable.get("tarif"))
   end
     
 end
 return hashTable
end



