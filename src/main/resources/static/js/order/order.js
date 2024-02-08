function selectMenuListByCategoryNo(div) {

    const categoryNo = div.children[0].value;

    $.ajax({
        url: "/menu/" + categoryNo,
        type: "get",
        success: function(data) {

            $("#menu-1").html("");
            $("#menu-2").html("");
            $("#menu-3").html("");

            for(let index in data) {
                let html = "";

                html += '<div class="menu-content">';
                html +=     '<div class="menu-content-write">';
                html +=         '<input type="hidden" class="menu-no">'
                html +=         '<div class="menu-name">' + data[index].menuName + '</div>';
                html +=         '<div class="menu-price">' + data[index].salePrice.toLocaleString() + '</div>';
                html +=     '</div>'
                html += '</div>'

                $("input[name=menu-no]").attr("value", data[index].menuNo);

                if(index < 6) {
                    $("#menu-1").append(html);
                } else if(index >= 6 && index < 12) {
                    $("#menu-2").append(html);
                } else if(index >= 12 && index < 18) {
                    $("#menu-3").append(html);
                }
            }
        },
        error: function(xhr) {
            console.log(xhr);
        }
    });

}
$(function() {
    const nonClick = document.querySelectorAll(".menu-content");

    function handleClick(event) {
        nonClick.forEach((e) => {
            e.classList.remove("menu-color");
        });
        event.target.classList.add("menu-color");
    }

    nonClick.forEach((e) => {
        e.addEventListener("click", handleClick);
    });
})

function insertOrder() {

    const menuNo = $(".menu-color").children().children().eq(0).val();
    const tableNo = $(".table-no").val();
    const parameter = {menuNo : menuNo ,tableNo : tableNo};

    $.ajax({
        url: "/order",
        type: "post",
        data: JSON.stringify(parameter),
        dataType : "json",
        contentType: "application/json",
        success: function(data) {

            $(".table-middle").html("");

            if(data.quantity == 1) {
                let html = "";
                html += '<div class="table-middle-wrap" onclick="">'
                html +=     '<div class="table-content-1">'
                html +=         '<div class="table-content-name-1">' + data.orderSeqInt + '</div>'
                html +=     '</div>'
                html +=     '<div class="table-content-2">'
                html +=         '<div class="table-content-name-1">' + data.menuName + '</div>'
                html +=     '</div>'
                html +=     '<div class="table-content-3">'
                html +=         '<div class="table-content-name-1">' + data.salePrice.toLocaleString() + '</div>'
                html +=     '</div>'
                html +=     '<div class="table-content-3">'
                html +=         '<div class="table-content-name-1">' + data.quantity + '</div>'
                html +=     '</div>'
                html +=     '<div class="table-content-3">'
                html +=         '<div class="table-content-name-1">' + data.discountPrice.toLocaleString() + '</div>'
                html +=     '</div>'
                html +=     '<div class="table-content-3">'
                html +=         '<div class="table-content-name-1">' + data.totalSalePrice.toLocaleString() + '</div>'
                html +=     '</div>'
                html +=     '<div class="table-content-3">'
                html +=         '<div class="table-content-name-1">' + data.etc + '</div>'
                html +=     '</div>'
                html += '</div>'

                $(".table-middle").append(html);
                $(".menu-color").removeClass("menu-color");
            } else {

            }

        },
        error: function(xhr) {
            console.log(xhr);
        }
    });
}