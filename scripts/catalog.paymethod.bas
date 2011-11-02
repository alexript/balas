import java.lang
import com.allen_sauer.gwt.log.client.Log

function OnCreate()
 Log.error('method OnCreate not defined')
end

function OnTrash()
 Log.error('method OnTrash not defined')
end

function OnRestore()
 Log.error('method OnRestore not defined')
end

function OnUpdate()
 Log.error('method OnUpdate not defined')
end

// must return HashTable
function OnChange(fieldname, hashTable)
 if fieldname = 'fullname'
  hashTable.put("fullname", hashTable.get("fullname").trim())
 end
 return hashTable
end


