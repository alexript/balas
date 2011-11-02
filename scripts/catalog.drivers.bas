import java.lang
import com.allen_sauer.gwt.log.client.Log

function OnCreate(record)
 Log.error('method OnCreate not defined')
end

function OnTrash(record)
 Log.error('method OnTrash not defined')
end

function OnRestore(record)
 Log.error('method OnRestore not defined')
end

function OnUpdate(record)
 Log.error('method OnUpdate not defined')
end

// must return HashTable
function OnChange(fieldname, hashTable)
 return hashTable
end


