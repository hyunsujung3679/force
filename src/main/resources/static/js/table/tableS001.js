/* 고객를 선택하기 위한 모달창 */
function tableMove() {

    $("#table-move-modal").modal({
        fadeDuration: 1000,
        fadeDelay: 0.5
    });

    $.ajax({
        url: "/table/move/before/list",
        type: "get",
        success: function(data) {
            if(data > 0) {
                selectOrderList();
                $(".menu-color").removeClass("menu-color");
            }
        },
        error: function(xhr) {
            console.log(xhr);
        }
    });

}
