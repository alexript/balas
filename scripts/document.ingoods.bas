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
function OnSenddateChange(hashTable)
 Log.error('method OnSenddateChange not defined')
 return hashTable
end

// must return HashTable
function OnAwbChange(hashTable)
 Log.error('method OnAwbChange not defined')
 return hashTable
end

// must return HashTable
function OnActivationdateChange(hashTable)
 Log.error('method OnActivationdateChange not defined')
 return hashTable
end

// must return HashTable
function OnInvoiceChange(hashTable)
 Log.error('method OnInvoiceChange not defined')
 return hashTable
end

// must return HashTable
function OnCreatedateChange(hashTable)
 Log.error('method OnCreatedateChange not defined')
 return hashTable
end

// must return HashTable
function OnPardocnameChange(hashTable)
 Log.error('method OnPardocnameChange not defined')
 return hashTable
end

// must return HashTable
function OnPardocChange(hashTable)
 Log.error('method OnPardocChange not defined')
 return hashTable
end

// must return HashTable
function OnNumberChange(hashTable)
 Log.error('method OnNumberChange not defined')
 return hashTable
end

// must return HashTable
function OnGnumChange(hashTable)
 Log.error('method OnGnumChange not defined')
 return hashTable
end

// must return HashTable
function OnUsernameChange(hashTable)
 Log.error('method OnUsernameChange not defined')
 return hashTable
end

// must return HashTable
function OnDomainChange(hashTable)
 Log.error('method OnDomainChange not defined')
 return hashTable
end

// must return HashTable
function OnTrashChange(hashTable)
 Log.error('method OnTrashChange not defined')
 return hashTable
end

// must return HashTable
function OnActiveChange(hashTable)
 Log.error('method OnActiveChange not defined')
 return hashTable
end

// must return HashTable
function OnPartnerChange(hashTable)
 Log.error('method OnPartnerChange not defined')
 return hashTable
end

// must return HashTable
function OnGtdChange(hashTable)
 Log.error('method OnGtdChange not defined')
 return hashTable
end

// must return HashTable
function OnGoodsaddpayDocnumChange(hashTable)
 Log.error('method OnGoodsaddpayDocnumChange not defined')
 return hashTable
end

// must return HashTable
function OnGoodsaddpayUsernameChange(hashTable)
 Log.error('method OnGoodsaddpayUsernameChange not defined')
 return hashTable
end

// must return HashTable
function OnGoodsaddpayTrashChange(hashTable)
 Log.error('method OnGoodsaddpayTrashChange not defined')
 return hashTable
end

// must return HashTable
function OnGoodsaddpayCreatedateChange(hashTable)
 Log.error('method OnGoodsaddpayCreatedateChange not defined')
 return hashTable
end

// must return HashTable
function OnGoodsaddpayDomainChange(hashTable)
 Log.error('method OnGoodsaddpayDomainChange not defined')
 return hashTable
end

// must return HashTable
function OnGoodsaddpayPaydateChange(hashTable)
 Log.error('method OnGoodsaddpayPaydateChange not defined')
 return hashTable
end

// must return HashTable
function OnGoodsaddpayPartnerChange(hashTable)
 Log.error('method OnGoodsaddpayPartnerChange not defined')
 return hashTable
end

// must return HashTable
function OnGoodsaddpayNumberChange(hashTable)
 Log.error('method OnGoodsaddpayNumberChange not defined')
 return hashTable
end

// must return HashTable
function OnGoodsaddpayDescrChange(hashTable)
 Log.error('method OnGoodsaddpayDescrChange not defined')
 return hashTable
end

// must return HashTable
function OnGoodsaddpaySummChange(hashTable)
 Log.error('method OnGoodsaddpaySummChange not defined')
 return hashTable
end

// must return HashTable
function OnGoodsaddpayCurrencyChange(hashTable)
 Log.error('method OnGoodsaddpayCurrencyChange not defined')
 return hashTable
end

// must return HashTable
function OnGoodsDocnumChange(hashTable)
 Log.error('method OnGoodsDocnumChange not defined')
 return hashTable
end

// must return HashTable
function OnGoodsUsernameChange(hashTable)
 Log.error('method OnGoodsUsernameChange not defined')
 return hashTable
end

// must return HashTable
function OnGoodsWeightChange(hashTable)
 hashTable.put("summ", hashTable.get("weight") * hashTable.get("tarif")) 
 return hashTable
end
  
function OnGoodsTarifChange(hashTable)
 hashTable.put("summ", hashTable.get("weight") * hashTable.get("tarif")) 
 return hashTable
end

// must return HashTable
function OnGoodsTrashChange(hashTable)
 Log.error('method OnGoodsTrashChange not defined')
 return hashTable
end

// must return HashTable
function OnGoodsCreatedateChange(hashTable)
 Log.error('method OnGoodsCreatedateChange not defined')
 return hashTable
end

// must return HashTable
function OnGoodsDomainChange(hashTable)
 Log.error('method OnGoodsDomainChange not defined')
 return hashTable
end

// must return HashTable
function OnGoodsPartnerChange(hashTable)
 rec = Catalogs.get("partners", hashTable.get("partner"))
 hashTable.put("currency", rec.get("currency"))
 rec2 = Catalogs.get("tarifs", rec.get("tarif"))
 hashTable.put("tarif", rec2.get("perkg"))
 hashTable.put("summ", hashTable.get("weight") * hashTable.get("tarif"))
 return hashTable
end

// must return HashTable
function OnGoodsNumberChange(hashTable)
 Log.error('method OnGoodsNumberChange not defined')
 return hashTable
end

// must return HashTable
function OnGoodsBoxesnumChange(hashTable)
 Log.error('method OnGoodsBoxesnumChange not defined')
 return hashTable
end

// must return HashTable
function OnGoodsSummChange(hashTable)
 Log.error('method OnGoodsSummChange not defined')
 return hashTable
end

// must return HashTable
function OnGoodsCurrencyChange(hashTable)
 Log.error('method OnGoodsCurrencyChange not defined')
 return hashTable
end

