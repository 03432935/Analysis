/*日期格式化*/
function formatDate(dateValue){
    if(null == dateValue){
        return "";
    }else{
        var date = new Date(dateValue);
        var year = date.getFullYear();
        var month = date.getMonth()+1;
        var day = date.getDate();
        month=(month>9)?(""+month):("0"+month);  //如果得到的数字小于9要在前面加'0'
        day=(day>9)?(""+day):("0"+day);
        return year + "-" + month + "-" + day;
    }
}
//日期格式的判断
var reg=/^([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8])))$/;