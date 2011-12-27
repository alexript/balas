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
 return hashTable
end

