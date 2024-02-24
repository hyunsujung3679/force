/* 고객를 선택하기 위한 모달창 */
function tableMovePopup() {

    $("#table-move-modal").modal({});

    $.ajax({
        url: "/table/exist/order/list",
        type: "get",
        success: function(data) {
            $(".before-list").html("");
            let html = "";
            for(let index in data) {
                html += '<option value=' + data[index].tableNo + '>' + data[index].tableName + '</option>'
            }
            $(".before-list").append(html);
        },
        error: function(xhr) {
            console.log(xhr);
        }
    });

    $.ajax({
        url: "/table/not/exist/order/list",
        type: "get",
        success: function(data) {
            $(".after-list").html("");
            let html = "";
            for(let index in data) {
                html += '<option value=' + data[index].tableNo + '>' + data[index].tableName + '</option>'
            }
            $(".after-list").append(html);
        },
        error: function(xhr) {
            console.log(xhr);
        }
    });

}

function tableMove() {
    const beforeTableNo = $(".before-list").val();
    const afterTableNo = $(".after-list").val();
    const parameter = {beforeTableNo : beforeTableNo, afterTableNo : afterTableNo};

    $.ajax({
        url: "/table/move",
        type: "post",
        data: JSON.stringify(parameter),
        dataType : "json",
        contentType: "application/json",
        success: function(data) {
            location.reload();
        },
        error: function(xhr) {
            console.log(xhr);
        }
    });
}

function reload() {
    location.reload();
}

function tableCombinePopup() {
    $("#table-combine-modal").modal({});

    $.ajax({
        url: "/table/exist/order/list",
        type: "get",
        success: function(data) {
            $(".first-list").html("");
            $(".second-list").html("");
            let html = "";
            for(let index in data) {
                html += '<option value=' + data[index].tableNo + '>' + data[index].tableName + '</option>'
            }
            $(".first-list").append(html);
            $(".second-list").append(html);
        },
        error: function(xhr) {
            console.log(xhr);
        }
    });

}

function tableCombine() {
    const firstTableNo = $(".first-list").val();
    const secondTableNo = $(".second-list").val();
    const parameter = {firstTableNo : firstTableNo, secondTableNo : secondTableNo};

    $.ajax({
        url: "/table/combine",
        type: "post",
        data: JSON.stringify(parameter),
        dataType : "json",
        contentType: "application/json",
        success: function(data) {
            if(data > 0) {
                location.reload();
            } else {
                alert("같은 번호로 합석 할 수 없습니다.")
            }

        },
        error: function(xhr) {
            console.log(xhr);
        }
    });
}