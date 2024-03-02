function selectMenuListByCategoryNo(div) {

    const categoryNo = div.children[0].value;

    $.ajax({
        url: "/menu/" + categoryNo,
        type: "get",
        success: function(data) {
            selectMenuList();
        },
        error: function(xhr) {
            console.log(xhr);
        }
    });

}

$(function() {
    const nonClickMenu = document.querySelectorAll(".menu-content-button");
    const nonClickOrder = document.querySelectorAll(".table-middle-wrap");
    const inputValue = document.querySelector('.top-line-input');

    function handleMenuClick(event) {
        nonClickMenu.forEach((e) => {
            e.classList.remove("menu-color");
        });
        event.target.classList.add("menu-color");
    }

    function handleMenuOrder(event) {
        nonClickOrder.forEach((e) => {
            e.classList.remove("order-color");
        });
        event.target.classList.add("order-color");
    }

    nonClickMenu.forEach((e) => {
        e.addEventListener("click", handleMenuClick);
    });

    nonClickOrder.forEach((e) => {
        e.addEventListener("click", handleMenuOrder);
    });

    inputValue.addEventListener('keyup', function(e) {
        let value = e.target.value;
        value = Number(value.replaceAll(',', ''));
        if(isNaN(value)) {         //NaN인지 판별
            inputValue.value = 0;
        }else {                   //NaN이 아닌 경우
            const formatValue = value.toLocaleString('ko-KR');
            inputValue.value = formatValue;
        }
    })
})

function saveOrder() {

    const menuNo = $(".menu-color").parent().children().eq(0).val();
    const tableNo = $(".table-no").val();
    const parameter = {menuNo : menuNo ,tableNo : tableNo};

    $.ajax({
        url: "/order/save",
        type: "post",
        data: JSON.stringify(parameter),
        dataType : "json",
        contentType: "application/json",
        success: function(data) {
            if(data > 0) {
                selectOrderList();
                selectMenuList();
                $(".menu-color").removeClass("menu-color");
            } else {
                $("#exception-modal-1").modal({});
            }
        },
        error: function(xhr) {
            console.log(xhr);
        }
    });
}

function cancelSelection() {
    const orderNo = $(".order-color").children().eq(0).val();
    const menuNo = $(".order-color").children().eq(1).val();
    const tableNo = $(".table-no").val();
    const parameter = {orderNo : orderNo, menuNo : menuNo, tableNo : tableNo};

    $.ajax({
        url: "/order/cancel/selection",
        type: "post",
        data: JSON.stringify(parameter),
        dataType : "json",
        contentType: "application/json",
        success: function(data) {
            if(data > 0) {
                selectOrderList();
                $(".menu-color").removeClass("order-color");
            }
        },
        error: function(xhr) {
            console.log(xhr);
        }
    });
}

function cancelWhole() {
    const orderNo = $("input[name=order-no]").eq(0).val();
    const tableNo = $(".table-no").val();
    const parameter = {orderNo : orderNo, tableNo : tableNo};

    $.ajax({
        url: "/order/cancel/whole",
        type: "post",
        data: JSON.stringify(parameter),
        dataType : "json",
        contentType: "application/json",
        success: function(data) {
            if(data > 0) {
                $(".table-middle").html("");
                $("#total-quantity").text(0);
                $("#total-discount-price").text(0);
                $("#total-sale-price").text(0);
            }
        },
        error: function(xhr) {
            console.log(xhr);
        }
    });
}

function quantityChange() {
    const orderNo = $(".order-color").children().eq(0).val();
    const menuNo = $(".order-color").children().eq(1).val();
    const quantity = $("input[name=inputValue]").val();
    const tableNo = $(".table-no").val();
    const parameter = {orderNo : orderNo, menuNo : menuNo, quantityStr : quantity, tableNo : tableNo};

    $.ajax({
        url: "/order/change/quantity",
        type: "post",
        data: JSON.stringify(parameter),
        dataType : "json",
        contentType: "application/json",
        success: function(data) {
            if(data > 0) {
                selectOrderList();
                $(".menu-color").removeClass("order-color");
                $("input[name=inputValue]").val(0);
            }
        },
        error: function(xhr) {
            console.log(xhr);
        }
    });
}

function salePriceChange() {
    const orderNo = $(".order-color").children().eq(0).val();
    const menuNo = $(".order-color").children().eq(1).val();
    const salePrice = $("input[name=inputValue]").val();
    const tableNo = $(".table-no").val();
    const parameter = {orderNo : orderNo, menuNo : menuNo, salePriceStr : salePrice, tableNo : tableNo};

    $.ajax({
        url: "/order/change/salePrice",
        type: "post",
        data: JSON.stringify(parameter),
        dataType : "json",
        contentType: "application/json",
        success: function(data) {
            if(data > 0) {
                selectOrderList();
                $(".menu-color").removeClass("order-color");
                $("input[name=inputValue]").val(0);
            }
        },
        error: function(xhr) {
            console.log(xhr);
        }
    });
}

function service() {
    const orderNo = $(".order-color").children().eq(0).val();
    const menuNo = $(".order-color").children().eq(1).val();
    const tableNo = $(".table-no").val();
    const parameter = {orderNo : orderNo, menuNo : menuNo, tableNo : tableNo};

    $.ajax({
        url: "/order/service",
        type: "post",
        data: JSON.stringify(parameter),
        dataType : "json",
        contentType: "application/json",
        success: function(data) {
            if(data > 0) {
                selectOrderList();
                $(".menu-color").removeClass("order-color");
            }
        },
        error: function(xhr) {
            console.log(xhr);
        }
    });
}

function selectOrderList() {

    const tableNo = $(".table-no").val();

    $.ajax({
        url: "/order",
        type: "get",
        data: {tableNo : tableNo},
        success: function(data) {
            $(".table-middle").html("");

            let totalPrice = 0;
            let totalQuantity = 0;
            let totalDiscountPrice = 0;
            let totalSalePrice = 0;

            for(let index in data) {
                let html = "";
                html += '<div class="table-middle-wrap">'
                html +=     '<input type="hidden" name="order-no">'
                html +=     '<input type="hidden" name="menu-no">'
                html +=     '<div class="table-content-1">'
                html +=         '<div>' + data[index].no + '</div>'
                html +=     '</div>'
                html +=     '<div class="table-content-2">'
                html +=         '<div>' + data[index].menuName + '</div>'
                html +=     '</div>'
                html +=     '<div class="table-content-3">'
                html +=         '<div>' + data[index].salePrice.toLocaleString() + '</div>'
                html +=     '</div>'
                html +=     '<div class="table-content-3">'
                html +=         '<div>' + data[index].quantity + '</div>'
                html +=     '</div>'
                html +=     '<div class="table-content-3">'
                html +=         '<div>' + data[index].discountPrice.toLocaleString() + '</div>'
                html +=     '</div>'
                html +=     '<div class="table-content-3">'
                html +=         '<div>' + data[index].totalSalePrice.toLocaleString() + '</div>'
                html +=     '</div>'
                html +=     '<div class="table-content-6">'
                html +=         '<div>' + data[index].etc + '</div>'
                html +=     '</div>'
                html += '</div>'

                totalPrice += data[index].salePrice;
                totalQuantity += data[index].quantity;
                totalDiscountPrice += data[index].discountPrice;
                totalSalePrice += data[index].totalSalePrice;

                $(".table-middle").append(html);
            }
            for(let index in data) {
                $("input[name=order-no]").eq(index).val(data[index].orderNo);
                $("input[name=menu-no]").eq(index).val(data[index].menuNo);
            }
            $("#total-quantity").text(totalQuantity);
            $("#total-discount-price").text(totalDiscountPrice.toLocaleString());
            $("#total-sale-price").text(totalSalePrice.toLocaleString());
            $("#total-price").text(totalPrice.toLocaleString());
            $("#discount-price").text(totalDiscountPrice.toLocaleString());
            $("#receive-price").text(totalSalePrice.toLocaleString());

            const nonClickOrder = document.querySelectorAll(".table-middle-wrap");
            function handleMenuOrder(event) {
                nonClickOrder.forEach((e) => {
                    e.classList.remove("order-color");
                });
                event.target.classList.add("order-color");
            }
            nonClickOrder.forEach((e) => {
                e.addEventListener("click", handleMenuOrder);
            });
        },
        error: function(xhr) {
            console.log(xhr);
        }
    });
}

function fullPer() {
    const orderNo = $("input[name=order-no]").eq(0).val();
    const percentStr = $("input[name=inputValue]").val();
    const tableNo = $(".table-no").val();
    const parameter = {orderNo : orderNo, percentStr : percentStr, tableNo : tableNo};

    $.ajax({
        url: "/order/discount/full/per",
        type: "post",
        data: JSON.stringify(parameter),
        dataType : "json",
        contentType: "application/json",
        success: function(data) {
            if(data > 0) {
                selectOrderList();
                $(".menu-color").removeClass("order-color");
                $("input[name=inputValue]").val(0);
            }
        },
        error: function(xhr) {
            console.log(xhr);
        }
    });
}

function fullPrice() {
    const orderNo = $("input[name=order-no]").eq(0).val();
    const discountPriceStr = $("input[name=inputValue]").val();
    const tableNo = $(".table-no").val();
    const parameter = {orderNo : orderNo, discountPriceStr : discountPriceStr, tableNo : tableNo};

    $.ajax({
        url: "/order/discount/full/price",
        type: "post",
        data: JSON.stringify(parameter),
        dataType : "json",
        contentType: "application/json",
        success: function(data) {
            if(data > 0) {
                selectOrderList();
                $(".menu-color").removeClass("order-color");
                $("input[name=inputValue]").val(0);
            }
        },
        error: function(xhr) {
            console.log(xhr);
        }
    });
}

function fullCancel() {
    const orderNo = $("input[name=order-no]").eq(0).val();
    const tableNo = $(".table-no").val();
    const parameter = {orderNo : orderNo, tableNo : tableNo};

    $.ajax({
        url: "/order/discount/full/cancel",
        type: "post",
        data: JSON.stringify(parameter),
        dataType : "json",
        contentType: "application/json",
        success: function(data) {
            if(data > 0) {
                selectOrderList();
                $(".menu-color").removeClass("order-color");
            }
        },
        error: function(xhr) {
            console.log(xhr);
        }
    });
}

function selPer() {
    const orderNo = $("input[name=order-no]").eq(0).val();
    const menuNo = $(".order-color").children().eq(1).val();
    const percentStr = $("input[name=inputValue]").val();
    const tableNo = $(".table-no").val();
    const parameter = {orderNo : orderNo, menuNo : menuNo, percentStr : percentStr, tableNo : tableNo};

    $.ajax({
        url: "/order/discount/sel/per",
        type: "post",
        data: JSON.stringify(parameter),
        dataType : "json",
        contentType: "application/json",
        success: function(data) {
            if(data > 0) {
                selectOrderList();
                $(".menu-color").removeClass("order-color");
                $("input[name=inputValue]").val(0);
            }
        },
        error: function(xhr) {
            console.log(xhr);
        }
    });
}

function selPrice() {
    const orderNo = $("input[name=order-no]").eq(0).val();
    const menuNo = $(".order-color").children().eq(1).val();
    const discountPriceStr = $("input[name=inputValue]").val();
    const tableNo = $(".table-no").val();
    const parameter = {orderNo : orderNo, menuNo : menuNo, discountPriceStr : discountPriceStr, tableNo : tableNo};

    $.ajax({
        url: "/order/discount/sel/price",
        type: "post",
        data: JSON.stringify(parameter),
        dataType : "json",
        contentType: "application/json",
        success: function(data) {
            if(data > 0) {
                selectOrderList();
                $(".menu-color").removeClass("order-color");
                $("input[name=inputValue]").val(0);
            }
        },
        error: function(xhr) {
            console.log(xhr);
        }
    });
}

function selCancel() {
    const orderNo = $("input[name=order-no]").eq(0).val();
    const menuNo = $(".order-color").children().eq(1).val();
    const tableNo = $(".table-no").val();
    const parameter = {orderNo : orderNo, menuNo : menuNo, tableNo : tableNo};

    $.ajax({
        url: "/order/discount/sel/cancel",
        type: "post",
        data: JSON.stringify(parameter),
        dataType : "json",
        contentType: "application/json",
        success: function(data) {
            if(data > 0) {
                selectOrderList();
                $(".menu-color").removeClass("order-color");
            }
        },
        error: function(xhr) {
            console.log(xhr);
        }
    });
}

function selectMenuList() {

    $("#menu-1").html("");
    $("#menu-2").html("");
    $("#menu-3").html("");

    for(let index in data) {
        let html = "";

        html += '<div class="menu-content">';
        html +=     '<input type="hidden" name="menu-no-1">'
        if(data[index].enoughStock === true) {
            html +=     '<button type="button" class="menu-content-button">' + data[index].menuName + ' ' + data[index].salePrice.toLocaleString() + '</button>'
        } else {
            html +=     '<button type="button" class="menu-content-button" disabled>' + data[index].menuName + ' ' + data[index].salePrice.toLocaleString() + '</button>'
        }
        html += '</div>'

        if(index < 6) {
            $("#menu-1").append(html);
        } else if(index >= 6 && index < 12) {
            $("#menu-2").append(html);
        } else if(index >= 12 && index < 18) {
            $("#menu-3").append(html);
        }
    }

    for(let index in data) {
        $("input[name=menu-no-1]").eq(index).val(data[index].menuNo);
    }

    const nonClickMenu = document.querySelectorAll(".menu-content-button");
    function handleMenuClick(event) {
        nonClickMenu.forEach((e) => {
            e.classList.remove("menu-color");
        });
        event.target.classList.add("menu-color");
    }

    nonClickMenu.forEach((e) => {
        e.addEventListener("click", handleMenuClick);
    });


}