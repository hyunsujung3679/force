function addCategoryPopup() {
    $("#category-add-modal").modal({});
}

function addCategory() {
    const categoryName = $("input[name=category-name]").val();
    const useYn = $("select[name=category-use-yn]").val();
    const priority = $("input[name=category-priority]").val();
    const parameter = {categoryName : categoryName, useYn : useYn, priority : priority};

    $.ajax({
        url: "/category/insert",
        type: "post",
        data: JSON.stringify(parameter),
        dataType : "json",
        contentType: "application/json",
        success: function(data) {
            if(data > 0) {
                location.reload();
            } else {
                $(".field-error-2").text($("input[name=field-error-2]").val());
            }
        },
        error: function(xhr) {
            console.log(xhr);
        }
    });

}

$(function() {
    const nonClickRow = document.querySelectorAll(".category-content");

    function handleClick(event) {
        nonClickRow.forEach((e) => {
            e.classList.remove("category-color");
        });
        event.target.classList.add("category-color");
    }

    nonClickRow.forEach((e) => {
        e.addEventListener("click", handleClick);
    });

})

function reload() {
    location.reload();
}

function modifyCategoryPopup() {
    const categoryNo = $(".category-color").children().eq(0).val();
    const categoryName = $(".category-color").children().eq(1).text();
    const useYn = $(".category-color").children().eq(2).text();
    const priority = $(".category-color").children().eq(3).text();

    if(categoryNo === undefined) {
        $("#exception-modal").modal({});
    } else {
        $("input[name=category-no-modify]").val(categoryNo);
        $("input[name=category-name-modify]").val(categoryName);
        $("input[name=category-use-yn-modify]").val(useYn);
        $("input[name=category-priority-modify]").val(priority);

        $("#category-modify-modal").modal({});
    }
}

function modifyCategory() {
    const categoryNo = $("input[name=category-no-modify]").val();
    const categoryName = $("input[name=category-name-modify]").val();
    const useYn = $("select[name=category-use-yn-modify]").val();
    const priority = $("input[name=category-priority-modify]").val();
    const parameter = {categoryNo : categoryNo, categoryName : categoryName, useYn : useYn, priority : priority};

    $.ajax({
        url: "/category/update",
        type: "post",
        data: JSON.stringify(parameter),
        dataType : "json",
        contentType: "application/json",
        success: function(data) {
            if(data > 0) {
                location.reload();
            } else {
                $(".field-error-2-modify").text($("input[name=field-error-2-modify]").val());
            }
        },
        error: function(xhr) {
            console.log(xhr);
        }
    });
}