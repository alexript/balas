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
 cargo = Documents.get("cargo")
 table = cargo.getTable("ingoods")
 childs = table.findContainers("ingoods", document)
 i = 0
 while i < childs.size()
    cargodoc = childs.get(i)
    cargodoc.unactivate()
    i+=1
 end
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
 if tablename='goods'
   if fieldname = 'weight'
     hashTable.put("summ", hashTable.get("weight") * hashTable.get("tarif"))
   elseif fieldname = 'tarif'
     hashTable.put("summ", hashTable.get("weight") * hashTable.get("tarif"))
   elseif fieldname = 'partner'
     rec = Catalogs.get("partners", hashTable.get("partner"))
     hashTable.put("currency", rec.get("currency"))
     
     hashTable.put("tarif", rec.get("perkg"))
     hashTable.put("summ", hashTable.get("weight") * hashTable.get("tarif"))
   end
     
 end
 return hashTable
end



