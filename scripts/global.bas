import java.lang
import java.text.SimpleDateFormat

// 1.5% for currency interchange
function globalCalcPercent(val)
 return val*1.5/100
end
 
function round(val, digits)
 dd = java.lang.Math.pow(10,digits)
 res = (java.lang.Math.round(val * dd) / dd)
 return res
end

function strdate(date)
  formatter = SimpleDateFormat("yyyy/MM/dd")
  return formatter.format(date)
end