function selectMenuListByCategoryNo() {

    const categoryNo = $(this).children[0].value;

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
                html +=         '<div class="menu-name">' + data[index].menuName + '</div>';
                html +=         '<div class="menu-price">' + data[index].salePrice.toLocaleString() + '</div>';
                html +=     '</div>'
                html += '</div>'

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

function insertOrder(div) {

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
}
