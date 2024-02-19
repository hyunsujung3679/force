function tableMove() {
    const nonClickTable = document.querySelectorAll(".line-warp");
    function handleTableClick(event) {

        nonClickTable.forEach((e) => {
            e.classList.remove("menu-color");
        });
        event.target.classList.add("menu-color");

    }

    nonClickTable.forEach((e) => {
        e.addEventListener("click", handleTableClick);
    });

}