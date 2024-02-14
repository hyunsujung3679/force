$(function() {
    let key = getCookie("idChk"); //user1
    if(key != ""){
        $("#userId").val(key);
    }

    if($("#userId").val() != ""){
        $("#idSaveCheck").attr("checked", true);
    }

    $("#idSaveCheck").change(function(){
        if($("#idSaveCheck").is(":checked")){
            setCookie("idChk", $("#userId").val(), 7);
        }else{
            deleteCookie("idChk");
        }
    });

    $("#userId").keyup(function(){
        if($("#idSaveCheck").is(":checked")){
            setCookie("idChk", $("#userId").val(), 7);
        }
    });
})

function setCookie(cookieName, value, exdays){
    let exdate = new Date();
    exdate.setDate(exdate.getDate() + exdays);
    var cookieValue = escape(value) + ((exdays==null) ? "" : "; expires=" + exdate.toGMTString());
    document.cookie = cookieName + "=" + cookieValue;
}

function deleteCookie(cookieName){
    let expireDate = new Date();
    expireDate.setDate(expireDate.getDate() - 1);
    document.cookie = cookieName + "= " + "; expires=" + expireDate.toGMTString();
}

function getCookie(cookieName) {
    cookieName = cookieName + '=';
    let cookieData = document.cookie;
    let start = cookieData.indexOf(cookieName);
    let cookieValue = '';
    if(start != -1){
        start += cookieName.length;
        var end = cookieData.indexOf(';', start);
        if(end == -1)end = cookieData.length;
        cookieValue = cookieData.substring(start, end);
    }
    return unescape(cookieValue);
}