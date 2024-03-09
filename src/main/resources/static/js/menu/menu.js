function addMenuPopup() {

    $("#menu-add-modal").modal({});

    $.ajax({
        url: "/category/list",
        type: "get",
        success: function(data) {
            $("select[name=category]").html("");
            let html = "";
            for(let index in data) {
                html += '<option value=' + data[index].categoryNo + '>' + data[index].categoryName + '</option>'
            }
            $("select[name=category]").append(html);
        },
        error: function(xhr) {
            console.log(xhr);
        }
    });

    $.ajax({
        url: "/common/sale-status",
        type: "get",
        success: function(data) {
            $("select[name=sale-status]").html("");
            let html = "";
            for(let index in data) {
                html += '<option value=' + data[index].saleStatusNo + '>' + data[index].saleStatus + '</option>'
            }
            $("select[name=sale-status]").append(html);
        },
        error: function(xhr) {
            console.log(xhr);
        }
    });

    $.ajax({
        url: "/ingredient",
        type: "get",
        success: function(data) {
            $("select[name=ingredient]").html("");
            let html = "";
            for(let index in data) {
                html += '<option value=' + data[index].ingredientNo + '>' + data[index].ingredientName + '</option>'
            }
            $("select[name=ingredient]").append(html);
        },
        error: function(xhr) {
            console.log(xhr);
        }
    });
}

function addIngredient() {

    const ingredientLabel = [[#{label.ingredient}]];

    // <div class="middle-area-3">
    //     <label th:text="|#{label.ingredient} : |"></label>
    //     <select name="ingredient" class="middle-area-1-select">
    //         <option value="">광어</option>
    //         <option value="">소라</option>
    //     </select>
    //     <input type="number" min="0.1" step="0.1" name="quantity" class="middle-area-3-input"/>
    //     <label class="label-number" th:text="#{label.number}"></label>
    //     <img class="menu-image-1" src="/images/menu/plus.png" alt="plus.png" onclick="addIngredient();"/>
    // </div>
    //
    // let html = "";
    //
    // html += '<div class="middle-area-3">'
    // html += '<label>재료</label>'

}